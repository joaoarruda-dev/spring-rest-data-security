package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.service.GroupService;
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

public class GroupResourceTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupResource groupResource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllGroups() {
        List<GroupDTO> responseDTOList = Collections.singletonList(new GroupDTO());
        when(groupService.getAll()).thenReturn(responseDTOList);

        ResponseEntity<List<GroupDTO>> response = groupResource.getAllGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
    }

    @Test
    public void testGetGroupById() {
        Long groupId = 1L;
        GroupDTO responseDTO = new GroupDTO();
        when(groupService.findById(groupId)).thenReturn(responseDTO);

        ResponseEntity<GroupDTO> response = groupResource.getGroupById(groupId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testUpdateGroup() {
        Long groupId = 1L;
        GroupDTO request = new GroupDTO();
        GroupDTO responseDTO = new GroupDTO();
        when(groupService.save(groupId, request)).thenReturn(responseDTO);

        ResponseEntity<GroupDTO> response = groupResource.update(groupId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testRegisterGroup() {
        GroupDTO request = new GroupDTO();
        GroupDTO responseDTO = new GroupDTO();
        when(groupService.save(request)).thenReturn(responseDTO);

        ResponseEntity<GroupDTO> response = groupResource.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testDeleteGroup() {
        Long groupId = 1L;

        ResponseEntity<Void> response = groupResource.update(groupId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}