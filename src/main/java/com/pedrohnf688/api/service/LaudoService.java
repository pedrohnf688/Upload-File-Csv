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
		Optional<Laudo> objLaudo = laudoRepositorio.findById(id);
		return objLaudo.orElse(null);
	}

	public List<Laudo> listarLaudos() {
		List<Laudo> laudos = this.laudoRepositorio.findAll();
		return laudos;
	}

}
