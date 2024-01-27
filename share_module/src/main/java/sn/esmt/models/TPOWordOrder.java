package sn.esmt.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.List;


@Entity
public class TPOWordOrder {

    @Id
    private int id;
    private String tpo;
    private String templateName;
    private String equipment;
    @ElementCollection
    private List<String> workflowFunctions;
    @ManyToOne
    private TPOData tpoDataOnFailure;
}
