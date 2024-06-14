package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOWorkOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String webServiceName;
    private String webServiceClassName;
    private boolean isServiceTemplate;
    @Column(columnDefinition = "TEXT")
    private String template;
    private String equipment;
    private boolean canBeDelete;
    @Transient
    private TpoFailureState tpoFailureState;
}
