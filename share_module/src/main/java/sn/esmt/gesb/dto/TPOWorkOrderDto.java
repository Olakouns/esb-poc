package sn.esmt.gesb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOWorkOrderDto {
    private String webServiceName;
    private String template;
    private String equipment;
    private List<TPOWorkOrderDto> tpoWorkOrderFailure;
}
