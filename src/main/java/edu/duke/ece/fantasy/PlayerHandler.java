package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.levelUp.TableInitializer;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.task.MonsterGenerator;
import edu.duke.ece.fantasy.task.MonsterMover;
import edu.duke.ece.fantasy.task.ResourceGenerator;
import edu.duke.ece.fantasy.task.TaskScheduler;
import org.hibernate.Session;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PlayerHandler extends Thread {
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
        new Thread(() -> receiveMessage()).start();
        new Thread(() -> handleAll()).start();
        sendMessage();
    }

    //one thread for receiving msg from client and adding it to requestMsgQueue
    private void receiveMessage() {
        while (!TCPcommunicator.isClosed()) {
            try {
                MessagesC2S request = TCPcommunicator.receive();
                if (TCPcommunicator.isClosed()) break;
                String request_str = "";
                request_str = myObjectMapper.writeValueAsString(request);
                System.out.println("[DEBUG] TCPcommunicator successfully receive:" + request_str);
                // add received request to requestMsgQueue
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

    // one thread for taking resultMsg out of resultMsgQueue and sending it to client
    private void sendMessage() {
        while (!TCPcommunicator.isClosed()) {
            try {
                // take one resultMsg out of resultMsgQueue and send it
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

    //one thread for handling all received msg and doing automatically tasks
    private void handleAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
//        HibernateUtil.getSessionFactory().getCurrentSession();
        MetaDAO metaDAO = new MetaDAO(session);
        // sharedData for sharing player info between taskScheduler and messageHandler
        SharedData sharedData = new SharedData();
        // messageHandler for handling received msg
        MessageHandler messageHandler = new MessageHandler(metaDAO, sharedData);
        // taskScheduler for doing automatically tasks
        TaskScheduler taskScheduler = new TaskScheduler();

        boolean taskIsAdded = false;
        boolean tableIsInitialized = false;

        while(!TCPcommunicator.isClosed()) {
            session.beginTransaction();

            //add tasks in taskScheduler when sharedData hold a player( this happens after login)
            if (!taskIsAdded && sharedData.getPlayer() != null) {
                MonsterGenerator monsterGenerator = new MonsterGenerator(System.currentTimeMillis(), 1000, true, metaDAO, sharedData, resultMsgQueue);
                MonsterMover monsterMover = new MonsterMover(System.currentTimeMillis(), 7000, true, metaDAO, sharedData, resultMsgQueue);
                ResourceGenerator resourceGenerator = new ResourceGenerator(System.currentTimeMillis(), 1000, true, sharedData);
                taskScheduler.addTask(monsterGenerator);
                taskScheduler.addTask(monsterMover);
                taskScheduler.addTask(resourceGenerator);
                taskIsAdded = true;
            }

            //handle server automatically generated tasks
            long TimeUntilNextTask = taskScheduler.getTimeToNextTask();
            if (TimeUntilNextTask <= 0) {
                // at least the first task in taskQueue should be executed
                taskScheduler.runReadyTasks();
            }
            TimeUntilNextTask = taskScheduler.getTimeToNextTask();

            //handle received client msg
            MessagesC2S request;
            try {
                // take one request out of requestMsgQueue
                request = requestMsgQueue.poll(TimeUntilNextTask, TimeUnit.MILLISECONDS);
                if (request != null) {
                    // handle request, add resultMsg to resultMsgQueue
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
