package sn.esmt.gesb.tpo_manager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.esmt.gesb.tpo_manager.models.chain.ListNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOData implements Serializable {
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
    @JsonIgnore
    @ManyToOne
    private ListNode listNode;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<TPOWorkOrder> patterns = new LinkedList<>();
    private boolean isCritical;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<TPOWorkOrder> previousStatesData;

    public List<TPOWorkOrder> getPatterns() {
        LinkedList<TPOWorkOrder> patternsLink = new LinkedList<>();
        ListNode current = listNode;
        if (current == null) {
            return patterns;
        }
        do {
            ListNode finalCurrent = current;
            patternsLink.add(patterns.stream().filter(p -> p.getId() == finalCurrent.getDataId()).findFirst().orElse(null));
            current = current.getNextNode();
        } while (current != null);
        return patternsLink;
    }
}
