package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.pedrohnf688.api.modelo.LaudoMedia;
import com.pedrohnf688.api.repositorio.LaudoMediaRepositorio;
import com.pedrohnf688.api.repositorio.LaudoRepositorio;
import com.pedrohnf688.api.service.LaudoMediaService;
import com.pedrohnf688.api.service.LaudoService;
import com.pedrohnf688.api.utils.CsvUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@RestController
@RequestMapping("/laudo")
@CrossOrigin(origins = "*")
public class LaudoController {

	private static final Logger log = LoggerFactory.getLogger(LaudoController.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LaudoService laudoService;

	@Autowired
	private LaudoRepositorio laudoRepositorio;

	@Autowired
	private LaudoMediaRepositorio laudoMediaRepositorio;

	@Autowired
	private LaudoMediaService laudoMediaService;

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

	public List<Laudo> PreenchimentodeDados(){
		List<Laudo> listaLaudo = laudoService.listarLaudos();
		
		for (int i = 0; i < listaLaudo.size(); i++) {
			
		}
		return null;
	}
	
	
	@PostMapping
	public ResponseEntity<Response<Laudo>> cadastrarLaudo(@Valid @RequestBody Laudo laudo, BindingResult result)
			throws NoSuchAlgorithmException {
		log.info("Cadastrando Laudo:{}", laudo.toString());

		Response<Laudo> response = new Response<Laudo>();

		validarDadosExistentes(laudo, result);

		if (result.hasErrors()) {
			log.error("Erro Validando Dados do Cadastro do Laudo: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.laudoService.salvar(laudo);
		response.setData(laudo);

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<Response<Laudo>> buscarLaudoPorId(@PathVariable("id") Integer id) {
		log.info("Buscar Laudo por Id");

		Response<Laudo> response = new Response<Laudo>();

		Optional<Laudo> laudo = this.laudoService.buscarPorId(id);

		if (!laudo.isPresent()) {
			log.info("Laudo não encontrado");
			response.getErros().add("Laudo não encontrado");
			ResponseEntity.badRequest().body(response);
		}

		response.setData(laudo.get());

		return ResponseEntity.ok(response);

	}

	@GetMapping(value = "/batch")
	public List<Laudo> buscarLaudoPorBatchId(@RequestParam("batchId") String batchId) {
		log.info("Buscar Laudo por BatchId");

		List<Laudo> laudos = this.laudoService.buscarPorBatchId(batchId);
		return laudos;
	}

	@GetMapping(value = "/dataSolicitada")
	public List<Laudo> buscarLaudoPorData(@RequestParam("dataSolicitada") String dataSolicitada) throws ParseException {
		log.info("Buscar Laudo por BatchId");

		List<Laudo> laudos = this.laudoService.buscarPorData(dataSolicitada);
		return laudos;
	}

	@PutMapping(value = "{id}")
	public ResponseEntity<Response<Laudo>> atualizarLaudo(@PathVariable("id") Integer id,
			@Valid @RequestBody Laudo laudo, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando o Laudo:{}", laudo.toString());

		Response<Laudo> response = new Response<Laudo>();

		Optional<Laudo> laudoId = this.laudoService.buscarPorId(id);

		if (!laudoId.isPresent()) {
			log.info("Laudo não encontrado");
			response.getErros().add("Laudo não encontrado");
			ResponseEntity.badRequest().body(response);
		}

		this.atualizarDadosLaudo(laudoId.get(), laudo, result);

		if (result.hasErrors()) {
			log.error("Erro validando Laudo:{}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.laudoService.salvar(laudoId.get());
		response.setData(laudoId.get());

		return ResponseEntity.ok(response);

	}

	@DeleteMapping
	public void deletarTodoLaudo() {
		this.laudoService.deletarTodoLaudo();
	}

	@DeleteMapping(value = "{id}")
	public ResponseEntity<Response<Laudo>> deletarCliente(@PathVariable("id") Integer id) {
		log.info("Removendo Laudo por Id: {}", id);

		Response<Laudo> response = new Response<Laudo>();

		Optional<Laudo> laudo = this.laudoService.buscarPorId(id);

		if (!laudo.isPresent()) {
			log.info("Laudo não encontrado");
			response.getErros().add("Laudo não encontrado");
			ResponseEntity.badRequest().body(response);
		}

		response.setData(laudo.get());

		this.laudoService.deletaLaudoPorId(id);

		return ResponseEntity.ok(response);
	}

	// Falta fazer o metodo pra filtrar os dados do laudo

	private void atualizarDadosLaudo(Laudo laudoId, Laudo laudo, BindingResult result) throws NoSuchAlgorithmException {

		laudoId.setCbt(laudo.getCbt());
		laudoId.setCcs(laudo.getCcs());
		laudoId.setCel(laudo.getCel());
		laudoId.setCmt(laudo.getCmt());
		laudoId.setDen(laudo.getDen());
		laudoId.setPh(laudo.getPh());
		laudoId.setRant(laudo.getRant());

		laudoId = laudo;

	}

	private void validarDadosExistentes(Laudo l, BindingResult result) {

	}

}