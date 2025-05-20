package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.dto.SimpleMemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
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

class MarathonServiceTest {

    @InjectMocks
    private MarathonService marathonService;

    @Mock
    private MarathonRepository marathonRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMarathons() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setName("Marathon 1");
        marathon.setWeight(10);
        marathon.setScore(100);
        when(marathonRepository.findAll()).thenReturn(Arrays.asList(marathon));

        List<MarathonDTO> marathons = marathonService.getAllMarathons();

        assertNotNull(marathons);
        assertEquals(1, marathons.size());
        assertEquals("Marathon 1", marathons.get(0).getName());
    }

    @Test
    void testGetMarathonById() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setName("Marathon 1");
        marathon.setWeight(10);
        marathon.setScore(100);
        when(marathonRepository.findById(1L)).thenReturn(Optional.of(marathon));

        MarathonDTO marathonDTO = marathonService.getMarathonById(1L);

        assertNotNull(marathonDTO);
        assertEquals("Marathon 1", marathonDTO.getName());
    }

    @Test
    void testCreateMarathonWithoutMembers() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setName("Marathon 1");
        marathon.setWeight(10);
        marathon.setScore(100);
        when(marathonRepository.save(any(Marathon.class))).thenReturn(marathon);

        MarathonDTO marathonDTO = new MarathonDTO(null, "Marathon 1", 10, 100, null);
        MarathonDTO createdMarathon = marathonService.createMarathonWithoutMembers(marathonDTO);

        assertNotNull(createdMarathon);
        assertEquals("Marathon 1", createdMarathon.getName());
    }

    @Test
    void testUpdateMarathon() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setName("Marathon 1");
        marathon.setWeight(10);
        marathon.setScore(100);
        when(marathonRepository.findById(1L)).thenReturn(Optional.of(marathon));
        when(marathonRepository.save(any(Marathon.class))).thenReturn(marathon);

        MarathonDTO marathonDTO = new MarathonDTO(null, "Updated Marathon", 20, 200, null);
        MarathonDTO updatedMarathon = marathonService.updateMarathon(1L, marathonDTO);

        assertNotNull(updatedMarathon);
        assertEquals("Updated Marathon", updatedMarathon.getName());
    }

    @Test
    void testAddMembersToMarathon() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        marathon.setName("Marathon 1");
        marathon.setWeight(10);
        marathon.setScore(100);
        Member member = new Member();
        member.setId(1L);
        member.setName("Member 1");
        when(marathonRepository.findById(1L)).thenReturn(Optional.of(marathon));
        when(memberRepository.findAllById(anyList())).thenReturn(Arrays.asList(member));
        when(marathonRepository.save(any(Marathon.class))).thenReturn(marathon);

        MarathonDTO updatedMarathon = marathonService.addMembersToMarathon(1L, Arrays.asList(1L));

        assertNotNull(updatedMarathon);
        assertEquals(1, updatedMarathon.getMembers().size());
        assertEquals("Member 1", updatedMarathon.getMembers().get(0).getName());
    }

    @Test
    void testDeleteMarathon() {
        doNothing().when(marathonRepository).deleteById(1L);

        marathonService.deleteMarathon(1L);

        verify(marathonRepository, times(1)).deleteById(1L);
    }
}