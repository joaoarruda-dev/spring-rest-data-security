package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Organization;
import br.edu.fatecsjc.lgnspringapi.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    @InjectMocks
    private OrganizationService organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrganization() {
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
    void testGetAllOrganizations() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.findAll()).thenReturn(List.of(organization));

        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();

        assertNotNull(organizations);
        assertEquals(1, organizations.size());
        assertEquals("Organization 1", organizations.get(0).getName());
    }

    @Test
    void testGetAllOrganizationsEmpty() {
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();

        assertNotNull(organizations);
        assertTrue(organizations.isEmpty());
    }

    @Test
    void testGetOrganizationById() {
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Organization 1");
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        OrganizationDTO organizationDTO = organizationService.getOrganizationById(1L);

        assertNotNull(organizationDTO);
        assertEquals("Organization 1", organizationDTO.getName());
    }

    @Test
    void testGetOrganizationByIdNotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        OrganizationDTO organizationDTO = organizationService.getOrganizationById(1L);

        assertNull(organizationDTO);
    }

    @Test
    void testUpdateOrganization() {
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
    void testUpdateOrganizationNotFound() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());

        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setName("Updated Organization");
        OrganizationDTO updatedOrganization = organizationService.updateOrganization(1L, organizationDTO);

        assertNull(updatedOrganization);
    }

    @Test
    void testDeleteOrganization() {
        doNothing().when(organizationRepository).deleteById(1L);

        organizationService.deleteOrganization(1L);

        verify(organizationRepository, times(1)).deleteById(1L);
    }
}