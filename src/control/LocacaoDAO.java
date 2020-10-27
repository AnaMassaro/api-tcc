package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Statement;

import model.Locacao;
import model.Veiculo;

public class LocacaoDAO {

	private Connection con;
	private PreparedStatement ps;
	private Locacao l;
	
	public int insert(Locacao l) {
		
		String sql = "insert into locacao values(default, ?, ?, ?, ?, ?, now(), ?, ?, ?);";
		con = ConnectionDB.getConnection();
		int lastId = 0;
		try {
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, l.getIdEmpresa());
			ps.setInt(2, l.getIdFunc());
			ps.setInt(3, l.getIdVeiculo());
			ps.setString(4, l.getOrigem());
			ps.setString(5, l.getDestino());
			//ps.setString(6, l.getDataHoraSaida());
			ps.setString(6, l.getDataHoraChegada());
			ps.setInt(7, l.getKmSaida());
			ps.setInt(8, l.getKmChegada());
			
			ps.execute();
			
			final ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
			    lastId = rs.getInt(1);
			}
			
			ps.close();
			con.close();
			return lastId;
		} catch (SQLException e) {
			System.out.println("Exception LocacaoDAO metodo insert -> " + e);
		}
		
		return 0;
	}
	
	public boolean edit(Locacao l, int id) {
		
		try {
			String sql = "update locacao set dataHora_chegada = now(), km_chegada = ? where id_locacao = " + id + ";";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, l.getKmChegada());

			ps.execute();
			ps.close();
			con.close();
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception metodo edit classe locacaoDAO -> " + e);
			
			return false;
		}
	}
	
	public int[] info(int locacao){
		int vet[] = {0,0};
			try {
				String sql = "select id_locacao, id_func, id_veiculo from locacao where id_locacao = '" + locacao + "';";
				con = ConnectionDB.getConnection();
				ps = con.prepareStatement(sql);
				
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					if(rs.getInt("id_locacao") == locacao) {
						
						vet[0] = rs.getInt("id_func");
						vet[1] = rs.getInt("id_veiculo");
						return vet;
					}
				}		
				ps.execute();
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("Erro metodo info " + e.toString());
			}
			return vet;
	}
	
	
	public int idFuncionario(String hash) {

		try {
			String sql = "select id_func, hash from funcionario where hash  = '" + hash + "';";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("hash").equals(hash)) {
					int id = rs.getInt("id_func");
					return id;
				}
			}
			
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Exception metodo idFuncionario - " + e.toString());
		}
		
		return 0;
		
	}
	
	public int idVeiculo(String hash) {
		try {
			String sql = "select id_veiculo, hash from veiculo where hash  = '" + hash + "';";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("hash").equals(hash)) {
					int id = rs.getInt("id_veiculo");
					return id;
				}
			}
			
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Exception metodo idFuncionario - " + e.toString());
		}
		
		return 0;
		
	}
	
	public JSONArray list(int id_empresa) {

			JSONArray ret = new JSONArray();
			JSONObject obj = new JSONObject();

			String sql = 
					"select  l.id_locacao, l.id_empresa, l.id_func, l.id_veiculo, l.origem, l.destino, l.dataHora_saida, l.dataHora_chegada, l.km_saida, l.km_chegada, v.placa, v.modelo, f.nome_func, f.email from locacao as l inner join veiculo as v,funcionario as f where l.id_veiculo = v.id_veiculo and l.id_func = f.id_func and l.id_empresa = '" + id_empresa + "';";
			con = ConnectionDB.getConnection();
			
			try {
				ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				
				int coluna = rsmd.getColumnCount();
				
				while(rs.next()) {

					for(int i = 1; i <= coluna; i++) {
						obj.put(rsmd.getColumnName(i), rs.getString(i));
					}
					ret.put(obj);
					obj = new JSONObject();
				}
				
				con.close();
				ps.close();
				return ret;
				
			} catch (SQLException e) {

				System.out.println("Exception no metodo list locacao - " + e.toString());
				return null;
			}
			
		}
	
	public JSONObject listid(int id_locacao) {

			JSONObject obj = new JSONObject();
			
			String sql = "select * from locacao where id_locacao = '" + id_locacao + "';";
			con = ConnectionDB.getConnection();
			
			try {
				ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				
				int coluna = rsmd.getColumnCount();
				
				while(rs.next()) {

					for(int i = 1; i <= coluna; i++) {
						obj.put(rsmd.getColumnName(i), rs.getString(i));
					}
				}
				
				con.close();
				ps.close();
				return obj;
				
			} catch (SQLException e) {
				System.out.println("Exception no metodo listid locacao - " + e.toString());
				return null;
			}	
		}
	
	
	
}
