package edu.duke.ece.fantacy;
import org.json.*;

public class Territory {
    int wid;
    double x;
    double y;
    String status;

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONObject toJSON(){
        JSONObject territory_obj = new JSONObject();
        territory_obj.put("x",this.x);
        territory_obj.put("y",this.y);
        territory_obj.put("status",this.status);
        territory_obj.put("wid",this.wid);
        return  territory_obj;
    }
}
