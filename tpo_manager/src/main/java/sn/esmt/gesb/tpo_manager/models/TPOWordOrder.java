package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class TPOWordOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String webServiceName;
    @Column(columnDefinition = "TEXT")
    private String template;
    private String equipment;
}
