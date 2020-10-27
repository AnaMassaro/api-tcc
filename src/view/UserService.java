package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import control.FuncionarioDAO;
import control.JavaMailApp;
import model.Funcionario;


@WebServlet("/user")
public class UserService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Funcionario obj = new Funcionario();
	FuncionarioDAO rep = new FuncionarioDAO();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		JavaMailApp jma = new JavaMailApp();
	
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
		String nomeFunc = dados.getString("nome");
		String emailFunc = dados.getString("email");
		String telefoneFunc = dados.getString("telefone");
		String cpf = dados.getString("cpf");
		String nCNH = dados.getString("cnh");		
		//String disponibilidade = dados.getString("disponibilidade");
		
		String[] carct = {"0","1","2","3","4","5","6","7","8","9"};
		String senha = "";
		for(int i = 0; i < 10; i++) {
			int j = (int) (Math.random()*carct.length);
			senha += carct[j];
		}
		
		obj.setIdEmpresa(Integer.parseInt(idEmpresa));
		obj.setNomeFunc(nomeFunc);
		obj.setEmail(emailFunc);
		obj.setTelefone(telefoneFunc);
		obj.setCpf(cpf);
		obj.setnCNH(nCNH);
		obj.setSenha(senha);
		obj.setDisponibilidade(true);
		
		//Parametros fora do escopo
		if(nomeFunc.length() > 50) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "nome fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(cpf.length() != 11){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cpf fora do escopo");
			out.print(retorno);
			return;
		}
		if(emailFunc.length() > 50) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "email fora do escopo");
			out.print(retorno);
			return;
		}
		if(telefoneFunc.length() > 11) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "telefone fora do escopo");
			out.print(retorno);
			return;
		}
		if(nCNH.length() > 20){
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cnh fora do escopo");
			out.print(retorno);
			return;
		}
		//CPF ja cadastrado
		if(rep.cpfExiste(cpf) == true) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cpf ja cadastrado");
			out.print(retorno);
			return;
		}
		
		//CNH Invalida
		if(!rep.isCNH(nCNH)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "CNH invalida");
			out.print(retorno);
			return;
		}
		
		//CPF Invalido
		if(!rep.isCPF(cpf)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cpf invalido");
			out.print(retorno);
			return;
		}
		
		//EMAIL inválido
		if(!jma.isValidEmailAddress(emailFunc)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "email invalido");
			out.print(retorno);
			return;
		}
		if(rep.insert(obj)) { 
			String retornaEmpresa = rep.nomeEmpresa(Integer.parseInt(idEmpresa));
			if(retornaEmpresa != null) {
				rep.mandaEmail(emailFunc, nomeFunc, retornaEmpresa, senha);
				response.setStatus(HttpServletResponse.SC_CREATED);
			}
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		
	    JSONObject retorno = new JSONObject();
		if(id != null){
			
			String status = rep.delete(Integer.parseInt(id));
			System.out.println("aquiii o metoooodo ->  " + status);
			if(status.equals("ok")) {
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}
			if(status.contains("constraint")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				retorno.put("erro", "e possivel excluir apenas usuarios recentes");
				out.print(retorno);
				return;
			}
			if(status.equals("no")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				retorno.put("erro", "usuario nao existe");
				out.print(retorno);
				return;
			}
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

		System.out.println(data);

		JSONObject dados = new JSONObject(data);
	    JSONObject retorno = new JSONObject();
	    
	    String id = request.getParameter("id");
	    String nome = dados.getString("nome");
	    String email = dados.getString("email");
	    String telefone = dados.getString("telefone");
	    
	  //Parametros fora do escopo
	  		if(nome.length() > 50) {
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "nome fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		
	  		if(email.length() > 50){
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "email fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		if(telefone.length() > 11) {
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "telefone fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		
	  	obj.setNomeFunc(nome);
	  	obj.setEmail(email);
	  	obj.setTelefone(telefone);
	  	
	  	if(rep.edit(obj, Integer.parseInt(id))) {
  			response.setStatus(HttpServletResponse.SC_OK);
	  	}else {
	  		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  	}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String idFunc = request.getParameter("id");
		String empresa = request.getParameter("empresa");
		System.out.println("funcionario -> " + idFunc);
		
	if(idFunc != null){
	
		JSONObject jo = rep.listid(Integer.parseInt(idFunc));
		out.print(jo);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		
	}else {
	
		JSONArray arr = rep.listar(Integer.parseInt(empresa));
	
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
