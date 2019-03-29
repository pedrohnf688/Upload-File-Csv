package com.pedrohnf688.api.repositorio;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedrohnf688.api.modelo.Laudo;
import com.pedrohnf688.api.modelo.Pessoa;

public interface PessoaRepositorio extends JpaRepository<Laudo, String>{

}
