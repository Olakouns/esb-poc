package sn.esmt.gesb.tpo_manager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.JDOMException;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.soam.EsbParameter;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.soam.VerbType;
import sn.esmt.gesb.tpo_manager.builder.MappingBuilder;
import sn.esmt.gesb.tpo_manager.exceptions.BadRequestException;
import sn.esmt.gesb.tpo_manager.exceptions.RequestNotAcceptableException;
import sn.esmt.gesb.tpo_manager.exceptions.ResourceNotFoundException;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.repositories.ConstantConfigRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TPOServiceImpl implements TPOService {

    private final TPODataRepository tpoDataRepository;
    private final ModelMapper modelMapper;
    private final MappingBuilder mappingBuilder;
    private final ConstantConfigRepository configurationRepository;

    @Override
    public TPODataDto getTPODataOfRequest(EsbRootActionRequest esbRootActionRequest) {
        VerbType verb = esbRootActionRequest.getEsbContent().getVerb();
        String condition = "";
        Optional<EsbParameter> esbParameterOptional = esbRootActionRequest.getEsbContent().getEsbParameter().stream().filter(esbParameter -> esbParameter.getName().equals("subscriberType")).findFirst();
        if (esbParameterOptional.isPresent()) {
            condition = esbParameterOptional.get().getName();
        }

        Specification<TPOData> specification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("verb"), verb.name());
        if (!condition.isEmpty()) {
            specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("condition")));
        }
        return modelMapper.map(tpoDataRepository.findOne(specification).orElseThrow(() -> new ResourceNotFoundException("TPOData", "verb", verb.name())), TPODataDto.class);
    }

    @Override
    public Workflow getMappingData(int tpoId, EsbRootActionRequest esbRootActionRequest) {
        TPOData tpoData = tpoDataRepository.findById(tpoId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoId));
        Workflow workflow = new Workflow();
        try {
            for (TPOWorkOrder pattern : tpoData.getPatterns()) {
                WorkflowStep workflowStep = buildWorkflowStep(pattern, esbRootActionRequest);
                if (pattern.getTpoWorkOrderFailure().size() > 0) {
                    for (TPOWorkOrder tpoWorkOrderFailure : pattern.getTpoWorkOrderFailure()) {
                        WorkflowStep workflowStepFailure = buildWorkflowStep(tpoWorkOrderFailure, esbRootActionRequest);
                        workflowStep.getFailureSteps().add(workflowStepFailure);
                    }
                }
                workflow.getWorkflowSteps().add(workflowStep);
            }
        } catch (IOException | JDOMException e) {
            // TODO: 2/16/2024 Manage exception correctly
            throw new BadRequestException("TPOData id : " + tpoId);
        }
        return workflow;
    }

    private WorkflowStep buildWorkflowStep(TPOWorkOrder pattern, EsbRootActionRequest esbRootActionRequest) throws IOException, JDOMException {
        WorkflowStep workflowStep = new WorkflowStep();
        String template = mappingBuilder.buildSOAPTemplate(pattern.getTemplate(), esbRootActionRequest);
        workflowStep.setBodyContent(template);
        if (configurationRepository.findByKeyName(pattern.getEquipment().toUpperCase()).isPresent()) {
            workflowStep.setUrl(configurationRepository.findByKeyName(pattern.getEquipment().toUpperCase()).get().getValueContent());
        } else {
            throw new RequestNotAcceptableException("tpo_manager.equipment");
        }
        return workflowStep;
    }
}
