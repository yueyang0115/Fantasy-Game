package edu.duke.ece.fantacy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.* ;

public class DBprocessor {
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

            String dropPlayer = "DROP TABLE IF EXISTS PLAYER CASCADE;";
            String dropMap = "DROP TABLE IF EXISTS MAP CASCADE;";

            stmt = c.createStatement();
            String sqlPlayer = "CREATE TABLE PLAYER" +
                    "(PLAYERNAME TEXT PRIMARY KEY NOT NULL," +
                    " WID       INT      NOT NULL," +
                    " PASSWORD  TEXT     NOT NULL)";
            stmt.executeUpdate(dropPlayer);
            stmt.executeUpdate(sqlPlayer);
            System.out.println("Built PLAYER table successfully");

            String sqlMap = "CREATE TABLE MAP" +
                    "(WID INT PRIMARY KEY     NOT NULL," +
                    " X       REAL," +
                    " Y       REAL," +
                    " STATUS  TEXT     NOT NULL)";
                    //"CONSTRAINT CLEAR FOREIGN KEY (WID) REFERENCES PLAYER(WID) ON DELETE SET NULL ON UPDATE CASCADE);";
            stmt.executeUpdate(dropMap);
            stmt.executeUpdate(sqlMap);
            System.out.println("Built MAP table successfully");

            //stmt.close();
            //c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
    }

    public void addUser(String username, String password,  int wid){
        try{c.setAutoCommit(false);
        //System.out.println("Opened database successfully");

        String curr = "";
        String worldId = "";
        //String temp = "";
        worldId = String.valueOf(wid);
        curr = "\'"+username+"\'";
        //temp = "\'"+password+"\'";

        stmt = c.createStatement();
        String sql = "INSERT INTO PLAYER (PLAYERNAME,WID,PASSWORD) "
                + "VALUES (" + curr + ", " + worldId + ", " + password + ");";
        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();
        //c.close();
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Added Player information successfully");

    }

    public int checkUser(String username, String password) {
        try {
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String curr = "\'"+username+"\'";
            String sql = "SELECT * FROM PLAYER WHERE PLAYERNAME =" + curr + ";";
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next()){
                return -1;
            }
            else{
                int wid = rs.getInt("wid");
                //System.out.printf("we get wid: %d\n", wid);
                String passwd = rs.getString("password");
                //System.out.printf("we get passwd is: %s\n", passwd);
                if(passwd.equals(password)){
                    return wid;
                }
                else{
                    return -2;
                }
            }

        }catch (Exception e){
             e.printStackTrace();
             System.err.println(e.getClass().getName()+": "+e.getMessage());
             System.exit(0);
         }
        return 0;
    }

    public Boolean checkTerritory(int wid, double x, double y){
        try {

            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String worldId = "";
            worldId = String.valueOf(wid);

            String cordx = String.format ("%.12f", x);
            String cordy = String.format ("%.12f", y);
            String ans = "";


            String sql = "SELECT * FROM MAP WHERE WID = " + worldId + " AND X = " + cordx +
                    " AND Y = " + cordy + ";";
            ResultSet rs = stmt.executeQuery(sql);

            //System.out.println( "HERE = " + rs );
//            while(rs.next()) {
//                System.out.printf("1\n");
//                ans = rs.getString("status");
//                System.out.println( "STATUS = " + ans );
//            }
            if(!rs.next()){
                return false;
            }
            else{
                ans = rs.getString("status");
                if(ans.equals("")){
                    return false;
                }else{
                    System.out.printf("STATUS = %s\n",ans);
                    return true;
                }
            }


        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return true;
    }


    public void addTerritory(int wid, double x, double y, String status){
        try{c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            String worldId = "";
            String curr = "";
            worldId = String.valueOf(wid);
            //System.out.println( "WID = " + worldId );
            String cordx = String.format ("%.12f", x);
            //System.out.println( "X = " + cordx );
            String cordy = String.format ("%.12f", y);
            //System.out.println( "Y = " + cordy );
            curr = "\'"+status+"\'";
            //System.out.println( "STATUS = " + curr );

            stmt = c.createStatement();
            String sql = "INSERT INTO MAP (WID,X,Y,STATUS) "
                    + "VALUES (" + worldId + ", " + cordx + ", " + cordy + ", " + curr + ");";
            stmt.executeUpdate(sql);

//            String sql1 = "SELECT * FROM MAP;";
//            ResultSet rs = stmt.executeQuery(sql1);
//
//            String ans = "";
//            while(rs.next()) {
//                System.out.printf("1\n");
//                ans = rs.getString("status");
//                System.out.println( "STATUS = " + ans );
//            }
            stmt.close();
            c.commit();
            //c.close();
        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Added Map information successfully");
    }
/*
    public void updateTerritory(int wid,double x,double y,String status){
    }

    /*public Territory getTerritory(int wid,double x,double y){
    }*/
}
