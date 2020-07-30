package edu.duke.ece.fantasy.database.DAO;

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

    public void applyFriend(int senderId,int receiverId){
        Relationship r = new Relationship(senderId,receiverId,Relationship.RelationStatus.approved);
        session.save(r);
    }

    public boolean checkFriend(int playerId,int possibleFriendId){
        Query<Relationship> q1 = session.createQuery("From Relationship r where r.receiverId=:receiverId and r.senderId=:senderId", Relationship.class);
        q1.setParameter("receiverId", playerId);
        q1.setParameter("senderId",possibleFriendId);
        if(q1.uniqueResult()!=null) return true;
        q1.setParameter("receiverId", possibleFriendId);
        q1.setParameter("senderId",playerId);
        return q1.uniqueResult() != null;
    }

    public List<Player> getApprovedFriendList(int playerId){
        List<Player> FriendList = new ArrayList<>();
        TypedQuery<Relationship> q1 = session.createQuery("From Relationship r where r.receiverId=:playerId and r.status=:status", Relationship.class);
        q1.setParameter("playerId", playerId);
        q1.setParameter("status", Relationship.RelationStatus.approved);
        TypedQuery<Relationship> q2 = session.createQuery("From Relationship r where r.senderId=:playerId and r.status=:status", Relationship.class);
        q2.setParameter("status", Relationship.RelationStatus.approved);
        q2.setParameter("playerId", playerId);
        PlayerDAO playerDAO = new PlayerDAO(session);
        for(Relationship r:q1.getResultList()){
            FriendList.add(playerDAO.getPlayer(r.getSenderId()));
        }
        for(Relationship r:q2.getResultList()){
            FriendList.add(playerDAO.getPlayer(r.getReceiverId()));
        }
        return FriendList;
    }

    public void getPendingListByReceiverId(int receiverId){

    }

    public void getPendingListBySenderId(int senderId){

    }
}
