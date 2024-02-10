package sn.esmt.gesb.tpo_manager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TPOData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Verb is required")
    private String verb;

    private String condition;

    @NotBlank(message = "TPO is required")
    private String tpo;
    @Column(columnDefinition = "TEXT")
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<TPOWordOrder> patterns = new LinkedList<>();
    @ManyToOne(cascade = CascadeType.PERSIST)
    private TPOData tpoDataOnFailure;
}
