package com.asouza.helpdesk.services;

import com.asouza.helpdesk.domain.dtos.ClienteDTO;
import com.asouza.helpdesk.domain.dtos.TecnicoDTO;
import com.asouza.helpdesk.domain.entities.Cliente;
import com.asouza.helpdesk.domain.entities.Pessoa;
import com.asouza.helpdesk.domain.entities.Tecnico;
import com.asouza.helpdesk.repositories.ClienteRepository;
import com.asouza.helpdesk.repositories.PessoaRepository;
import com.asouza.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.asouza.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private ClienteRepository repository;
    private PessoaRepository pessoaRepository;

    @Autowired
    public ClienteService(ClienteRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public Cliente findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Pessoa não encontrado"));
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null);
        validaPorCpfEEmail(objDTO);
        Cliente newObj = new Cliente(objDTO);
        return repository.save(newObj);


    }

    public Cliente update(Integer id, ClienteDTO objDTO) {
        objDTO.setId(id);
        Cliente oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Cliente(objDTO);
        return repository.save(oldObj);
    }

    private void validaPorCpfEEmail(ClienteDTO objDTO){
        Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF já cadastrado no sistema");
        }

        obj = pessoaRepository.findByEmail(objDTO.getEmail());
        if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("Email já cadastrado no sistema");
        }

    }


    public void delete(Integer id) {
        Cliente obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço, não pode ser deletado");
        }
        repository.deleteById(id);
    }
}
