package sn.esmt.gesb.tpo_manager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.esmt.gesb.tpo_manager.models.chain.ListNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tpo_work_failure_id")
    private List<TPOWorkOrder> tpoWorkOrderFailure = new ArrayList<>();
    private boolean canBeDelete;
    @JsonIgnore
    @ManyToOne
    private ListNode listNode;
    @Transient
    private List<TPOWorkOrder> linkedList;
    public List<TPOWorkOrder> getLinkedList() {
        LinkedList<TPOWorkOrder> patternsLink = new LinkedList<>();
        ListNode current = listNode;
        if (current == null) {
            return tpoWorkOrderFailure;
        }
        do {
            ListNode finalCurrent = current;
            patternsLink.add(tpoWorkOrderFailure.stream().filter(p -> p.getId() == finalCurrent.getDataId()).findFirst().orElse(null));
            current = current.getNextNode();
        } while (current != null);
        return patternsLink;
    }
}
