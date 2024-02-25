package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOWorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String webServiceName;
    private String webServiceClassName;
    private boolean isServiceTemplate;
    @Column(columnDefinition = "TEXT")
    private String template;
    private String equipment;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tpo_work_failure_id")
    private List<TPOWorkOrder> tpoWorkOrderFailure = new ArrayList<>();
}
