package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pedrohnf688.api.modelo.Laudo;
import com.pedrohnf688.api.repositorio.PessoaRepositorio;
import com.pedrohnf688.api.utils.CsvUtils;

@RestController
@RequestMapping("/laudo")
public class LaudoController {

	@Autowired
	private PessoaRepositorio pessoaRepositorio;

	public LaudoController(PessoaRepositorio pessoaRepositorio) {
		this.pessoaRepositorio = pessoaRepositorio;
	}

	@GetMapping
	public List<Laudo> listarLaudos() {
		List<Laudo> laudos = this.pessoaRepositorio.findAll();
		return laudos;
	}
	
	@PostMapping(value = "/upload", consumes = "text/csv")
	public void uploadSimple(@RequestBody InputStream body) throws IOException {
		pessoaRepositorio.saveAll(CsvUtils.read(Laudo.class, body));
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public void uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
		pessoaRepositorio.saveAll(CsvUtils.read(Laudo.class, file.getInputStream()));
	}

}
