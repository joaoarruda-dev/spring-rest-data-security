package br.edu.fatecsjc.lgnspringapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarathonDTO {
    private Long id;
    private String name;
    private int weight;
    private int score;
    private List<SimpleMemberDTO> members;
}