package com.pedrohnf688.api.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.modelo.Laudo;
import com.pedrohnf688.api.repositorio.LaudoRepositorio;

@Service
public class LaudoService {

	private static final Logger log = LoggerFactory.getLogger(LaudoService.class);

	@Autowired
	private LaudoRepositorio laudoRepositorio;

	public Laudo buscarPorId(Long id) {
		log.info("Buscando Laudo por id");
		Optional<Laudo> objLaudo = laudoRepositorio.findById(id);
		return objLaudo.orElse(null);
	}

	public List<Laudo> listarLaudos() {
		log.info("Listando Laudo");
		List<Laudo> laudos = this.laudoRepositorio.findAll();
		return laudos;
	}

	public void salvar(Laudo laudo) {
		log.info("Salvando Laudo");
		this.laudoRepositorio.save(laudo);
	}

	public void deletarTodoLaudo() {
		log.info("Deletando todo o Laudo");
		this.laudoRepositorio.deleteAll();
	}
	
	public List<Laudo> buscarPorBatchId(String batchId){
		log.info("Buscando Laudo por batchId");
		List<Laudo> laudos = this.laudoRepositorio.findByBatchId(batchId);
		return laudos;
	}
	
	
	
}
