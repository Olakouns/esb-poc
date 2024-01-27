package sn.esmt.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.LinkedList;
import java.util.List;

@Entity
public class TPOData {

    @Id
    private int id;
    private String resource;
    private String verb;
    private String condition;
    private String tpo;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TPOPattern> patterns = new LinkedList<>();
}
