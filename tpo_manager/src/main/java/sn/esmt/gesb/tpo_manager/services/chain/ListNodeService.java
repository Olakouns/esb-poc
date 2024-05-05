package sn.esmt.gesb.tpo_manager.services.chain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.models.chain.ListNode;
import sn.esmt.gesb.tpo_manager.repositories.chain.ListNodeRepository;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class ListNodeService {
    private final ListNodeRepository listNodeRepository;

    public ListNode createListNode(TPOWorkOrder tpoWorkOrder) {
        ListNode listNode = new ListNode();
        listNode.setDataId(tpoWorkOrder.getId());
        listNode.setNextNode(null);
        return listNodeRepository.save(listNode);
    }

    public ListNode createListNode(LinkedList<TPOWorkOrder> tpoWorkOrders) {
        ListNode head = new ListNode();
        ListNode current = head;

        var index = 0;

        for (TPOWorkOrder data : tpoWorkOrders) {
            ListNode node = new ListNode();
            node.setDataId(data.getId());
            if (index == 0) {
                head = node;
            } else {
                current.setNextNode(node);
            }
            current = node;
            index++;
        }
        return listNodeRepository.save(head);
    }

    public void deleteListNode(int id) {
        listNodeRepository.deleteById(id);
    }

    public void addEnd(ListNode listNode, TPOWorkOrder tpoWordOrder) {
        ListNode lastListNode = listNode;
        while (lastListNode.getNextNode() != null) {
            lastListNode = lastListNode.getNextNode();
        }
        lastListNode.setNextNode(createListNode(tpoWordOrder));
        listNodeRepository.save(lastListNode);
    }
}
