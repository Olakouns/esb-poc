package sn.esmt.gesb.tpo_manager.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbParameter;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.soam.VerbType;
import sn.esmt.gesb.tpo_manager.exceptions.ResourceNotFoundException;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TPOServiceImpl implements TPOService {

    private final TPODataRepository tpoDataRepository;
    private final ModelMapper modelMapper;

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
}
