package br.edu.fatecsjc.lgnspringapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDTO {

    private Long id;
    private String name;
    private String addressNumber;
    private String addressStreet;
    private String addressNeighborhood;
    private String addressZipcode;
    private String addressCity;
    private String addressState;
    private String institutionName;
    private String country;
}
