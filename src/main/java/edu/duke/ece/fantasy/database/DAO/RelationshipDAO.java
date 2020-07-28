package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Relationship;
import org.hibernate.Session;

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

    public List<Player> getApprovedFriendList(int playerId){
        List<Player> FriendList = new ArrayList<>();
        TypedQuery<Relationship> q1 = session.createQuery("From Relationship r where r.receiverId=:playerId and r.status=RelationStatus.approved", Relationship.class);
        q1.setParameter("playerId", playerId);
        TypedQuery<Relationship> q2 = session.createQuery("From Relationship r where r.senderId=:playerId and r.status=RelationStatus.approved", Relationship.class);
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
