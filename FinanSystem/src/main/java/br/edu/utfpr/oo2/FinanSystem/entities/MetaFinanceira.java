package br.edu.utfpr.oo2.FinanSystem.entities;

public class MetaFinanceira {

	private Integer id;
	private Integer usuarioId;
	private String descricao;
	private double valorMensal;
	private String tipoMeta;
	
	
	public MetaFinanceira() {
		
	}

	public MetaFinanceira(Integer id, Integer usuarioId, String descricao, double valorMensal, String tipoMeta) {
		this.id = id;
		this.usuarioId = usuarioId;
		this.descricao = descricao;
		this.valorMensal = valorMensal;
		this.tipoMeta = tipoMeta;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorMensal() {
		return valorMensal;
	}

	public void setValorMensal(double valorMensal) {
		this.valorMensal = valorMensal;
	}

	public String getTipoMeta() {
		return tipoMeta;
	}

	public void setTipoMeta(String tipoMeta) {
		this.tipoMeta = tipoMeta;
	}
	
	
}
