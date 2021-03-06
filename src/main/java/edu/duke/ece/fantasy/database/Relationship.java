package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Relationship")
public class Relationship {
    public enum RelationStatus{
        pending,approved
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "senderId", unique = true, nullable = false)
    private int senderId;

    @Column(name = "receiverId", unique = true, nullable = false)
    private int receiverId;

    @Column(name = "status",nullable = false)
    private RelationStatus status;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    public Relationship() {
    }

    public Relationship(int senderId, int receiverId, RelationStatus status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public RelationStatus getStatus() {
        return status;
    }

    public void setStatus(RelationStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
