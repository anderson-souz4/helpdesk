package com.asouza.helpdesk.services;

import com.asouza.helpdesk.domain.TecnicoDTO;
import com.asouza.helpdesk.domain.entities.Pessoa;
import com.asouza.helpdesk.domain.entities.Tecnico;
import com.asouza.helpdesk.repositories.PessoaRepository;
import com.asouza.helpdesk.repositories.TecnicoRepository;
import com.asouza.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.asouza.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    private TecnicoRepository repository;
    private PessoaRepository pessoaRepository;

    @Autowired
    public TecnicoService(TecnicoRepository repository, PessoaRepository pessoaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
    }

    public Tecnico findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Técnico não encontrado"));
    }

    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    public Tecnico create(TecnicoDTO objDTO) {
        objDTO.setId(null);
        validaPorCpfEEmail(objDTO);
        Tecnico newObj = new Tecnico(objDTO);
        return repository.save(newObj);


    }

    public Tecnico update(Integer id, TecnicoDTO objDTO) {
        objDTO.setId(id);
        Tecnico oldObj = findById(id);
        validaPorCpfEEmail(objDTO);
        oldObj = new Tecnico(objDTO);
        return repository.save(oldObj);
    }

    private void validaPorCpfEEmail(TecnicoDTO objDTO){
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
        Tecnico obj = findById(id);
        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Técnico possui ordens de serviço, não pode ser deletado");
        }
        repository.deleteById(id);
    }
}
