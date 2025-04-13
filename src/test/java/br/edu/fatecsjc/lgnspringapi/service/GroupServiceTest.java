package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupConverter groupConverter;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<Group> groups = Arrays.asList(new Group(), new Group());
        List<GroupDTO> groupDTOs = Arrays.asList(new GroupDTO(), new GroupDTO());

        when(groupRepository.findAll()).thenReturn(groups);
        when(groupConverter.convertToDto(groups)).thenReturn(groupDTOs);

        List<GroupDTO> result = groupService.getAll();

        assertEquals(groupDTOs, result);
        verify(groupRepository).findAll();
        verify(groupConverter).convertToDto(groups);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Group group = new Group();
        GroupDTO groupDTO = new GroupDTO();

        when(groupRepository.findById(id)).thenReturn(Optional.of(group));
        when(groupConverter.convertToDto(group)).thenReturn(groupDTO);

        GroupDTO result = groupService.findById(id);

        assertEquals(groupDTO, result);
    }

    @Test
    public void testSaveWithId() {
        Long id = 1L;
        GroupDTO groupDTO = new GroupDTO();
        Group group = new Group();

        when(groupRepository.findById(id)).thenReturn(Optional.of(group));
        when(groupConverter.convertToEntity(any(GroupDTO.class), any(Group.class))).thenReturn(group);
        when(groupRepository.save(any(Group.class))).thenReturn(group);
        when(groupConverter.convertToDto(any(Group.class))).thenReturn(groupDTO);

        GroupDTO result = groupService.save(id, groupDTO);

        assertEquals(groupDTO, result);
        verify(memberRepository).deleteMembersByGroup(group);
    }

    @Test
    public void testSaveWithoutId() {
        GroupDTO groupDTO = new GroupDTO();
        Group group = new Group();

        when(groupConverter.convertToEntity(any(GroupDTO.class))).thenReturn(group);
        when(groupRepository.save(any(Group.class))).thenReturn(group);
        when(groupConverter.convertToDto(any(Group.class))).thenReturn(groupDTO);

        GroupDTO result = groupService.save(groupDTO);

        assertEquals(groupDTO, result);
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        doNothing().when(groupRepository).deleteById(id);

        groupService.delete(id);

        verify(groupRepository).deleteById(id);
    }
}