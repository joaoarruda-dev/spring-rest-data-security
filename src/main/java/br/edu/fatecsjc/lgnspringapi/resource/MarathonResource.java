package br.edu.fatecsjc.lgnspringapi.resource;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.service.MarathonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marathons")
@RequiredArgsConstructor
@Tag(name = "Marathon")
@SecurityRequirement(name = "bearerAuth")
public class MarathonResource {

    private final MarathonService marathonService;

    @GetMapping
    @Operation(description = "Get all marathons", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<List<MarathonDTO>> getAllMarathons() {
        List<MarathonDTO> marathons = marathonService.getAllMarathons();
        return ResponseEntity.ok(marathons);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get marathon by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<MarathonDTO> getMarathonById(@PathVariable Long id) {
        MarathonDTO marathon = marathonService.getMarathonById(id);
        return ResponseEntity.ok(marathon);
    }

    @PostMapping
    @Operation(description = "Create a new marathon", responses = {
            @ApiResponse(description = "Success", responseCode = "201"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<MarathonDTO> createMarathon(@RequestBody MarathonDTO marathonDTO) {
        MarathonDTO createdMarathon = marathonService.createMarathonWithoutMembers(marathonDTO);
        return ResponseEntity.status(201).body(createdMarathon);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update marathon by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<MarathonDTO> updateMarathon(@PathVariable Long id, @RequestBody MarathonDTO marathonDTO) {
        MarathonDTO updatedMarathon = marathonService.updateMarathon(id, marathonDTO);
        return ResponseEntity.ok(updatedMarathon);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete marathon by ID", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<Void> deleteMarathon(@PathVariable Long id) {
        marathonService.deleteMarathon(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/members")
    @Operation(description = "Add members to marathon", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Unknown error", responseCode = "400")
    })
    public ResponseEntity<MarathonDTO> addMembersToMarathon(@PathVariable Long id, @RequestBody List<Long> memberIds) {
        MarathonDTO updatedMarathon = marathonService.addMembersToMarathon(id, memberIds);
        return ResponseEntity.ok(updatedMarathon);
    }
}