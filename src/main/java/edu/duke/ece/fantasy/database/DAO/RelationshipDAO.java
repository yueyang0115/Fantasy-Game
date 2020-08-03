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

    public void applyFriend(int senderId, int receiverId) {
        Relationship r = new Relationship(senderId, receiverId, Relationship.RelationStatus.approved);
        HibernateUtil.save(r);
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
        PlayerDAO playerDAO = MetaDAO.getPlayerDAO();
        // use playerId as both receiver and sender since player could be both of them
        List<Relationship> r1 = HibernateUtil.queryList("From Relationship r where r.receiverId=:playerId and r.status=:status",
                Relationship.class,
                new String[]{"playerId", "status"},
                new Object[]{playerId, Relationship.RelationStatus.approved});
        List<Relationship> r2 = HibernateUtil.queryList("From Relationship r where r.senderId=:playerId and r.status=:status",
                Relationship.class,
                new String[]{"playerId", "status"},
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
