package sn.esmt.gesb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Workflow {
    private LinkedList<WorkflowStep> workflowSteps = new LinkedList<>();
}
