package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		int contRepetidos[] = new int[laudo.size()];

		int somaCasein[] = new int[laudo.size()];
		int somaCbt[] = new int[laudo.size()];
		int somaCcs[] = new int[laudo.size()];
		int somaCel[] = new int[laudo.size()];
		int somaCmt[] = new int[laudo.size()];

		int somaDen[] = new int[laudo.size()];
		int somaFat[] = new int[laudo.size()];
		int somaFpd[] = new int[laudo.size()];
		int somaPh[] = new int[laudo.size()];
		int somaRant[] = new int[laudo.size()];

		int somaSnf[] = new int[laudo.size()];
		int somaSolids[] = new int[laudo.size()];
		int somaTotpro[] = new int[laudo.size()];
		int somaTrupro[] = new int[laudo.size()];
		int somaUrea[] = new int[laudo.size()];

		int media[] = new int[laudo.size()];

		String regex = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		// compiling regex
		Pattern p = Pattern.compile(regex);

		for (int i = 0; i < laudo.size(); i++) {
			for (int j = 0; j < laudo.size(); j++) {
				if (laudo.get(i).getSequence() == laudo.get(j).getSequence()) {
					contRepetidos[i]++;
				}
			}
			System.out
					.println("\n Repeticões numero " + laudo.get(i).getSequence() + ": " + contRepetidos[i] + " vezes");
		}

		for (int y = 0; y < contRepetidos.length; y++) {
			for (int z = 0; z < contRepetidos.length; z++) {
				if (contRepetidos[y] == contRepetidos[z] && laudo.get(y).getSequence() == laudo.get(z).getSequence()) {

					Matcher m1 = p.matcher(laudo.get(z).getCasein() != null ? laudo.get(z).getCasein() : "0");
					Matcher m2 = p.matcher(laudo.get(z).getCbt() != null ? laudo.get(z).getCbt() : "0");
					Matcher m3 = p.matcher(laudo.get(z).getCcs() != null ? laudo.get(z).getCcs() : "0");
					Matcher m4 = p.matcher(laudo.get(z).getCel() != null ? laudo.get(z).getCel() : "0");
					Matcher m5 = p.matcher(laudo.get(z).getCmt() != null ? laudo.get(z).getCmt() : "0");

					Matcher m6 = p.matcher(laudo.get(z).getDen() != null ? laudo.get(z).getDen() : "0");
					Matcher m7 = p.matcher(laudo.get(z).getFat() != null ? laudo.get(z).getFat() : "0");
					Matcher m8 = p.matcher(laudo.get(z).getFpd() != null ? laudo.get(z).getFpd() : "0");
					Matcher m9 = p.matcher(laudo.get(z).getPh() != null ? laudo.get(z).getPh() : "0");
					Matcher m10 = p.matcher(laudo.get(z).getRant() != null ? laudo.get(z).getRant() : "0");

					Matcher m11 = p.matcher(laudo.get(z).getSnf() != null ? laudo.get(z).getSnf() : "0");
					Matcher m12 = p.matcher(laudo.get(z).getSolids() != null ? laudo.get(z).getSolids() : "0");
					Matcher m13 = p.matcher(laudo.get(z).getTotpro() != null ? laudo.get(z).getTotpro() : "0");
					Matcher m14 = p.matcher(laudo.get(z).getTrupro() != null ? laudo.get(z).getTrupro() : "0");
					Matcher m15 = p.matcher(laudo.get(z).getUrea() != null ? laudo.get(z).getUrea() : "0");

					somaCasein[y] += (m1.find() && m1.group().equals(laudo.get(z).getCasein()))
							? Double.parseDouble(laudo.get(z).getCasein())
							: 0;
					somaCbt[y] += (m2.find() && m2.group().equals(laudo.get(z).getCbt()))
							? Double.parseDouble(laudo.get(z).getCbt())
							: 0;
					somaCcs[y] += (m3.find() && m3.group().equals(laudo.get(z).getCcs()))
							? Double.parseDouble(laudo.get(z).getCcs())
							: 0;
					somaCel[y] += (m4.find() && m4.group().equals(laudo.get(z).getCel()))
							? Double.parseDouble(laudo.get(z).getCel())
							: 0;
					somaCmt[y] += (m5.find() && m5.group().equals(laudo.get(z).getCmt()))
							? Double.parseDouble(laudo.get(z).getCmt())
							: 0;

					somaDen[y] += (m6.find() && m6.group().equals(laudo.get(z).getDen()))
							? Double.parseDouble(laudo.get(z).getDen())
							: 0;
					somaFat[y] += (m7.find() && m7.group().equals(laudo.get(z).getFat()))
							? Double.parseDouble(laudo.get(z).getFat())
							: 0;
					somaFpd[y] += (m8.find() && m8.group().equals(laudo.get(z).getFpd()))
							? Double.parseDouble(laudo.get(z).getFpd())
							: 0;
					somaPh[y] += (m9.find() && m9.group().equals(laudo.get(z).getPh()))
							? Double.parseDouble(laudo.get(z).getPh())
							: 0;
					somaRant[y] += (m10.find() && m10.group().equals(laudo.get(z).getRant()))
							? Double.parseDouble(laudo.get(z).getRant())
							: 0;

					somaSnf[y] += (m11.find() && m11.group().equals(laudo.get(z).getSnf()))
							? Double.parseDouble(laudo.get(z).getSnf())
							: 0;
					somaSolids[y] += (m12.find() && m12.group().equals(laudo.get(z).getSolids()))
							? Double.parseDouble(laudo.get(z).getSolids())
							: 0;
					somaTotpro[y] += (m13.find() && m13.group().equals(laudo.get(z).getTotpro()))
							? Double.parseDouble(laudo.get(z).getTotpro())
							: 0;
					somaTrupro[y] += (m14.find() && m14.group().equals(laudo.get(z).getTrupro()))
							? Double.parseDouble(laudo.get(z).getTrupro())
							: 0;
					somaUrea[y] += (m15.find() && m15.group().equals(laudo.get(z).getUrea()))
							? Double.parseDouble(laudo.get(z).getUrea())
							: 0;

				}
			}

			somaCasein[y] /= contRepetidos[y];
			somaCbt[y] /= contRepetidos[y];
			somaCcs[y] /= contRepetidos[y];
			somaCel[y] /= contRepetidos[y];
			somaCmt[y] /= contRepetidos[y];

			somaDen[y] /= contRepetidos[y];
			somaFat[y] /= contRepetidos[y];
			somaFpd[y] /= contRepetidos[y];
			somaPh[y] /= contRepetidos[y];
			somaRant[y] /= contRepetidos[y];

			somaSnf[y] /= contRepetidos[y];
			somaSolids[y] /= contRepetidos[y];
			somaTotpro[y] /= contRepetidos[y];
			somaTrupro[y] /= contRepetidos[y];
			somaUrea[y] /= contRepetidos[y];

			System.out.println("\nMedia dos Valores: " + somaCasein[y]);
		}

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