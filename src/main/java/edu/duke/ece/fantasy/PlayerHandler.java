package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.hibernate.Session;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerHandler extends Thread{
    private WorldCoord[] currentCoord = new WorldCoord[1];
    private boolean[] canGenerateMonster  = new boolean[1];

    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private ObjectMapper myObjectMapper;
    private MessageHandler messageHandler;
    private LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    private LinkedBlockingQueue<MessagesC2S> requestMsgQueue;
    private Session monsterSession ;
    private TaskScheduler taskScheduler;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.messageHandler = new MessageHandler(currentCoord, canGenerateMonster);
        this.resultMsgQueue = new LinkedBlockingQueue<>();
        this.requestMsgQueue = new LinkedBlockingQueue<>();
        canGenerateMonster[0] = false;
        this.taskScheduler = new TaskScheduler();
        this.monsterSession = HibernateUtil.getSessionFactory().openSession();
    }

    public void run() {
        MonsterGenerator monsterGenerator = new MonsterGenerator(System.currentTimeMillis(), 1000, true, monsterSession, currentCoord, canGenerateMonster, resultMsgQueue);
        MonsterMover monsterMover = new MonsterMover(System.currentTimeMillis(), 7000, true, monsterSession, currentCoord, canGenerateMonster, resultMsgQueue);
        taskScheduler.addTask(monsterGenerator);
        taskScheduler.addTask(monsterMover);
        new Thread(()-> receiveMessage()).start();
        new Thread(()-> handleAll() ).start();
        sendMessage();
    }

    private void receiveMessage(){
        while(!TCPcommunicator.isClosed()){
            try{
                MessagesC2S request = TCPcommunicator.receive();
                if(TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);
                requestMsgQueue.offer(request);
            }

            catch(IOException e){
                e.printStackTrace();
                if(TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop receiving, close corresponding thread in server");
    }

    private void sendMessage(){
        while(!TCPcommunicator.isClosed()){
            try {
                MessagesS2C msg = resultMsgQueue.take();
                TCPcommunicator.send(msg);
                if (TCPcommunicator.isClosed()) break;
                String result_str = "";
                result_str = myObjectMapper.writeValueAsString(msg);
                System.out.println("[DEBUG] TCPcommunicator successfully send " + result_str);
            }
            catch(IOException | InterruptedException e){
                e.printStackTrace();
                if(TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop sending, close corresponding thread in server");
    }

    private void handleAll(){
        while(!TCPcommunicator.isClosed()) {
            //handle server automatically generated tasks
            long TimeUntilNextTask = taskScheduler.getTimeToNextTask();
            if(TimeUntilNextTask <= 0){
                // at least the first task should be executed
                taskScheduler.runReadyTasks();
            }

            //handle server in-response-to-client tasks
            MessagesC2S request = requestMsgQueue.poll();
            if (request != null) {
                MessagesS2C result = messageHandler.handle(request);
                resultMsgQueue.offer(result);
            }
        }
    }


}
