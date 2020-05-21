package edu.duke.ece.fantacy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBProcessor {
    Connection c = null;
    Statement stmt = null;

    /**
     * this method connect postgres sql and create several tables
     */
    public void connectDB() {
        //connect db
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://rajje.db.elephantsql.com:5432/ylbwemyh",
                            "ylbwemyh", "kOHhsRdraZXpIQXgFzmYIv2TpvGOEBqe");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        //create user table
        try {
            stmt = c.createStatement();
            String sqlUser = "CREATE TABLE USER " +
                    "(WID INT PRIMARY KEY     NOT NULL," +
                    " USERNAME       TEXT    NOT NULL, " +
                    " PASSWORD       TEXT     NOT NULL)";
            stmt.executeUpdate(sqlUser);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //create map table

    }

    public void addTerritory(int wid, double x, double y,String status){
    }

    public void updateTerritory(int wid,double x,double y,String status){
    }

    /*public Territory getTerritory(int wid,double x,double y){
    }*/
}
