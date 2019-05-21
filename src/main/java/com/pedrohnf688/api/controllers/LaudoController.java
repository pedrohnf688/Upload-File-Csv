package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.pedrohnf688.api.repositorio.LaudoMediaRepositorio;
import com.pedrohnf688.api.repositorio.LaudoRepositorio;
import com.pedrohnf688.api.service.LaudoMediaService;
import com.pedrohnf688.api.service.LaudoService;
import com.pedrohnf688.api.utils.CsvUtils;

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

//	public List<Laudo> PreenchimentodeDados(){
//		List<Laudo> listaLaudo = laudoService.listarLaudos();
//		
//		for (int i = 0; i < listaLaudo.size(); i++) {
//			
//		}
//		return null;
//	}

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

	@PutMapping(value = "/filtro")
	public ResponseEntity<Response<List<Laudo>>> filtrarDadosLaudo(BindingResult result)
			throws NoSuchAlgorithmException {
//		log.info("Atualizando o Laudo:{}", laudo.toString());

		Response<List<Laudo>> response = new Response<List<Laudo>>();

		List<Laudo> laudoNovo = new ArrayList<Laudo>();
		List<Laudo> laudo = this.laudoService.listarLaudos();

		this.atualizarFiltraDadosLaudo(laudo, laudoNovo, result);

		if (result.hasErrors()) {
			log.error("Erro validando Laudo:{}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

//		this.laudoService.salvar(laudoId.get());
//		response.setData(laudoId.get());

		return ResponseEntity.ok(response);

	}

	private void atualizarFiltraDadosLaudo(List<Laudo> laudo, List<Laudo> laudoNovo, BindingResult result) {
		// TODO Auto-generated method stub

	}

//	public static void main(String[] args) {
//
//		List<Integer> x = new ArrayList<Integer>();
//		x.add(1);
//		x.add(1);
//		x.add(2);
//		x.add(5);
//		x.add(3);
//		x.add(2);
//		x.add(9);
//		x.add(6);
//		x.add(3);
//		x.add(3);
//
//		// numero 1:2 ==> 2
//		// numero 2:2 ==> 4
//		// numero 5:1 ==> 5
//		// numero 3:3 ==> 9
//		// numero 6:1 ==> 6
//
//		int contRepetidos[] = new int[10];
//		int soma[] = new int[10];
//		int media[] = new int[10];
//
//		for (int i = 0; i < x.size(); i++) {
//			for (int j = 0; j < x.size(); j++) {
//				if (x.get(i) == x.get(j)) {
//					contRepetidos[i]++;
//				}
//			}
//			System.out.println("\n Repeticões numero " + x.get(i) + ": " + contRepetidos[i] + " vezes"
//					+ " Soma dos Valores: " + soma[i]);
//		}
//
//		for (int y = 0; y < contRepetidos.length; y++) {
//			for (int z = 0; z < contRepetidos.length; z++) {
//				if (contRepetidos[y] == contRepetidos[z] && x.get(y) == x.get(z)) {
//					soma[y] += x.get(z);
//				}
//			}
//
//			soma[y] /= contRepetidos[y];
//			System.out.println("\nMedia dos Valores: " + soma[y]);
//		}
//
////		for (int i = 0; i < 10; i++) {
////			System.out.println("\nMedia: " + soma[i]);
////		}
//
//	}
//
////	  public static void main( String[ ] args ) {
////	        int[ ] original = { 1 , 8 , 5 , 7 , 3 , 5 , 3 };
////
////	        // remover repetidos
////	        int[ ] unicos = new int[ original.length ];
////	        int qtd = 0;
////	        for( int i = 0 ; i < original.length ; i++ ) {
////	            boolean existe = false;
////	            for( int j = 0 ; j < qtd ; j++ ) {
////	                if( unicos[ j ] == original[ i ] ) {
////	                    existe = true;
////	                    break;
////	                }
////	            }
////	            if( !existe ) {
////	                unicos[ qtd++ ] = original[ i ];
////	            }
////	        }
////
////	        // ajuste do tamanho do vetor resultante
////	        unicos = Arrays.copyOf( unicos , qtd );
////
////	        // imprime resultado
////	        for( int i = 0 ; i < unicos.length ; i++ ) {
////	            System.out.println( "" + i + " = " + unicos[ i ] );
////	        }
////
////	    }

}