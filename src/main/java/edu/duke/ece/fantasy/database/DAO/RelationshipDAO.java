package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Relationship;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class RelationshipDAO {
    Session session;

    public RelationshipDAO(Session session) {
        this.session = session;
    }

    public void applyFriend(int senderId, int receiverId) {
        Relationship r = new Relationship(senderId, receiverId, Relationship.RelationStatus.approved);
        session.save(r);
    }

    public boolean checkFriend(int playerId, int possibleFriendId) {
        if (HibernateUtil.queryOne("From Relationship r where r.receiverId=:receiverId and r.senderId=:senderId",
                Relationship.class,
                new String[]{"receiverId", "senderId"},
                new Object[]{playerId, possibleFriendId}) != null) {
            return true;
        }
        return HibernateUtil.queryOne("From Relationship r where r.receiverId=:receiverId and r.senderId=:senderId",
                Relationship.class,
                new String[]{"receiverId", "senderId"},
                new Object[]{playerId, possibleFriendId}) != null;
    }

    public List<Player> getApprovedFriendList(int playerId) {
        List<Player> FriendList = new ArrayList<>();
//        TypedQuery<Relationship> q1 = session.createQuery();
//        q1.setParameter("playerId", playerId);
//        q1.setParameter("status", Relationship.RelationStatus.approved);
//        TypedQuery<Relationship> q2 = session.createQuery("From Relationship r where r.senderId=:playerId and r.status=:status", Relationship.class);
//        q2.setParameter("status", Relationship.RelationStatus.approved);
//        q2.setParameter("playerId", playerId);
        PlayerDAO playerDAO = MetaDAO.getPlayerDAO();
        List<Relationship> r1 = HibernateUtil.queryList("From Relationship r where r.receiverId=:playerId and r.status=:status",
                Relationship.class,
                new String[]{"receiverId", "status"},
                new Object[]{playerId, Relationship.RelationStatus.approved});
        List<Relationship> r2 = HibernateUtil.queryList("From Relationship r where r.receiverId=:playerId and r.status=:status",
                Relationship.class,
                new String[]{"senderId", "status"},
                new Object[]{playerId, Relationship.RelationStatus.approved});
        for (Relationship r : r1) {
            FriendList.add(playerDAO.getPlayer(r.getSenderId()));
        }
        for (Relationship r : r2) {
            FriendList.add(playerDAO.getPlayer(r.getReceiverId()));
        }
        return FriendList;
    }

    public void getPendingListByReceiverId(int receiverId) {

    }

    public void getPendingListBySenderId(int senderId) {

    }
}
