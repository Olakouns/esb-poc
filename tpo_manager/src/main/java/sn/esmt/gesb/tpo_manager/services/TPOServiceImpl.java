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
import sn.esmt.gesb.soam.EsbService;
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
import java.util.List;
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
        }else {
            log.error("subscriberType not present");
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
                if(pattern.isServiceTemplate()){
                    if (esbRootActionRequest.getEsbContent().getEsbServices() == null || esbRootActionRequest.getEsbContent().getEsbServices().getEsbService().isEmpty()) {
//                        todo : throw exception here
                    }
                    for (EsbService esbService : esbRootActionRequest.getEsbContent().getEsbServices().getEsbService()) {
                        // todo : check here otherwise clone object
                        List<EsbParameter> esbParameters =  esbRootActionRequest.getEsbContent().getEsbParameter();
                        esbParameters.addAll(esbService.getEsbParameter());
                        workflow.getWorkflowSteps().add(builderStep(pattern, esbParameters));
                    }
                } else {
                    workflow.getWorkflowSteps().add(builderStep(pattern,  esbRootActionRequest.getEsbContent().getEsbParameter()));
                }
            }
        } catch (IOException | JDOMException e) {
            // TODO: 2/16/2024 Manage exception correctly
            throw new BadRequestException("TPOData id : " + tpoId);
        }
        return workflow;
    }

    private WorkflowStep builderStep(TPOWorkOrder pattern, List<EsbParameter> esbParameters) throws IOException, JDOMException {
        WorkflowStep workflowStep = buildWorkflowStep(pattern, esbParameters);
        if (!pattern.getTpoWorkOrderFailure().isEmpty()) {
            for (TPOWorkOrder tpoWorkOrderFailure : pattern.getTpoWorkOrderFailure()) {
                WorkflowStep workflowStepFailure = buildWorkflowStep(tpoWorkOrderFailure, esbParameters);
                workflowStep.getFailureSteps().add(workflowStepFailure);
            }
        }
        return workflowStep;
    }

    private WorkflowStep buildWorkflowStep(TPOWorkOrder pattern, List<EsbParameter> esbParameters) throws IOException, JDOMException {
        WorkflowStep workflowStep = new WorkflowStep();
        String template = mappingBuilder.buildSOAPTemplate(pattern.getTemplate(), esbParameters);
        workflowStep.setBodyContent(template);
        if (configurationRepository.findByKeyName(pattern.getEquipment().toUpperCase()).isPresent()) {
            workflowStep.setUrl(configurationRepository.findByKeyName(pattern.getEquipment().toUpperCase()).get().getValueContent());
        } else {
            throw new RequestNotAcceptableException("tpo_manager.equipment");
        }
        return workflowStep;
    }
}
