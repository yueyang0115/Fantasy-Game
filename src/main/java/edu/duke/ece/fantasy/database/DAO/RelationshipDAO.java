package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.*;
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
        Query<Relationship> q = session.createQuery("From Relationship r where r.receiverId=:receiverId and r.senderId=:senderId",
                Relationship.class);
        String[] paraName = new String[]{"receiverId", "senderId"};
        Object[] para = new Object[]{playerId, possibleFriendId};
        HibernateUtil.assignMutilplePara(q, paraName, para);

        Relationship r1 = q.uniqueResult();
        if (r1 != null) {
            return true;
        }
        q = session.createQuery("From Relationship r where r.receiverId=:receiverId and r.senderId=:senderId",
                Relationship.class);
        paraName = new String[]{"receiverId", "senderId"};
        para = new Object[]{playerId, possibleFriendId};
        HibernateUtil.assignMutilplePara(q, paraName, para);
        Relationship r2 = q.uniqueResult();
        return r2 != null;
    }

    public List<Player> getApprovedFriendList(int playerId) {
        List<Player> FriendList = new ArrayList<>();
        PlayerDAO playerDAO = new PlayerDAO(session);
        // use playerId as both receiver and sender since player could be both of them
        Query<Relationship> q = session.createQuery(
                "From Relationship r where r.receiverId=:playerId or r.senderId=:playerId and r.status=:status",
                Relationship.class);
        String[] paraName = new String[]{"playerId", "status"};
        Object[] para = new Object[]{playerId, Relationship.RelationStatus.approved};
        HibernateUtil.assignMutilplePara(q, paraName, para);
        List<Relationship> relationships = q.getResultList();
        for (Relationship r : relationships) {
            if (r.getReceiverId() != playerId) {
                FriendList.add(playerDAO.getPlayer(r.getReceiverId()));
            } else {
                FriendList.add(playerDAO.getPlayer(r.getSenderId()));
            }

        }
        return FriendList;
    }

    public void getPendingListByReceiverId(int receiverId) {

    }

    public void getPendingListBySenderId(int senderId) {

    }
}
