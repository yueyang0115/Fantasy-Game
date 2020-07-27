package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Relationship;
import org.hibernate.Session;

public class RelationshipDAO {
    Session session;

    public RelationshipDAO(Session session) {
        this.session = session;
    }

    public void applyFriend(int senderId,int receiverId){
        Relationship r = new Relationship(senderId,receiverId,Relationship.RelationStatus.pending);
        session.save(r);
    }
}
