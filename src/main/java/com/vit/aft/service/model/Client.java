package com.vit.aft.service.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "client",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",
            nullable = false,
            unique = true)
    private long id;

    @Column(name = "user_name",
            length = 100,
            unique = true)
    private String clientName;

    @Column(name = "hobbies")
    private String hobbies;

    @Column(name = "greeting")
    private String mainGreeting;

}
