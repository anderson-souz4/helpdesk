package com.asouza.helpdesk.repositories;

import com.asouza.helpdesk.domain.entities.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{
}
