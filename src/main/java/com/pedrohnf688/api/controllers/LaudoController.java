package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@GetMapping(value = "/filtro")
	public ResponseEntity<Response<List<Laudo>>> filtrarDadosLaudo(@RequestParam("batchId") String batchId)
			throws NoSuchAlgorithmException {
		log.info("Atualizando o Laudo do Batch:{}", batchId);

		Response<List<Laudo>> response = new Response<List<Laudo>>();

		List<Laudo> laudoNovo = new ArrayList<Laudo>();
		List<Laudo> laudo = this.laudoService.buscarPorBatchId(batchId);

		this.atualizarFiltraDadosLaudo(laudo, laudoNovo);

//		this.laudoService.salvar(laudoId.get());
		response.setData(laudoNovo);

		return ResponseEntity.ok(response);

	}

	private void atualizarFiltraDadosLaudo(List<Laudo> laudo, List<Laudo> laudoNovo) {

		Laudo la = new Laudo();
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

		String regex = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		// compiling regex
		Pattern p = Pattern.compile(regex);

		for (int i = 0; i < laudo.size(); i++) {
			for (int j = 0; j < laudo.size(); j++) {
				if (laudo.get(i).getSequence().equals(laudo.get(j).getSequence())) {
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

			System.out.println("Vetor de Casein Soma:" + somaCasein[y] + "\nCont:" + contRepetidos[y]);

		}

		for (int s = 0; s < contRepetidos.length; s++) {

			somaCasein[s] /= contRepetidos[s];
			somaCbt[s] /= contRepetidos[s];
			somaCcs[s] /= contRepetidos[s];
			somaCel[s] /= contRepetidos[s];
			somaCmt[s] /= contRepetidos[s];

			somaDen[s] /= contRepetidos[s];
			somaFat[s] /= contRepetidos[s];
			somaFpd[s] /= contRepetidos[s];
			somaPh[s] /= contRepetidos[s];
			somaRant[s] /= contRepetidos[s];

			somaSnf[s] /= contRepetidos[s];
			somaSolids[s] /= contRepetidos[s];
			somaTotpro[s] /= contRepetidos[s];
			somaTrupro[s] /= contRepetidos[s];
			somaUrea[s] /= contRepetidos[s];

			System.out.println("Media Casein:" + somaCasein[s] + "Contador" + contRepetidos[s]);

		}

		for (int a = 0; a < laudo.size(); a++) {
			for (int b = 0; b < laudo.size(); b++) {

				if (laudo.get(a).getSequence() == laudo.get(b).getSequence() && a > 1) {

					la.setBatchId(laudo.get(a).getBatchId());
					la.setCasein(String.valueOf(somaCasein[a - 1]));
					la.setCbt(String.valueOf(somaCbt[a - 1]));
					la.setCcs(String.valueOf(somaCcs[a - 1]));
					la.setCel(String.valueOf(somaCel[a - 1]));
					la.setCmt(String.valueOf(somaCmt[a - 1]));
					la.setDate(laudo.get(a).getDate());
					la.setDen(String.valueOf(somaDen[a - 1]));
					la.setFat(String.valueOf(somaFat[a - 1]));
					la.setFpd(String.valueOf(somaFpd[a - 1]));
					la.setPh(String.valueOf(somaPh[a - 1]));
					la.setRant(String.valueOf(somaRant[a - 1]));
					la.setSampleid(laudo.get(a).getSampleid());
					la.setSequence(laudo.get(a).getSequence());
					la.setSnf(String.valueOf(somaSnf[a - 1]));
					la.setSolids(String.valueOf(somaSolids[a - 1]));
					la.setTotpro(String.valueOf(somaTotpro[a - 1]));
					la.setTrupro(String.valueOf(somaTrupro[a - 1]));
					la.setUrea(String.valueOf(somaUrea[a - 1]));

				} else {

					la.setBatchId(laudo.get(a).getBatchId());
					la.setCasein(String.valueOf(somaCasein[a]));
					la.setCbt(String.valueOf(somaCbt[a]));
					la.setCcs(String.valueOf(somaCcs[a]));
					la.setCel(String.valueOf(somaCel[a]));
					la.setCmt(String.valueOf(somaCmt[a]));
					la.setDate(laudo.get(a).getDate());
					la.setDen(String.valueOf(somaDen[a]));
					la.setFat(String.valueOf(somaFat[a]));
					la.setFpd(String.valueOf(somaFpd[a]));
					la.setPh(String.valueOf(somaPh[a]));
					la.setRant(String.valueOf(somaRant[a]));
					la.setSampleid(laudo.get(a).getSampleid());
					la.setSequence(laudo.get(a).getSequence());
					la.setSnf(String.valueOf(somaSnf[a]));
					la.setSolids(String.valueOf(somaSolids[a]));
					la.setTotpro(String.valueOf(somaTotpro[a]));
					la.setTrupro(String.valueOf(somaTrupro[a]));
					la.setUrea(String.valueOf(somaUrea[a]));

				}

				laudoNovo.add(la);
			}

			System.out.println(laudoNovo.get(a));

		}

	}

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