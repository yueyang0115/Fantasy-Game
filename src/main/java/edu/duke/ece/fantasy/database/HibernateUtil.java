package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.ObjectMapperFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public static <T> T get(Class<?> entity, Serializable id) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        T res = (T) session.get(entity, id);
        session.getTransaction().commit();
        return res;
    }

    public static void save(Object obj) {
        Session dbSession = getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        dbSession.save(obj);
        dbSession.getTransaction().commit();
    }

    public static void saveOrUpdate(Object obj) {
        Session dbSession = getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        dbSession.saveOrUpdate(obj);
        dbSession.getTransaction().commit();
    }

    public static void update(Object obj) {
        Session dbSession = getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        dbSession.update(obj);
        dbSession.getTransaction().commit();
    }

    public static void delete(Object obj) {
        Session dbSession = getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        dbSession.delete(obj);
        dbSession.getTransaction().commit();
    }

    public static void execute(String queryString, String paraName, Object para) {
        Session session = getSessionFactory().getCurrentSession();
        Query q = session.createQuery(queryString);
        q.setParameter(paraName, para);
        q.executeUpdate();
    }

    public static <T> T queryOne(String queryString, Class<?> entity, String[] parameterName, Object[] parameter) {
        Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        Query<?> q = HibernateUtil.getSessionFactory().getCurrentSession().createQuery(queryString, entity);
        for (int i = 0; i < parameter.length; i++) {
            q.setParameter(parameterName[i], parameter[i]);
        }
        T res = (T) q.uniqueResult();
        dbSession.getTransaction().commit();
        return res;
    }

    public static <T> List<T> queryList(String queryString, Class<?> entity, String[] parameterName, Object[] parameter) {
        Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
        dbSession.beginTransaction();
        Query<?> q = HibernateUtil.getSessionFactory().getCurrentSession().createQuery(queryString, entity);
        for (int i = 0; i < parameter.length; i++) {
            q.setParameter(parameterName[i], parameter[i]);
        }
        List<T> res = (List<T>) q.getResultList();
        dbSession.getTransaction().commit();
        return res;
    }
}
