package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MarathonResourceTest {

    @Mock
    private MarathonService marathonService;

    @InjectMocks
    private MarathonResource marathonResource;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(marathonResource).build();
    }

    @Test
    void testGetAllMarathons() throws Exception {
        MarathonDTO marathonDTO = new MarathonDTO(1L, "Marathon1", 10, 100, null);
        List<MarathonDTO> marathonList = Collections.singletonList(marathonDTO);

        when(marathonService.getAllMarathons()).thenReturn(marathonList);

        mockMvc.perform(get("/marathons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Marathon1"));
    }

    @Test
    void testGetMarathonById() throws Exception {
        MarathonDTO marathonDTO = new MarathonDTO(1L, "Marathon1", 10, 100, null);

        when(marathonService.getMarathonById(anyLong())).thenReturn(marathonDTO);

        mockMvc.perform(get("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Marathon1"));
    }

    @Test
    void testCreateMarathon() throws Exception {
        MarathonDTO marathonDTO = new MarathonDTO(1L, "Marathon1", 10, 100, null);

        when(marathonService.createMarathonWithoutMembers(any(MarathonDTO.class))).thenReturn(marathonDTO);

        mockMvc.perform(post("/marathons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Marathon1\",\"weight\":10,\"score\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Marathon1"));
    }

    @Test
    void testUpdateMarathon() throws Exception {
        MarathonDTO marathonDTO = new MarathonDTO(1L, "UpdatedMarathon", 20, 200, null);

        when(marathonService.updateMarathon(anyLong(), any(MarathonDTO.class))).thenReturn(marathonDTO);

        mockMvc.perform(put("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedMarathon\",\"weight\":20,\"score\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedMarathon"));
    }

    @Test
    void testDeleteMarathon() throws Exception {
        mockMvc.perform(delete("/marathons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddMembersToMarathon() throws Exception {
        MarathonDTO marathonDTO = new MarathonDTO(1L, "Marathon1", 10, 100, null);

        when(marathonService.addMembersToMarathon(anyLong(), anyList())).thenReturn(marathonDTO);

        mockMvc.perform(post("/marathons/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1, 2, 3]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Marathon1"));
    }
}