package edu.duke.ece.fantacy;
import java.util.*;

public class MockDBprocessor {
    private HashMap<String, Integer> userMap;

    public MockDBprocessor(){
        this.userMap = new HashMap<>();
    }

    public void create(){ }

    public int checkUser(String username, String password){
        return userMap.getOrDefault(username, -1);
    }

    public void addUser(String username, String password, int wid){
        userMap.put("username",wid);
    }

}
