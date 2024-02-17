package sn.esmt.gesb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkflowStep {
    private String url;
    private String bodyContent;
    private LinkedList<WorkflowStep> failureSteps = new LinkedList<>();
}
