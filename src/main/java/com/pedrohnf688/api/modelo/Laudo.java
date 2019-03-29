package com.pedrohnf688.api.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Laudo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@JsonProperty("BatchId")
	private String batchId;
	
	@JsonProperty("Sequence")
	private String sequence;
	
	@JsonProperty("Date")
	private String date;
	
	@JsonProperty("SampleID")
	private String sampleid;
	
	@JsonProperty("Fat")
	private float fat;
	
	@JsonProperty("Tru.Pro.")
	private float trupro;
	
	@JsonProperty("Tot.Pro.")
	private float totpro;
	
	@JsonProperty("Casein")
	private float casein;
	
	@JsonProperty("Solids")
	private float solids;
	
	@JsonProperty("SNF")
	private float snf;
	
	@JsonProperty("FPD")
	private float fpd;
	
	@JsonProperty("Urea")
	private float urea;

	
	public Laudo() {
	}


	public Laudo(Long id, String batchId, String sequence, String date, String sampleid, float fat, float trupro,
			float totpro, float casein, float solids, float snf, float fpd, float urea) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.sequence = sequence;
		this.date = date;
		this.sampleid = sampleid;
		this.fat = fat;
		this.trupro = trupro;
		this.totpro = totpro;
		this.casein = casein;
		this.solids = solids;
		this.snf = snf;
		this.fpd = fpd;
		this.urea = urea;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}


	public String getSequence() {
		return sequence;
	}


	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getSampleid() {
		return sampleid;
	}


	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}


	public float getFat() {
		return fat;
	}


	public void setFat(float fat) {
		this.fat = fat;
	}


	public float getTrupro() {
		return trupro;
	}


	public void setTrupro(float trupro) {
		this.trupro = trupro;
	}


	public float getTotpro() {
		return totpro;
	}


	public void setTotpro(float totpro) {
		this.totpro = totpro;
	}


	public float getCasein() {
		return casein;
	}


	public void setCasein(float casein) {
		this.casein = casein;
	}


	public float getSolids() {
		return solids;
	}


	public void setSolids(float solids) {
		this.solids = solids;
	}


	public float getSnf() {
		return snf;
	}


	public void setSnf(float snf) {
		this.snf = snf;
	}


	public float getFpd() {
		return fpd;
	}


	public void setFpd(float fpd) {
		this.fpd = fpd;
	}


	public float getUrea() {
		return urea;
	}


	public void setUrea(float urea) {
		this.urea = urea;
	}
	
	

}
