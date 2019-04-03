package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pedrohnf688.api.config.Response;
import com.pedrohnf688.api.modelo.Laudo;
import com.pedrohnf688.api.repositorio.LaudoRepositorio;
import com.pedrohnf688.api.service.LaudoService;
import com.pedrohnf688.api.utils.CsvUtils;

@RestController
@RequestMapping("/laudo")
@CrossOrigin(origins = "*")
public class LaudoController {

	private static final Logger log = LoggerFactory.getLogger(LaudoController.class);

	@Autowired
	private LaudoService laudoService;

	@Autowired
	private LaudoRepositorio laudoRepositorio;

	@GetMapping
	public List<Laudo> listarLaudos() {
		List<Laudo> laudos = this.laudoService.listarLaudos();
		return laudos;
	}

//	@PostMapping(value = "/upload", consumes = "text/csv")
//	public void uploadSimple(@RequestBody InputStream body) throws IOException {
//		laudoRepositorio.saveAll(CsvUtils.read(Laudo.class, body));
//	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public void uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {

		log.info("Fazendo Upload do Arquivo Csv do Laudo");

		try {
			laudoRepositorio.saveAll(CsvUtils.read(Laudo.class, file.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping(value = "{id}")
	public ResponseEntity<Response<Laudo>> buscarClientePorId(@PathVariable("id") Long id) {

		log.info("Buscar Laudo por Id");

		Response<Laudo> response = new Response<Laudo>();

		Laudo laudo = this.laudoService.buscarPorId(id);

		response.setData(Optional.ofNullable(laudo));

		verificarResposta(response);

		return ResponseEntity.ok(response);
	}

	@PutMapping(value = "{id}")
	public ResponseEntity<Response<Laudo>> atualizarLaudo(@PathVariable("id") Long id, @Valid @RequestBody Laudo laudo,
			BindingResult result) throws NoSuchAlgorithmException {

		log.info("Atualizando o Laudo:{}", laudo.toString());

		Response<Laudo> response = new Response<Laudo>();

		Laudo laudoId = this.laudoService.buscarPorId(id);

		response.setData(Optional.ofNullable(laudoId));

		verificarResposta(response);

		this.atualizarDadosLaudo(laudoId, laudo, result);

		if (result.hasErrors()) {
			log.error("Falta Implementar. Erro validando Laudo:{}", result.getAllErrors());

			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));

			return ResponseEntity.badRequest().body(response);
		}

		this.laudoService.salvar(laudoId);

		return ResponseEntity.ok(response);

	}

	private void atualizarDadosLaudo(Laudo laudoId, Laudo laudo, BindingResult result) throws NoSuchAlgorithmException {

		/*
		 * Falta implementar.
		 */

	}

	private void verificarResposta(Response<Laudo> response) {
		if (!response.getData().isPresent()) {
			log.info("Laudo não encontrado");

			response.getErros().add("Laudo não encontrado");

			ResponseEntity.badRequest().body(response);
		}
	}

}
