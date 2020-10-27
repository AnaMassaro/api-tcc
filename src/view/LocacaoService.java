package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import control.FuncionarioDAO;
import control.LocacaoDAO;
import control.VeiculoDAO;
import model.Funcionario;
import model.Locacao;
import model.Veiculo;


@WebServlet("/locacao")
public class LocacaoService extends HttpServlet {

	LocacaoDAO rep = new LocacaoDAO();
	Locacao obj = new Locacao();
	
	VeiculoDAO vd = new VeiculoDAO();
	Veiculo v = new Veiculo();
	
	FuncionarioDAO fd = new FuncionarioDAO();
	Funcionario f = new Funcionario();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		PrintWriter out = response.getWriter();

		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}

		String data = buffer.toString();

		System.out.println(data);

		JSONObject dados = new JSONObject(data);
	    JSONObject retorno = new JSONObject();
	    
	    String idEmpresa = request.getParameter("empresa");
	    String funcionario = dados.getString("id_func");
	    String veiculo = dados.getString("id_veiculo");
	    String origem = dados.getString("origem");
	    String destino = dados.getString("destino");
	    String kmSaida = dados.getString("km_saida");
	    
	    int idFuncionario = rep.idFuncionario(funcionario);
	    System.out.println("AQUI ESTA O ID FUNC -> " + idFuncionario);

	    int idVeiculo = rep.idVeiculo(veiculo);
	    System.out.println("AQUI ESTA O ID VEICULO -> " + idVeiculo);
	    
	    if(origem.length() > 20) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "origem fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(destino.length() > 20){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "destino fora do escopo");
			out.print(retorno);
			return;
		}
		
		obj.setIdEmpresa(Integer.parseInt(idEmpresa));
		if(idFuncionario != 0) {
			obj.setIdFunc(idFuncionario);
		}else {
			retorno.put("erro", "funcionario null");
		}
		
		if(idVeiculo != 0) {
			obj.setIdVeiculo(idVeiculo);
		}else {
			retorno.put("retorno", "veiculo null");
		}
		
		obj.setOrigem(origem);
		obj.setDestino(destino);
		obj.setDataHoraChegada("");
		obj.setKmSaida(Integer.parseInt(kmSaida));
		obj.setKmChegada(0);
		
		int locacao = rep.insert(obj);
		if(locacao != 0) {
			v.setDisponibilidade(false);
			f.setDisponibilidade(false);
			fd.status(f, idFuncionario);
			vd.status(v, idVeiculo);
			retorno.put("locacao", locacao);
			out.print(retorno);
		
			response.setStatus(HttpServletResponse.SC_CREATED);
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		PrintWriter out = response.getWriter();

		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}

		String data = buffer.toString();


		JSONObject dados = new JSONObject(data);
	    JSONObject retorno = new JSONObject();
	    
	    String id = request.getParameter("locacao");
	    String kmChegada = dados.getString("kmChegada");
	    
	    int vet[] = rep.info(Integer.parseInt(id));
	    
	    
	    obj.setKmChegada(Integer.parseInt(kmChegada));
	    
	    if(rep.edit(obj, Integer.parseInt(id))) {
  			v.setDisponibilidade(true);
  			f.setDisponibilidade(true);
  			if( (vd.status(v, vet[1])) && fd.status(f, vet[0]) ) {
  	  			response.setStatus(HttpServletResponse.SC_OK);
  			}else {
  		  		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  			}
  			
	    }else {
	  		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  		retorno.put("erro", "else");
	  		out.print(retorno);
	    }
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String idLocacao = request.getParameter("id");
		String empresa = request.getParameter("empresa");
		System.out.println("locacao -> " + idLocacao);
		
	if(idLocacao != null){
	
		JSONObject jo = rep.listid(Integer.parseInt(idLocacao));
		out.print(jo);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		
	}else {
	
		JSONArray arr = rep.list(Integer.parseInt(empresa));
	
		if(arr.length() > 0) {
			out.print(arr);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.print("[]");
		}
	}	
}
	
	
	

}
