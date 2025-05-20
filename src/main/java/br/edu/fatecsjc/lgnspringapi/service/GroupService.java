package br.edu.fatecsjc.lgnspringapi.service;

import br.edu.fatecsjc.lgnspringapi.converter.GroupConverter;
import br.edu.fatecsjc.lgnspringapi.dto.GroupDTO;
import br.edu.fatecsjc.lgnspringapi.entity.Group;
import br.edu.fatecsjc.lgnspringapi.repository.GroupRepository;
import br.edu.fatecsjc.lgnspringapi.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupConverter groupConverter;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository, GroupConverter groupConverter) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupConverter = groupConverter;
    }

    public List<GroupDTO> getAll() {
        return groupConverter.convertToDto(groupRepository.findAll());
    }

    public GroupDTO findById(Long id) {
        return groupRepository.findById(id)
                .map(groupConverter::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));
    }
    @Transactional
    public GroupDTO save(Long id, GroupDTO dto) {
        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));
        memberRepository.deleteMembersByGroup(entity);
        entity.getMembers().clear();

        Group groupToSaved = groupConverter.convertToEntity(dto, entity);
        groupToSaved.getMembers().forEach( member -> member.setGroup(groupToSaved));
        Group groupReturned = groupRepository.save(groupToSaved);
        return groupConverter.convertToDto(groupReturned);
    }

    public GroupDTO save(GroupDTO dto) {
        Group groupToSaved = groupConverter.convertToEntity(dto);
        Group groupReturned = groupRepository.save(groupToSaved);
        return groupConverter.convertToDto(groupReturned);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }
}
