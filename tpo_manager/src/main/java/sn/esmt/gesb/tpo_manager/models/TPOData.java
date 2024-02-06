package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
public class TPOData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String verb;
    private String condition;
    private String tpo;
    @Column(columnDefinition = "TEXT")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TPOWordOrder> patterns = new LinkedList<>();
    @ManyToOne
    private TPOData tpoDataOnFailure;
}
