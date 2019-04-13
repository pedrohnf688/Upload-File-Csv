package com.pedrohnf688.api.service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

	public void deletaLaudoPorId(Long id) {
		log.info("Deletando Laudo por Id");
		this.laudoRepositorio.deleteById(id);
	}

	public List<Laudo> buscarPorBatchId(String batchId) {
		log.info("Buscando Laudo por batchId");
		List<Laudo> laudos = this.laudoRepositorio.findByBatchId(batchId);
		return laudos;
	}

	public List<Laudo> buscarPorData(String dataSolicitada) throws ParseException {
		log.info("Buscando Laudo por Data");
		List<Laudo> laudos = this.laudoRepositorio.findAllByDate(conversao(dataSolicitada));
		return laudos;
	}

	public LocalDate conversao(String dataSolicitada) throws ParseException {
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate data = LocalDate.parse(dataSolicitada, formato);
		System.out.println(data);
		return data;
	}

}
