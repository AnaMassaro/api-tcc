package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import control.VeiculoDAO;
import model.Veiculo;

@WebServlet("/veiculo")
public class VeiculoService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Veiculo obj = new Veiculo();
	VeiculoDAO rep = new VeiculoDAO();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	

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

		String idEmpresaVeiculo = req.getParameter("empresa");
		String idCategoria = dados.getString("categoria");
		String placa = dados.getString("placa");
		String modelo = dados.getString("modelo");
		String cor = dados.getString("cor");
		String ano = dados.getString("ano");
		String km = dados.getString("km");
		String disponibilidade = dados.getString("disponibilidade");
		String situacao = dados.getString("situacao");
		String marca = dados.getString("marca");
		String[] carct = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","Q","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String qrcode = "";
		for(int i = 0; i < 10; i++) {
			int j = (int) (Math.random()*carct.length);
			qrcode += carct[j];
		}
		
		//Parametros fora do escopo
		if(placa.length() != 7) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "placa fora do escopo");
			out.print(retorno);
			return;
		}
		
		if(modelo.length() > 30){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "modelo fora do escopo");
			out.print(retorno);
			return;
		}
		if(cor.length() > 10) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "cor fora do escopo");
			out.print(retorno);
			return;
		}
		if(ano.length() != 4) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "ano fora do escopo");
			out.print(retorno);
			return;
		}
		if(marca.length() > 40) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			retorno.put("erro", "marca fora do escopo");
			out.print(retorno);
			return;
		}
		
		obj.setIdEmpresa(Integer.parseInt(idEmpresaVeiculo));
		obj.setIdCategoria(Integer.parseInt(idCategoria));
		obj.setPlaca(placa);
		obj.setModelo(modelo);
		obj.setCor(cor);
		obj.setAno(Integer.parseInt(ano));
		obj.setKm(Integer.parseInt(km));
		obj.setDisponibilidade(Boolean.getBoolean(disponibilidade));
		obj.setSituacao(Boolean.getBoolean(situacao));
		obj.setQrcode(qrcode);
		obj.setMarca(marca);
		
		int id = rep.insert(obj);
		if(id != 0) {
			retorno.put("id", id);
			out.print(retorno);
			resp.setStatus(HttpServletResponse.SC_CREATED);
		}else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				PrintWriter out = response.getWriter();
				String idEmpresa = request.getParameter("empresa");
				String idCategoria = request.getParameter("categoria");
				String idVeiculo = request.getParameter("veiculo");
				System.out.println("Empresa ->  " + idEmpresa);
				System.out.println("Categoria ->  " + idCategoria);
				System.out.println("Veiculo -> " + idVeiculo);
				
			if( (idEmpresa != null) && (idCategoria != null)){
				
				JSONArray arr = rep.list(Integer.parseInt(idEmpresa), Integer.parseInt(idCategoria));
				
				if(arr.length() > 0) {
					out.print(arr);
					response.setStatus(HttpServletResponse.SC_ACCEPTED);
				}else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					out.print("[]");
				}
			return;
			}
			if(idVeiculo != null) {
				JSONObject jo = rep.listid(Integer.parseInt(idVeiculo));
				out.print(jo);
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
			}	
		}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		
	    JSONObject retorno = new JSONObject();
		if(id != null){
			
			String status = rep.delete(Integer.parseInt(id));
			
			if(status.equals("ok")) {
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}
			if(status.contains("constraint")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				retorno.put("erro", "e possivel excluir apenas veiculo recentes");
				out.print(retorno);
				return;
			}
			if(status.equals("no")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				retorno.put("erro", "veiculo nao existe");
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
	    String placa = dados.getString("placa");
	    String modelo = dados.getString("modelo");
	    String cor = dados.getString("cor");
	    String ano = dados.getString("ano");
	    String marca = dados.getString("marca");
	    
	  //Parametros fora do escopo
	  		if(placa.length() != 7) {
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "placa fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		
	  		if(modelo.length() > 30){
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "modelo fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		if(cor.length() > 10) {
	  			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  			retorno.put("erro", "cor fora do escopo");
	  			out.print(retorno);
	  			return;
	  		}
	  		
	  	obj.setPlaca(placa);
	  	obj.setModelo(modelo);
	  	obj.setCor(cor);
	  	obj.setAno(Integer.parseInt(ano));
	  	obj.setMarca(marca);
	  	
	  	if(rep.edit(obj, Integer.parseInt(id))) {
  			response.setStatus(HttpServletResponse.SC_OK);
	  	}else {
	  		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	  		retorno.put("erro", "else");
	  		out.print(retorno);
	  	}
	  		
	  	
	}
}
