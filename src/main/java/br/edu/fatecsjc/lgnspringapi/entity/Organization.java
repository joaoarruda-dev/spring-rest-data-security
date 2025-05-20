package br.edu.fatecsjc.lgnspringapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @SequenceGenerator(initialValue = 1, allocationSize = 1, name = "organizationidgen", sequenceName = "organization_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
