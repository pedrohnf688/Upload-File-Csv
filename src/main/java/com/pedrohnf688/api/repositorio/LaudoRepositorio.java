package com.pedrohnf688.api.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedrohnf688.api.modelo.Laudo;

public interface LaudoRepositorio extends JpaRepository<Laudo, Long> {

	List<Laudo> findByBatchId(String batchId);

}
