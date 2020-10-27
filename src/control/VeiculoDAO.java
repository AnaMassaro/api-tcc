package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Statement;

import model.Veiculo;

public class VeiculoDAO {
	
	private Connection con;
	private PreparedStatement ps;
	private Veiculo v;
	
	public int insert(Veiculo v) {
		String sql = "insert into veiculo values(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
		con = ConnectionDB.getConnection();
		int lastId = 0;
		try {
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, v.getIdEmpresa());
			ps.setInt(2, v.getIdCategoria());
			ps.setString(3, v.getPlaca());
			ps.setString(4, v.getModelo());
			ps.setString(5, v.getCor());
			ps.setInt(6, v.getAno());
			ps.setInt(7, v.getKm());
			ps.setBoolean(8, v.isDisponibilidade());
			ps.setBoolean(9, v.isSituacao());
			ps.setString(10, v.getQrcode());
			ps.setString(11, v.getMarca());
			
			ps.execute();
			
			final ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
			    lastId = rs.getInt(1);
			}
			
			ps.close();
			con.close();
			return lastId;
		} catch (SQLException e) {
			System.out.println("Erro no metodo cadastra veiculo " + e);
		}
		return 0;
	}
	
	public JSONArray list(int id_empresa, int id_categoria) {

		JSONArray ret = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String sql = "select * from veiculo where id_empresa = '" + id_empresa + "' and id_categoria = '" + id_categoria + "';";
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

			System.out.println("Exception no metodo listaVeiculos - " + e.toString());
			return null;
		}
		
	}
	
	public JSONObject listid(int id_veiculo) {

		JSONObject obj = new JSONObject();
		
		String sql = "select * from veiculo where id_veiculo = '" + id_veiculo + "';";
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
			System.out.println("Exception no metodo listid - " + e.toString());
			return null;
		}	
	}
	
	public boolean veiculoExiste(int id) {

			String sql = "select * from veiculo where id_veiculo = '" + id + "';";
			con = ConnectionDB.getConnection();
		try {
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt(1) == id) {
					return true;
				}
			}
			ps.close();
			con.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public String hash(int id) {
		String hash;
		try {
			String sql = "select id_veiculo,hash from veiculo where id_veiculo = '" + id + "';";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getInt(1) == id) {
					hash = rs.getString("hash");
					return hash;
				}
			}

			ps.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String delete(int id) {
		
		if(veiculoExiste(id)) {
			String sql = "delete from veiculo where id_veiculo = '" + id + "';";
			con = ConnectionDB.getConnection();
			try {
				ps = con.prepareStatement(sql);
				ps.execute();
				ps.close();
				con.close();
				
				return "ok";
			} catch (SQLException e) {
				e.printStackTrace();
				return e.toString();
			}
		}else {
			return "no";
		}
	
	}

	
	public boolean status(Veiculo v, int id) {
		
		try {
			String sql = "update veiculo set disponibilidade = ? where id_veiculo = " + id + ";";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setBoolean(1, v.isDisponibilidade());
			ps.execute();
			ps.close();
			con.close();
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception metodo status -> " + e);
			return false;
		}
	}
	
	public boolean edit(Veiculo v, int id) {
		
		try {
			String sql = "update veiculo set placa = ?, modelo = ?, cor = ?, ano = ?, marca = ? where id_veiculo = " + id + ";";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, v.getPlaca());
			ps.setString(2, v.getModelo());
			ps.setString(3, v.getCor());
			ps.setInt(4, v.getAno());
			ps.setString(5, v.getMarca());
			
			ps.execute();
			ps.close();
			con.close();
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception metodo edit -> " + e);
			
			return false;
		}
	}
	
	
	


}
