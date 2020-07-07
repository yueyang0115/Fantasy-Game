package edu.duke.ece.fantasy;

import java.net.*;
import java.io.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.json.MessagesC2S;
import edu.duke.ece.fantasy.json.MessagesS2C;
import org.json.*;

public class TCPCommunicator {

    public Socket socket;
    private ObjectMapper objectMapper;
    private InputStream in;
    private OutputStream out;
    private boolean isShutdown;
    private int sendfailNum;
    private int recvfailNum;
    private int FAIL_LIMIT = 2;

    public TCPCommunicator(ServerSocket serverSocket) {
        init();
        try {
            this.socket = serverSocket.accept();
            while(this.socket ==null){
                this.socket = serverSocket.accept();
            }
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            System.out.println("[DEBUG] TCP communicator successfully accept player socket!");
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to accept player socket!");
            e.printStackTrace();
        }
    }

    public TCPCommunicator(String ip, int port) {
        init();
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to crete socket in client-side");
            e.printStackTrace();
        }
    }

     private void init(){
        this.isShutdown = false;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,false);
    }

    public void send(MessagesS2C msg) {
        if(socket.isOutputShutdown()){
            isShutdown = true;
            return;
        }
        try {
            objectMapper.writeValue(out, msg);
            sendfailNum = 0;
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to send data");
            sendfailNum++;
            // if send msg fails more than FAIL_LIMIT, socket might closed
            // make flag isShutdown to be true, we can check TCPcm.isClosed() to stop the program
            if(sendfailNum > FAIL_LIMIT){
                sendfailNum = 0;
                isShutdown = true;
                System.out.println("[DEBUG] Client might closed");
            }
            e.printStackTrace();
        }
    }

    public MessagesC2S receive() {
        MessagesC2S res = new MessagesC2S();
        try {
            res = objectMapper.readValue(in, MessagesC2S.class);
            recvfailNum = 0;
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to receive data");
            recvfailNum++;
            // if receive msg fails more than FAIL_LIMIT, socket might closed
            // make flag isShutdown to be true, we can check TCPcm.isClosed() to stop the program
            if(recvfailNum > FAIL_LIMIT){
                recvfailNum = 0;
                isShutdown = true;
                System.out.println("[DEBUG] Client might closed");
            }
            e.printStackTrace();
        }
        return res;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("[DEBUG] TCP communicator failed to close socket!");
            e.printStackTrace();
        }
    }

    // we can check TCPcm.isClosed() to stop the program
    public boolean isClosed(){
        return this.isShutdown;
    }
}
