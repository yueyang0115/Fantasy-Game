package edu.duke.ece.fantacy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;

public class TerritoryHandler {
    SessionFactory sf = HibernateUtil.getSessionFactory();
    private double width_unit = 0.0002;
    private double height_unit = 0.0002;
    private static double[][] offset = {{0, 0.0002}};
    private static ArrayList<Double> x_offset = new ArrayList<>(Arrays.asList(0.0, 0.0002, -0.0002));
    private static ArrayList<Double> y_offset = new ArrayList<>(Arrays.asList(0.0, 0.0002, -0.0002));

    public TerritoryHandler() {
    }


    public int[] MillierConvertion(double lat, double lon) {
        double L = 6381372 * Math.PI * 2;//地球周长
        double W=L;// 平面展开后，x轴等于周长
        double H=L/2;// y轴约等于周长一半
        double mill=2.3;// 米勒投影中的一个常数，范围大约在正负2.3之间
        int x = (int)(lon * Math.PI) / 180;// 将经度从度数转换为弧度
        int y = (int)(lat * Math.PI) / 180;// 将纬度从度数转换为弧度
        y= (int) (1.25 * Math.log( Math.tan( 0.25 * Math.PI + 0.4 * y ) ));// 米勒投影的转换
        // 弧度转为实际距离
        x = (int)(( W / 2 ) + ( W / (2 * Math.PI) ) )* x;
        y = (int) (( H / 2 ) - ( H / ( 2 * mill ) ) )* y;
        int[] result=new int[2];
        result[0]=x;
        result[1]=y;
        return result;
    }


    // given coordination, return list of territory
    public List<Territory> getTerritories(int wid, double x, double y) {
        Session session = sf.openSession();
        session.beginTransaction();

        List<Territory> res = new ArrayList<>();
        // find the center of block
        double center_x = (int) (x / width_unit) * width_unit + width_unit / 2;
        double center_y = (int) (y / height_unit) * height_unit + height_unit / 2;

        // add neighbor block to database, update status of center block
        for (Double x_off : x_offset) {
            for (Double y_off : y_offset) {
                double target_x = center_x + x_off;
                double target_y = center_y + y_off;
                if (x > 180 || x < -180 || y > 90 || y < -90) continue;
                Territory t = getTerritory(wid, target_x, target_y);
                if (t == null) {
                    // if territory haven't been tracked, add it to database
                    t = new Territory(wid, target_x, target_y,"unexplored");
                    t.addMonster(new Monster("wolf",100,10));
                    session.save(t);
                }
                if (x_off == 0 && y_off == 0) {
                    // change the status of center territory
                    t.setStatus("explored");
                    session.update(t);
                }
                res.add(t);
            }
        }
        session.getTransaction().commit();
        session.close();
        return res;
    }

    public void addTerritory(int wid, double x, double y, String status) {
        Session session = sf.openSession();
        session.beginTransaction();
    }

    public void updateTerritory(int wid, int x,int y){

    }

    public Territory getTerritory(int wid, double x, double y) {
        // select territory according to conditions
        Session session = sf.openSession();
        session.beginTransaction();
        Query q = session.createQuery("From Territory M where M.wid =:wid and M.x =:x and M.y = :y");
        q.setParameter("wid", wid);
        q.setParameter("x", x);
        q.setParameter("y", y);
        Territory res = (Territory) q.uniqueResult();
        session.close();
        return res;
    }
}
