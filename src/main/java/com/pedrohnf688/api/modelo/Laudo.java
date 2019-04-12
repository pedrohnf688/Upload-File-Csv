package com.pedrohnf688.api.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Laudo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("BatchId")
	private String batchId;

	@JsonProperty("Sequence")
	private String sequence;

	@JsonProperty("Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="GMT-3")
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

	private float ccs;

	private float cel;

	private float ph;

	private float den;

	private float rant;

	private float cbt;

	private float cmt;

	public Laudo() {
	}

	public Laudo(Long id, String batchId, String sequence, String date, String sampleid, float fat, float trupro,
			float totpro, float casein, float solids, float snf, float fpd, float urea, float ccs, float cel, float ph,
			float den, float rant, float cbt, float cmt) {
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
		this.ccs = ccs;
		this.cel = cel;
		this.ph = ph;
		this.den = den;
		this.rant = rant;
		this.cbt = cbt;
		this.cmt = cmt;
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

	public float getCcs() {
		return ccs;
	}

	public void setCcs(float ccs) {
		this.ccs = ccs;
	}

	public float getCel() {
		return cel;
	}

	public void setCel(float cel) {
		this.cel = cel;
	}

	public float getPh() {
		return ph;
	}

	public void setPh(float ph) {
		this.ph = ph;
	}

	public float getDen() {
		return den;
	}

	public void setDen(float den) {
		this.den = den;
	}

	public float getRant() {
		return rant;
	}

	public void setRant(float rant) {
		this.rant = rant;
	}

	public float getCbt() {
		return cbt;
	}

	public void setCbt(float cbt) {
		this.cbt = cbt;
	}

	public float getCmt() {
		return cmt;
	}

	public void setCmt(float cmt) {
		this.cmt = cmt;
	}

	@Override
	public String toString() {
		return "Laudo [id=" + id + ", batchId=" + batchId + ", sequence=" + sequence + ", date=" + date + ", sampleid="
				+ sampleid + ", fat=" + fat + ", trupro=" + trupro + ", totpro=" + totpro + ", casein=" + casein
				+ ", solids=" + solids + ", snf=" + snf + ", fpd=" + fpd + ", urea=" + urea + ", ccs=" + ccs + ", cel="
				+ cel + ", ph=" + ph + ", den=" + den + ", rant=" + rant + ", cbt=" + cbt + ", cmt=" + cmt + "]";
	}

}
