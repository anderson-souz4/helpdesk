package com.asouza.helpdesk.repositories;

import com.asouza.helpdesk.domain.entities.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>{
}
