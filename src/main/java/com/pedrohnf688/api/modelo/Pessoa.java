package com.pedrohnf688.api.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pessoa {

	@Id
	private String nome;
	private int idade;
	private String email;
	
	public Pessoa() {
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
