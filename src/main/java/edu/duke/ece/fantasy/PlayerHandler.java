package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

public class PlayerHandler extends Thread{
    private int wid;
    private WorldCoord currentCoord;
    private boolean canGenerateMonster;
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private DBprocessor myDBprocessor;
    private ObjectMapper myObjectMapper;
    MessageHandler messageHandler;
    private MonsterGenerator monsterGenerator;
    private Queue<MessagesS2C> messageS2CQueue;
    Logger log = LoggerFactory.getLogger(Player.class);

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.messageHandler = new MessageHandler();
//        this.monsterHandler = new MonsterHandler();
        this.messageS2CQueue = new LinkedList<>();
    }

    public void run() {
        new Thread(()->startReceive()).start();
        new Thread(()->startSend()).start();
        //new Thread(()->startGenrateMonster()).start();
    }

    public void startReceive(){
        System.out.println("getting into receive");
        while(!TCPcommunicator.isClosed()){
            try{
                MessagesC2S request = TCPcommunicator.receive();
                System.out.println("have received msg");
                if(TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);
//                Instant start = Instant.now();

                MessagesS2C result = messageHandler.handle(request);
                wid = messageHandler.getWid();
                currentCoord = messageHandler.getCurrentCoord();
                canGenerateMonster = messageHandler.getCanGenerateMonster();
                messageS2CQueue.offer(result);

//                TCPcommunicator.send(result);
//                if(TCPcommunicator.isClosed()) break;
//                String result_str = "";
//                result_str = myObjectMapper.writeValueAsString(result);
//                System.out.println("[DEBUG] TCPcommunicator successfully send " +result_str);
//                Instant end = Instant.now();
//                Duration timeElapsed  = Duration.between(start,end);
//                log.info("time used in handling message is {} Nanoseconds",timeElapsed.getNano());
            }
            catch(IOException e){
                e.printStackTrace();
                if(TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
//        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, close corresponding thread in server");
    }

    public void startSend(){
        System.out.println("getting into send");
        while(!TCPcommunicator.isClosed()){
            try {
                if (!messageS2CQueue.isEmpty()) {
                    System.out.println("queue is not Empty");
                    MessagesS2C msg = messageS2CQueue.poll();
                    TCPcommunicator.send(msg);
                    if (TCPcommunicator.isClosed()) break;
                    String result_str = "";
                    result_str = myObjectMapper.writeValueAsString(msg);
                    System.out.println("[DEBUG] TCPcommunicator successfully send " + result_str);
                }
            }
            catch(IOException e){
                e.printStackTrace();
                if(TCPcommunicator.isClosed()) {
                    System.out.println("[DEBUG] Client socket might closed, prepare to exit");
                }
            }
        }
//        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, close corresponding thread in server");
    }

    public void startGenrateMonster(){
        while(!TCPcommunicator.isClosed()){
            Timer timer = new Timer();
            timer.schedule(new MonsterGenerator(this.currentCoord, canGenerateMonster), 0, 1000);
        }
    }

}
