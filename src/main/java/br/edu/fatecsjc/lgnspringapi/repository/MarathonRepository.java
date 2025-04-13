package br.edu.fatecsjc.lgnspringapi.repository;

import br.edu.fatecsjc.lgnspringapi.entity.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarathonRepository extends JpaRepository<Marathon, Long> {
}
