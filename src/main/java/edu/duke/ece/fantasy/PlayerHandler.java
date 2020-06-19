package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

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
    private LinkedBlockingQueue<MessagesS2C> messageS2CQueue;
    Logger log = LoggerFactory.getLogger(Player.class);

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.messageHandler = new MessageHandler();
//        this.monsterHandler = new MonsterHandler();
        this.messageS2CQueue = new LinkedBlockingQueue<>();
    }

    public void run() {
        new Thread(()-> receiveMessage()).start();
        new Thread(()-> sendMessage()).start();
        new Thread(()->generateMonsters()).start();
        new Thread(()->generateMonsterMessage()).start();
    }

    public void receiveMessage(){
        while(!TCPcommunicator.isClosed()){
            try{
                MessagesC2S request = TCPcommunicator.receive();
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
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop receiving, close corresponding thread in server");
    }

    public void sendMessage(){
        while(!TCPcommunicator.isClosed()){
            try {
                if (!messageS2CQueue.isEmpty()) {
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
        TCPcommunicator.close();
        System.out.println("[DEBUG] Client socket might closed, stop sending, close corresponding thread in server");
    }

    public void generateMonsters(){
        Timer timer = new Timer();
        while(!TCPcommunicator.isClosed()){
            timer.schedule(new MonsterGenerator(this.currentCoord, canGenerateMonster), 0, 1000);
        }
        System.out.println("[DEBUG] Client socket might closed, stop generating monster");
    }

    public void generateMonsterMessage(){
        Timer timer = new Timer();
        while(!TCPcommunicator.isClosed()){
            timer.schedule(new MonsterDetector(canGenerateMonster, messageS2CQueue),0,1200);
        }
        System.out.println("[DEBUG] Client socket might closed, stop sending changed monster");
    }

}
