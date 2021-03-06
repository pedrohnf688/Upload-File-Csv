package com.pedrohnf688.api.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pedrohnf688.api.modelo.Laudo;

public interface LaudoRepositorio extends JpaRepository<Laudo, Integer> {

	List<Laudo> findByBatchId(String batchId);

	List<Laudo> findAllByDate(LocalDate date);

	Laudo findAllByBatchId(String batchId);

}
