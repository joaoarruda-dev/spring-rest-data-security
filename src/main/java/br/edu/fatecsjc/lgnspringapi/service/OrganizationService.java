package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = mapToEntity(organizationDTO);
        Organization savedOrganization = organizationRepository.save(organization);
        return mapToDTO(savedOrganization);
    }

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public OrganizationDTO getOrganizationById(Long id) {
        Optional<Organization> organization = organizationRepository.findById(id);
        return organization.map(this::mapToDTO).orElse(null);
    }

    public OrganizationDTO updateOrganization(Long id, OrganizationDTO organizationDTO) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (organizationOptional.isPresent()) {
            Organization organization = organizationOptional.get();
            organization.setName(organizationDTO.getName());
            organization.setAddressNumber(organizationDTO.getAddressNumber());
            organization.setAddressStreet(organizationDTO.getAddressStreet());
            organization.setAddressNeighborhood(organizationDTO.getAddressNeighborhood());
            organization.setAddressZipcode(organizationDTO.getAddressZipcode());
            organization.setAddressCity(organizationDTO.getAddressCity());
            organization.setAddressState(organizationDTO.getAddressState());
            organization.setInstitutionName(organizationDTO.getInstitutionName());
            organization.setCountry(organizationDTO.getCountry());
            Organization updatedOrganization = organizationRepository.save(organization);
            return mapToDTO(updatedOrganization);
        } else {
            return null;
        }
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    private OrganizationDTO mapToDTO(Organization organization) {
        return OrganizationDTO.builder()
                .id(organization.getId())
                .name(organization.getName())
                .addressNumber(organization.getAddressNumber())
                .addressStreet(organization.getAddressStreet())
                .addressNeighborhood(organization.getAddressNeighborhood())
                .addressZipcode(organization.getAddressZipcode())
                .addressCity(organization.getAddressCity())
                .addressState(organization.getAddressState())
                .institutionName(organization.getInstitutionName())
                .country(organization.getCountry())
                .build();
    }

    private Organization mapToEntity(OrganizationDTO organizationDTO) {
        return Organization.builder()
                .id(organizationDTO.getId())
                .name(organizationDTO.getName())
                .addressNumber(organizationDTO.getAddressNumber())
                .addressStreet(organizationDTO.getAddressStreet())
                .addressNeighborhood(organizationDTO.getAddressNeighborhood())
                .addressZipcode(organizationDTO.getAddressZipcode())
                .addressCity(organizationDTO.getAddressCity())
                .addressState(organizationDTO.getAddressState())
                .institutionName(organizationDTO.getInstitutionName())
                .country(organizationDTO.getCountry())
                .build();
    }
}