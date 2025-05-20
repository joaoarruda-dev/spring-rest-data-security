package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.OrganizationDTO;
import br.edu.fatecsjc.lgnspringapi.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrganizationResourceTest {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationResource organizationResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrganization() {
        OrganizationDTO request = new OrganizationDTO();
        OrganizationDTO responseDTO = new OrganizationDTO();
        when(organizationService.createOrganization(any(OrganizationDTO.class))).thenReturn(responseDTO);

        ResponseEntity<OrganizationDTO> response = organizationResource.createOrganization(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testGetAllOrganizations() {
        List<OrganizationDTO> responseDTOList = Collections.singletonList(new OrganizationDTO());
        when(organizationService.getAllOrganizations()).thenReturn(responseDTOList);

        ResponseEntity<List<OrganizationDTO>> response = organizationResource.getAllOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
    }

    @Test
    void testGetOrganizationById() {
        Long organizationId = 1L;
        OrganizationDTO responseDTO = new OrganizationDTO();
        when(organizationService.getOrganizationById(organizationId)).thenReturn(responseDTO);

        ResponseEntity<OrganizationDTO> response = organizationResource.getOrganizationById(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testUpdateOrganization() {
        Long organizationId = 1L;
        OrganizationDTO request = new OrganizationDTO();
        OrganizationDTO responseDTO = new OrganizationDTO();
        when(organizationService.updateOrganization(organizationId, request)).thenReturn(responseDTO);

        ResponseEntity<OrganizationDTO> response = organizationResource.updateOrganization(organizationId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void testDeleteOrganization() {
        Long organizationId = 1L;

        ResponseEntity<Void> response = organizationResource.deleteOrganization(organizationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}