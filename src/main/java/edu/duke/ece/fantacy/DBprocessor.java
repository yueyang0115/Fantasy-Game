package edu.duke.ece.fantacy;
import java.sql.*;

public class DBprocessor {

    public void connectDB() {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5433/testdb",
                            "postgres", "123");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void addTerritory(int wid,double x,double y,String status){

    }
    public void updateTerritory(int wid,double x,double y,String status){}
    public boolean checkTerritory(int wid,double x,double y){return false;}
    public Territory getTerritory(int wid,double x,double y){return null;}
}
