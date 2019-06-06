package com.pedrohnf688.api.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedrohnf688.api.modelo.Laudo;
import com.pedrohnf688.api.modelo.LaudoMedia;
import com.pedrohnf688.api.service.LaudoMediaService;
import com.pedrohnf688.api.service.LaudoService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@RestController
@RequestMapping("/laudoMedia")
@CrossOrigin(origins = "*")
public class LaudoMediaController {

	private static final Logger log = LoggerFactory.getLogger(LaudoMediaController.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LaudoService laudoService;

	@Autowired
	private LaudoMediaService laudoMediaService;

	@PutMapping("/batchId")
	public LaudoMedia MedidaLaudos(@RequestParam("batchId") String batchId) {
		return this.laudoMediaService.salvar(mediaAritmeticaLaudo(batchId));
	}

	public LaudoMedia mediaAritmeticaLaudo(String batchId) {
		log.info("Metodo para gerar a media dos atributos do laudo por solicitação:");

		List<Laudo> laudos = this.laudoService.buscarPorBatchId(batchId);

		String regex = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
		// compiling regex
		Pattern p = Pattern.compile(regex);

		// Creates a matcher that will match input1 against regex
		double media1 = 0;
		double media2 = 0;
		double media3 = 0;
		double media4 = 0;
		double media5 = 0;
		double media6 = 0;
		double media7 = 0;
		double media8 = 0;
		double media9 = 0;
		double media10 = 0;
		double media11 = 0;
		double media12 = 0;
		double media13 = 0;
		double media14 = 0;
		double media15 = 0;

		int cont = 0;

		for (int i = 0; i < laudos.size(); i++) {

			Matcher m1 = p.matcher(laudos.get(i).getCasein() != null ? laudos.get(i).getCasein() : "0");
			Matcher m2 = p.matcher(laudos.get(i).getCbt() != null ? laudos.get(i).getCbt() : "0");
			Matcher m3 = p.matcher(laudos.get(i).getCcs() != null ? laudos.get(i).getCcs() : "0");
			Matcher m4 = p.matcher(laudos.get(i).getCel() != null ? laudos.get(i).getCel() : "0");
			Matcher m5 = p.matcher(laudos.get(i).getCmt() != null ? laudos.get(i).getCmt() : "0");

			Matcher m6 = p.matcher(laudos.get(i).getDen() != null ? laudos.get(i).getDen() : "0");
			Matcher m7 = p.matcher(laudos.get(i).getFat() != null ? laudos.get(i).getFat() : "0");
			Matcher m8 = p.matcher(laudos.get(i).getFpd() != null ? laudos.get(i).getFpd() : "0");
			Matcher m9 = p.matcher(laudos.get(i).getPh() != null ? laudos.get(i).getPh() : "0");
			Matcher m10 = p.matcher(laudos.get(i).getRant() != null ? laudos.get(i).getRant() : "0");

			Matcher m11 = p.matcher(laudos.get(i).getSnf() != null ? laudos.get(i).getSnf() : "0");
			Matcher m12 = p.matcher(laudos.get(i).getSolids() != null ? laudos.get(i).getSolids() : "0");
			Matcher m13 = p.matcher(laudos.get(i).getTotpro() != null ? laudos.get(i).getTotpro() : "0");
			Matcher m14 = p.matcher(laudos.get(i).getTrupro() != null ? laudos.get(i).getTrupro() : "0");
			Matcher m15 = p.matcher(laudos.get(i).getUrea() != null ? laudos.get(i).getUrea() : "0");

			media1 += (m1.find() && m1.group().equals(laudos.get(i).getCasein()))
					? Double.parseDouble(laudos.get(i).getCasein())
					: 0;
			media2 += (m2.find() && m2.group().equals(laudos.get(i).getCbt()))
					? Double.parseDouble(laudos.get(i).getCbt())
					: 0;
			media3 += (m3.find() && m3.group().equals(laudos.get(i).getCcs()))
					? Double.parseDouble(laudos.get(i).getCcs())
					: 0;
			media4 += (m4.find() && m4.group().equals(laudos.get(i).getCel()))
					? Double.parseDouble(laudos.get(i).getCel())
					: 0;
			media5 += (m5.find() && m5.group().equals(laudos.get(i).getCmt()))
					? Double.parseDouble(laudos.get(i).getCmt())
					: 0;

			media6 += (m6.find() && m6.group().equals(laudos.get(i).getDen()))
					? Double.parseDouble(laudos.get(i).getDen())
					: 0;
			media7 += (m7.find() && m7.group().equals(laudos.get(i).getFat()))
					? Double.parseDouble(laudos.get(i).getFat())
					: 0;
			media8 += (m8.find() && m8.group().equals(laudos.get(i).getFpd()))
					? Double.parseDouble(laudos.get(i).getFpd())
					: 0;
			media9 += (m9.find() && m9.group().equals(laudos.get(i).getPh()))
					? Double.parseDouble(laudos.get(i).getPh())
					: 0;
			media10 += (m10.find() && m10.group().equals(laudos.get(i).getRant()))
					? Double.parseDouble(laudos.get(i).getRant())
					: 0;

			media11 += (m11.find() && m11.group().equals(laudos.get(i).getSnf()))
					? Double.parseDouble(laudos.get(i).getSnf())
					: 0;
			media12 += (m12.find() && m12.group().equals(laudos.get(i).getSolids()))
					? Double.parseDouble(laudos.get(i).getSolids())
					: 0;
			media13 += (m13.find() && m13.group().equals(laudos.get(i).getTotpro()))
					? Double.parseDouble(laudos.get(i).getTotpro())
					: 0;
			media14 += (m14.find() && m14.group().equals(laudos.get(i).getTrupro()))
					? Double.parseDouble(laudos.get(i).getTrupro())
					: 0;
			media15 += (m15.find() && m15.group().equals(laudos.get(i).getUrea()))
					? Double.parseDouble(laudos.get(i).getUrea())
					: 0;

			cont++;
		}

		media1 /= cont;
		media2 /= cont;
		media3 /= cont;
		media4 /= cont;
		media5 /= cont;

		media6 /= cont;
		media7 /= cont;
		media8 /= cont;
		media9 /= cont;
		media10 /= cont;

		media11 /= cont;
		media12 /= cont;
		media13 /= cont;
		media14 /= cont;
		media15 /= cont;

		LaudoMedia l = new LaudoMedia();

		l.setCaseinMedia(media1);
		l.setCbtMedia(media2);
		l.setCcsMedia(media3);
		l.setCelMedia(media4);
		l.setCmtMedia(media5);
		l.setDenMedia(media6);
		l.setFatMedia(media7);
		l.setFpdMedia(media8);
		l.setPhMedia(media9);
		l.setRantMedia(media10);
		l.setSnfMedia(media11);
		l.setSolidsMedia(media12);
		l.setTotproMedia(media13);
		l.setTruproMedia(media14);
		l.setUreaMedia(media15);

		l.setListaLaudos(laudos);
		this.laudoMediaService.salvar(l);

		log.info("Contador:{}", cont);

		return l;
	}

	@GetMapping(value = "relatorio/pdf/{id}")
	public void gerarPdf(@PathVariable("id") Integer id, HttpServletResponse response)
			throws JRException, SQLException, IOException {
		log.info("Gerando Relatorio do Laudo para Id: {}", id);

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("Id_LaudoMedia", id);

		// Pega o arquivo .jasper localizado em resources
		InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/report2.jasper");

		// Cria o objeto JaperReport com o Stream do arquivo jasper
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		// Passa para o JasperPrint o relatório, os parâmetros e a fonte dos dados, no
		// caso uma conexão ao banco de dados
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

		// Configura a respota para o tipo PDF
		response.setContentType("application/pdf");
		// Define que o arquivo pode ser visualizado no navegador e também nome final do
		// arquivo
		// para fazer download do relatório troque 'inline' por 'attachment'
		response.setHeader("Content-Disposition", "inline; filename=reportLaudoCliente.pdf");

		// Faz a exportação do relatório para o HttpServletResponse
		final OutputStream outStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
	}

	@GetMapping(value = "relatorio/excel/{id}")
	public void gerarExcel(@PathVariable("id") Integer id, HttpServletResponse response)
			throws JRException, SQLException, IOException {

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("Id_LaudoMedia", id);

		InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/report2.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource.getConnection());

		response.setContentType("application/x-xls");
		response.setHeader("Content-Disposition", "inline; filename=reportLaudoCliente.xls");

		final OutputStream outStream = response.getOutputStream();

		JRXlsExporter exportsXLS = new JRXlsExporter();

		exportsXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
		exportsXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));

		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();

		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		configuration.setWhitePageBackground(false);
		configuration.setRemoveEmptySpaceBetweenRows(false);

		exportsXLS.setConfiguration(configuration);

		exportsXLS.exportReport();

	}

}
