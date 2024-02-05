package sn.esmt.gesb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPOWordOrderDto {
    private String webServiceName;
    private String template;
    private String equipment;
}
