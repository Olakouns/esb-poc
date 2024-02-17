package sn.esmt.gesb.tpo_manager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Verb is required")
    private String verb;
    private String tpoCondition;
    @NotBlank(message = "TPO is required")
    private String tpo;
    @Column(columnDefinition = "TEXT")
    private String description;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<TPOWorkOrder> patterns = new LinkedList<>();
}
