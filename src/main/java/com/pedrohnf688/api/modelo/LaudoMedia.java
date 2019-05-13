package com.pedrohnf688.api.modelo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class LaudoMedia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private double fat;
	private double trupro;
	private double totpro;
	private double casein;
	private double solids;
	private double snf;
	private double fpd;
	private double urea;
	private double ccs;
	private double cel;
	private double ph;
	private double den;
	private double rant;
	private double cbt;
	private double cmt;

	@OneToMany(orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	private List<Laudo> listaLaudos;

	public LaudoMedia() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public double getTrupro() {
		return trupro;
	}

	public void setTrupro(double trupro) {
		this.trupro = trupro;
	}

	public double getTotpro() {
		return totpro;
	}

	public void setTotpro(double totpro) {
		this.totpro = totpro;
	}

	public double getCasein() {
		return casein;
	}

	public void setCasein(double casein) {
		this.casein = casein;
	}

	public double getSolids() {
		return solids;
	}

	public void setSolids(double solids) {
		this.solids = solids;
	}

	public double getSnf() {
		return snf;
	}

	public void setSnf(double snf) {
		this.snf = snf;
	}

	public double getFpd() {
		return fpd;
	}

	public void setFpd(double fpd) {
		this.fpd = fpd;
	}

	public double getUrea() {
		return urea;
	}

	public void setUrea(double urea) {
		this.urea = urea;
	}

	public double getCcs() {
		return ccs;
	}

	public void setCcs(double ccs) {
		this.ccs = ccs;
	}

	public double getCel() {
		return cel;
	}

	public void setCel(double cel) {
		this.cel = cel;
	}

	public double getPh() {
		return ph;
	}

	public void setPh(double ph) {
		this.ph = ph;
	}

	public double getDen() {
		return den;
	}

	public void setDen(double den) {
		this.den = den;
	}

	public double getRant() {
		return rant;
	}

	public void setRant(double rant) {
		this.rant = rant;
	}

	public double getCbt() {
		return cbt;
	}

	public void setCbt(double cbt) {
		this.cbt = cbt;
	}

	public double getCmt() {
		return cmt;
	}

	public void setCmt(double cmt) {
		this.cmt = cmt;
	}

	public List<Laudo> getListaLaudos() {
		return listaLaudos;
	}

	public void setListaLaudos(List<Laudo> listaLaudos) {
		this.listaLaudos = listaLaudos;
	}

	@Override
	public String toString() {
		return "LaudoMedia [id=" + id + ", fat=" + fat + ", trupro=" + trupro + ", totpro=" + totpro + ", casein="
				+ casein + ", solids=" + solids + ", snf=" + snf + ", fpd=" + fpd + ", urea=" + urea + ", ccs=" + ccs
				+ ", cel=" + cel + ", ph=" + ph + ", den=" + den + ", rant=" + rant + ", cbt=" + cbt + ", cmt=" + cmt
				+ "]";
	}

}
