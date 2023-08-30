package com.asouza.helpdesk.repositories;

import com.asouza.helpdesk.domain.entities.Chamado;
import com.asouza.helpdesk.domain.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
}
