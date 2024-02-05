package sn.esmt.gesb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TPODataDto {
    private int id;
    private String tpo;
    private List<TPOWordOrderDto> patterns;
    private TPODataDto tpoDataOnFailure;
}
