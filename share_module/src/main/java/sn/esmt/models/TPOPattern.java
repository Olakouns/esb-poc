package sn.esmt.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TPOPattern {
    @Id
    private Long id;
    private String patternName;
    private String dataMappingCode;
    private boolean isTemplate;
}
