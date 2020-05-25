package edu.duke.ece.fantacy;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
    @Test
    public void createTest(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Add new Employee object
        Monster emp = new Monster();
        emp.setHp(200);
        emp.setAtk(30);
        emp.setType("wolf");

        session.save(emp);

        session.getTransaction().commit();
        session.close();
        HibernateUtil.shutdown();
    }
}