package com.pedrohnf688.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pedrohnf688.api.modelo.Laudo;

public class Response<T> {

	private Optional<T> data;
	private List<String> erros;
	private T data2;

	public Response() {
	}

	public Optional<T> getData() {
		return data;
	}

	public void setData(Optional<T> optional) {
		this.data = optional;
	}

	public T getData2() {
		return data2;
	}

	public void setData2(T data2) {
		this.data2 = data2;
	}

	public List<String> getErros() {
		if (this.erros == null) {
			this.erros = new ArrayList<String>();
		}

		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

}
