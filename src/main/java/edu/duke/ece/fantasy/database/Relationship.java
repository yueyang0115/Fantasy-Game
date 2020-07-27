package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Relationship")
public class Relationship {
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
    private String status;

    @Temporal(TemporalType.DATE)
    private Date startDate;
}
