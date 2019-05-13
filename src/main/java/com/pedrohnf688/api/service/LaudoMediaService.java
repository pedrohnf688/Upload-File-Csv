package com.pedrohnf688.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.modelo.LaudoMedia;
import com.pedrohnf688.api.repositorio.LaudoMediaRepositorio;

@Service
public class LaudoMediaService {

	private static final Logger log = LoggerFactory.getLogger(LaudoMediaService.class);

	@Autowired
	private LaudoMediaRepositorio laudoMediaRepositorio;

	public LaudoMedia salvar(LaudoMedia l) {
		log.info("Salvando a Media do Laudo");
		return this.laudoMediaRepositorio.save(l);
	}

	public List<LaudoMedia> listarTodasMedias() {
		return this.laudoMediaRepositorio.findAll();
	}
	
	public List<LaudoMedia> AtualizarMediaLaudo() {
		return null;
	}

}
