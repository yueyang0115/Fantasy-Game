package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.levelUp.TableInitializer;
import edu.duke.ece.fantasy.net.SocketServer;
import edu.duke.ece.fantasy.task.TaskHandler;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Server {
    private SocketServer socketServer;
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public Server() {
    }

    public void startGame() throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            // create a jdbc connection for table initialization
            SessionImplementor sessImpl = (SessionImplementor) session;
            Connection connection = sessImpl.getJdbcConnectionAccess().obtainConnection();

            // initialize SkillTable
            TableInitializer tableInitializer = new TableInitializer(session, connection);
            tableInitializer.initializeAll();
            session.getTransaction().commit();
        } catch (SQLException e) {
            System.out.println("Failed to create session and jdbc connection");
            e.printStackTrace();
        }
        System.out.println("initial end");

        TaskHandler.INSTANCE.initialize();

        socketServer = new SocketServer();
        socketServer.start();
        logger.info("socket start");
    }

    public static void main(String[] args) {
//        Server server = new Server(1234, 5678);
        Server server = new Server();
        try {
            server.startGame();
        } catch (Exception e) {
            logger.error("server start failed", e);
            System.exit(-1);
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("close session factory");
                HibernateUtil.shutdown();
            }));
        }
    }
}
