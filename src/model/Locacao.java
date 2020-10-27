package model;

public class Locacao {
	
	private int idLocacao;
	private int idEmpresa;
	private int idFunc;
	private int idVeiculo;
	private String origem;
	private String destino;
	private String dataHoraSaida;
	private String dataHoraChegada;
	private int kmSaida;
	private int kmChegada;
	
	public int getIdLocacao() {
		return idLocacao;
	}
	public void setIdLocacao(int idLocacao) {
		this.idLocacao = idLocacao;
	}
	public int getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public int getIdFunc() {
		return idFunc;
	}
	public void setIdFunc(int idFunc) {
		this.idFunc = idFunc;
	}
	public int getIdVeiculo() {
		return idVeiculo;
	}
	public void setIdVeiculo(int idVeiculo) {
		this.idVeiculo = idVeiculo;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getDataHoraSaida() {
		return dataHoraSaida;
	}
	public void setDataHoraSaida(String dataHoraSaida) {
		this.dataHoraSaida = dataHoraSaida;
	}
	public String getDataHoraChegada() {
		return dataHoraChegada;
	}
	public void setDataHoraChegada(String dataHoraChegada) {
		this.dataHoraChegada = dataHoraChegada;
	}
	public int getKmSaida() {
		return kmSaida;
	}
	public void setKmSaida(int kmSaida) {
		this.kmSaida = kmSaida;
	}
	public int getKmChegada() {
		return kmChegada;
	}
	public void setKmChegada(int kmChegada) {
		this.kmChegada = kmChegada;
	}
	
	
}
