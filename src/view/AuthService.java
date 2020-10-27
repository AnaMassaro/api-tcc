package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import control.FuncionarioDAO;
import model.Funcionario;

@WebServlet("/auth")
public class AuthService extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		   
			Funcionario obj = new Funcionario();
			FuncionarioDAO rep = new FuncionarioDAO();
		
		 	StringBuilder buffer = new StringBuilder();
		    BufferedReader reader = req.getReader();
		    String line;
		    PrintWriter out = resp.getWriter();
		    
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		    }
		    
		    String data = buffer.toString();
		    
		    System.out.println(data);
		    
		    JSONObject dados = new JSONObject(data);
		    JSONObject retorno = new JSONObject();
		    
			String cpf = dados.getString("cpf");
			String senhaAcesso = dados.getString("senhaAcesso");
			
			JSONArray arr = rep.autenticacaoFuncionario(cpf, senhaAcesso);
		    

				if(arr.length() > 0) {
					JSONObject json = (JSONObject) arr.get(0);
					
					retorno.put("id_func", json.getString("id_func"));
					retorno.put("id_empresa", json.getString("id_empresa"));
					retorno.put("nome", json.getString("nome_func"));
					retorno.put("email", json.getString("email"));
					retorno.put("telefone", json.getString("telefone"));
					retorno.put("cpf", json.getString("cpf"));
					retorno.put("cnh", json.getString("nCNH"));
					
					resp.setStatus(HttpServletResponse.SC_OK);
				}else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);	
				}
			    
				out.print(retorno);
				
				
 		
			  
		    
		
	}
}
