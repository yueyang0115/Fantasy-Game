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
    volatile WorldCoord[] currentCoord = new WorldCoord[1];
    volatile boolean[] canGenerateMonster  = new boolean[1];
    private Timer generateMonsterTimer = new Timer();
    private Timer checkUpdatedMonsterTimer = new Timer();
    private Timer moveMonsterTimer = new Timer();
    private TCPCommunicator TCPcommunicator;
    private UDPCommunicator UDPcommunicator;
    private ObjectMapper myObjectMapper;
    private MessageHandler messageHandler;
    private LinkedBlockingQueue<MessagesS2C> messageS2CQueue;
    Logger log = LoggerFactory.getLogger(Player.class);

    public PlayerHandler(TCPCommunicator TCPcm, UDPCommunicator UDPcm){
        this.TCPcommunicator = TCPcm;
        this.UDPcommunicator = UDPcm;
        this.myObjectMapper = new ObjectMapper();
        this.messageHandler = new MessageHandler(currentCoord, canGenerateMonster);
        this.messageS2CQueue = new LinkedBlockingQueue<>();
        canGenerateMonster[0] = false;
    }

    public void run() {
        new Thread(()-> sendMessage()).start();
        new Thread(()-> generateMonsters()).start();
        new Thread(()-> checkUpdatedMonsters()).start();
        new Thread(()-> moveMonsters()).start();
        receiveMessage();
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
        stopTimer();
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
        stopTimer();
        System.out.println("[DEBUG] Client socket might closed, stop sending, close corresponding thread in server");
    }

    public void generateMonsters(){
        while(!canGenerateMonster[0]){
        }
        generateMonsterTimer.schedule(new MonsterGenerator(this.currentCoord, this.canGenerateMonster), 0, 1000);
    }

    public void checkUpdatedMonsters(){
        while(!canGenerateMonster[0]){
        }
        checkUpdatedMonsterTimer.schedule(new MonsterDetector(this.canGenerateMonster, this.messageS2CQueue),0,5000);
    }

    public void moveMonsters(){
        while(!canGenerateMonster[0]){
        }
        moveMonsterTimer.schedule(new MonsterMover(this.currentCoord, this.canGenerateMonster), 0, 3000);
    }

    public void stopTimer(){
        generateMonsterTimer.cancel();
        generateMonsterTimer.purge();
        checkUpdatedMonsterTimer.cancel();
        checkUpdatedMonsterTimer.purge();
        moveMonsterTimer.cancel();
        moveMonsterTimer.purge();
    }
}
