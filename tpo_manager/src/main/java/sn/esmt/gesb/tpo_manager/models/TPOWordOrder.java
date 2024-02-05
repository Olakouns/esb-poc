package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class TPOWordOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String webServiceName;
    private String template;
    private String equipment;
}
