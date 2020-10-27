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

import control.EmpresaDAO;
import control.JavaMailApp;
import model.Empresa;

/**
 * Servlet implementation class EmpresaService
 */
@WebServlet("/empresa")
public class EmpresaService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Empresa obj = new Empresa();
		EmpresaDAO rep = new EmpresaDAO();
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

		String razaoSocial = dados.getString("razaoSocial");
		String cnpj = dados.getString("cnpj");
		String emailEmpresa = dados.getString("email");
		String logradouro = dados.getString("cidade");
		String rua = dados.getString("rua");
		String bairro = dados.getString("bairro");
		String numero = dados.getString("numero");
		String cep = dados.getString("cep");
		
		String[] carct = {"0","1","2","3","4","5","6","7","8","9"};
		String senha = "";
		for(int i = 0; i < 10; i++) {
			int j = (int) (Math.random()*carct.length);
			senha += carct[j];
		}
		
		obj.setRazaoSocial(razaoSocial);
		obj.setCnpj(cnpj);
		obj.setEmailEmpresa(emailEmpresa);
		obj.setLogradouro(logradouro);
		obj.setRua(rua);
		obj.setBairro(bairro);
		obj.setNumero(Integer.parseInt(numero));
		obj.setCep(cep);
		obj.setSenhaEmpresa(senha);
		
		//Parametros fora do escopo
		if(razaoSocial.length() > 100) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "razao social fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(cnpj.length() > 14) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cnpj fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(emailEmpresa.length() > 50) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "email fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(rua.length() > 30) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "rua fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(bairro.length() > 30) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "bairro fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(cep.length() > 8) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cep fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(logradouro.length() > 100) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cidade fora do escopo");
			out.print(retorno);
			return;
		}
		//CNPJ já cadastrado
		if(rep.cnpjExiste(cnpj) == true) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "CNPJ ja cadastrado");
			out.print(retorno);
			return;
		}
		
		//CNPJ inválido
		if(!rep.isCNPJ(cnpj)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "CNPJ invalida");
			out.print(retorno);
			return;
		}
		
		//EMAIL inválido
		if(!jma.isValidEmailAddress(emailEmpresa)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "email invalido");
			out.print(retorno);
			return;
		}
		
		if(rep.insert(obj)) {
			rep.mandaEmail(obj.getEmailEmpresa(), senha);
		
			JSONArray arr = rep.idEmpresa(cnpj);
		   
				if(arr.length() > 0) {
					JSONObject json = (JSONObject) arr.get(0);
					
					retorno.put("id_empresa", json.getString("id_empresa"));
					out.print(retorno);
					response.setStatus(HttpServletResponse.SC_CREATED);
				}else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);	
				}
		}
		
		
		
	}

}
