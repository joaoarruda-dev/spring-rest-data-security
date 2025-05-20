package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.dto.MarathonDTO;
import br.edu.fatecsjc.lgnspringapi.dto.SimpleMemberDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import br.edu.fatecsjc.lgnspringapi.entity.Member;
import br.edu.fatecsjc.lgnspringapi.repository.MarathonRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarathonService {

    private final MarathonRepository marathonRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public MarathonService(MarathonRepository marathonRepository, MemberRepository memberRepository) {
        this.marathonRepository = marathonRepository;
        this.memberRepository = memberRepository;
    }

    public List<MarathonDTO> getAllMarathons() {
        return marathonRepository.findAll().stream()
                .map(marathon -> new MarathonDTO(
                        marathon.getId(),
                        marathon.getName(),
                        marathon.getWeight(),
                        marathon.getScore(),
                        marathon.getMembers().stream()
                                .map(member -> new SimpleMemberDTO(
                                        member.getId(),
                                        member.getName()
                                ))
                                .toList()
                ))
                .toList();
    }

    public MarathonDTO getMarathonById(Long id) {
        return marathonRepository.findById(id)
                .map(marathon -> new MarathonDTO(
                        marathon.getId(),
                        marathon.getName(),
                        marathon.getWeight(),
                        marathon.getScore(),
                        marathon.getMembers().stream()
                                .map(member -> new SimpleMemberDTO(
                                        member.getId(),
                                        member.getName()
                                ))
                                .toList()
                ))
                .orElse(null);
    }

    public MarathonDTO createMarathonWithoutMembers(MarathonDTO marathonDTO) {
        Marathon marathon = Marathon.builder()
                .name(marathonDTO.getName())
                .weight(marathonDTO.getWeight())
                .score(marathonDTO.getScore())
                .build();
        Marathon savedMarathon = marathonRepository.save(marathon);
        return new MarathonDTO(
                savedMarathon.getId(),
                savedMarathon.getName(),
                savedMarathon.getWeight(),
                savedMarathon.getScore(),
                null
        );
    }


    public MarathonDTO updateMarathon(Long id, MarathonDTO marathonDTO) {
        return marathonRepository.findById(id)
                .map(marathon -> {
                    marathon.setName(marathonDTO.getName());
                    marathon.setWeight(marathonDTO.getWeight());
                    marathon.setScore(marathonDTO.getScore());
                    Marathon updatedMarathon = marathonRepository.save(marathon);
                    return new MarathonDTO(
                            updatedMarathon.getId(),
                            updatedMarathon.getName(),
                            updatedMarathon.getWeight(),
                            updatedMarathon.getScore(),
                            updatedMarathon.getMembers().stream()
                                    .map(member -> new SimpleMemberDTO(
                                            member.getId(),
                                            member.getName()
                                    ))
                                    .toList()
                    );
                })
                .orElse(null);
    }

    public MarathonDTO addMembersToMarathon(Long marathonId, List<Long> memberIds) {
        return marathonRepository.findById(marathonId)
                .map(marathon -> {
                    List<Member> members = memberRepository.findAllById(memberIds);
                    marathon.getMembers().addAll(members);
                    Marathon updatedMarathon = marathonRepository.save(marathon);
                    return new MarathonDTO(
                            updatedMarathon.getId(),
                            updatedMarathon.getName(),
                            updatedMarathon.getWeight(),
                            updatedMarathon.getScore(),
                            updatedMarathon.getMembers().stream()
                                    .map(member -> new SimpleMemberDTO(
                                            member.getId(),
                                            member.getName()
                                    ))
                                    .toList()
                    );
                })
                .orElse(null);
    }

    public void deleteMarathon(Long id) {
        marathonRepository.deleteById(id);
    }
}