package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrganizationServiceTest {

    @InjectMocks
    private OrganizationService organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrganization() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setName("Organization 1");
        OrganizationDTO createdOrganization = organizationService.createOrganization(organizationDTO);

        assertNotNull(createdOrganization);
        assertEquals("Organization 1", createdOrganization.getName());
    }

    @Test
    public void testGetAllOrganizations() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.findAll()).thenReturn(Arrays.asList(organization));

        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();

        assertNotNull(organizations);
        assertEquals(1, organizations.size());
        assertEquals("Organization 1", organizations.get(0).getName());
    }

    @Test
    public void testGetOrganizationById() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        OrganizationDTO organizationDTO = organizationService.getOrganizationById(1L);

        assertNotNull(organizationDTO);
        assertEquals("Organization 1", organizationDTO.getName());
    }

    @Test
    public void testUpdateOrganization() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setName("Updated Organization");
        OrganizationDTO updatedOrganization = organizationService.updateOrganization(1L, organizationDTO);

        assertNotNull(updatedOrganization);
        assertEquals("Updated Organization", updatedOrganization.getName());
    }

    @Test
    public void testDeleteOrganization() {
        doNothing().when(organizationRepository).deleteById(1L);

        organizationService.deleteOrganization(1L);

        verify(organizationRepository, times(1)).deleteById(1L);
    }
}