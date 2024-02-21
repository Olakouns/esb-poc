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
public class TPODataDto {
    private int id;
    private String tpo;
    private String description;
    private boolean isCritical;
    private TPOWorkOrderDto previousStateData;
    private List<TPOWorkOrderDto> patterns;
}
