package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.Item.Item;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ItemDAO {
    Session session;

    public ItemDAO(Session session) {
        this.session = session;
    }

//    public List<String> getAllItem(){
//        return ;
//    }

//    public Item getItem(String name) {
//        Query q = session.createQuery("From Item I where I.name =:name");
//        q.setParameter("name", name);
//        Item res = (Item) q.uniqueResult();
//        return res;
//    }


}
