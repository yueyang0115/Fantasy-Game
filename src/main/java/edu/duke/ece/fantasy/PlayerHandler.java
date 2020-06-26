package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.task.MonsterGenerator;
import edu.duke.ece.fantasy.task.MonsterMover;
import edu.duke.ece.fantasy.task.TaskScheduler;
import org.hibernate.Session;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PlayerHandler extends Thread{
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private ObjectMapper myObjectMapper;
    private LinkedBlockingQueue<MessagesS2C> resultMsgQueue;
    private LinkedBlockingQueue<MessagesC2S> requestMsgQueue;

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm) {
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.resultMsgQueue = new LinkedBlockingQueue<>();
        this.requestMsgQueue = new LinkedBlockingQueue<>();
    }

    public void run() {
        new Thread(()-> receiveMessage()).start();
        new Thread(()-> handleAll() ).start();
        sendMessage();
    }

    private void receiveMessage() {
        while (!TCPcommunicator.isClosed()) {
            try {
                MessagesC2S request = TCPcommunicator.receive();
                if (TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);
                requestMsgQueue.offer(request);
            } catch (IOException e) {
                e.printStackTrace();
                if (TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop receiving, close corresponding thread in server");
    }

    private void sendMessage() {
        while (!TCPcommunicator.isClosed()) {
            try {
                MessagesS2C msg = resultMsgQueue.take();
                TCPcommunicator.send(msg);
                if (TCPcommunicator.isClosed()) break;
                String result_str = "";
                result_str = myObjectMapper.writeValueAsString(msg);
                System.out.println("[DEBUG] TCPcommunicator successfully send " + result_str);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                if (TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop sending, close corresponding thread in server");
    }

    private void handleAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        MetaDAO metaDAO = new MetaDAO(session);
        SharedData sharedData = new SharedData();
        MessageHandler messageHandler = new MessageHandler(metaDAO, sharedData);
        TaskScheduler taskScheduler = new TaskScheduler();

        boolean taskIsAdded = false;
        while(!TCPcommunicator.isClosed()) {
            session.beginTransaction();
            //handle server automatically generated tasks
            if(!taskIsAdded && sharedData.getPlayer() != null) {
                MonsterGenerator monsterGenerator = new MonsterGenerator(System.currentTimeMillis(), 1000, true, metaDAO, sharedData, resultMsgQueue);
                MonsterMover monsterMover = new MonsterMover(System.currentTimeMillis(), 7000, true, metaDAO, sharedData,  resultMsgQueue);
                taskScheduler.addTask(monsterGenerator);
                taskScheduler.addTask(monsterMover);
                taskIsAdded = true;
            }

            long TimeUntilNextTask = taskScheduler.getTimeToNextTask();
            if (TimeUntilNextTask <= 0) {
                // at least the first task should be executed
                taskScheduler.runReadyTasks();
            }
            TimeUntilNextTask = taskScheduler.getTimeToNextTask();

            //handle server in-response-to-client tasks
            MessagesC2S request;
            try {
                request = requestMsgQueue.poll(TimeUntilNextTask, TimeUnit.MILLISECONDS);
                if (request != null) {
                    MessagesS2C result = messageHandler.handle(request);
                    resultMsgQueue.offer(result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            session.getTransaction().commit();
        }
    }


}
