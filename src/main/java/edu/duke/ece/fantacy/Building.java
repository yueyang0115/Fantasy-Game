package edu.duke.ece.fantacy;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Inheritance
@DiscriminatorColumn(name="TYPE")
@Table(name="Building")
public class Building {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "TYPE", unique = false, nullable = false, length = 100)
    private String type;


}
