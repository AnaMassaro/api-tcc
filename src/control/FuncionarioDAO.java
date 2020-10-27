package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Funcionario;

public class FuncionarioDAO {

	private Connection con;
	private PreparedStatement ps;
	private Funcionario f;
	
	
	
	public boolean insert(Funcionario f) {
		
		String sql = "insert into funcionario values(default, ?, ?, ?, ?, ?, ?, ?, ?);";
		con = ConnectionDB.getConnection();
		
		try {
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, f.getIdEmpresa());
			ps.setString(2, f.getNomeFunc());
			ps.setString(3, f.getEmail());
			ps.setString(4, f.getTelefone());
			ps.setString(5, f.getCpf());
			ps.setString(6, f.getnCNH());
			ps.setString(7, f.getSenha());
			ps.setBoolean(8, f.isDisponibilidade());
			
			ps.execute();
			ps.close();
			con.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Exception metodo cadastraFuncionario - " + e.toString());
		}
		
		return false;
	}
	
	public JSONArray autenticacaoFuncionario(String cpf, String senhaFuncionario) {
		
		JSONArray ret = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String sql = "select * from funcionario where cpf = '" + cpf + "' and hash = '" + senhaFuncionario + "';";
		
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

			System.out.println("Exception no metodo autenticacaoFuncionario - " + e.toString());
			return null;
		}
	}
	
	
	public boolean idExiste(int id) {
		String sql = "select * from funcionario where id_func = '" + id + "';";
		con = ConnectionDB.getConnection();
		
		try {
			ps = con.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getInt("id_func") == id) {
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
	
	public String delete(int id){
		if(idExiste(id)) {
			String sql = "delete from funcionario where id_func = '" + id + "';";
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

	public boolean edit(Funcionario f, int id) {
	
		try {
			String sql = "update funcionario set nome_func = ?, email = ?, telefone = ? where id_func = " + id + ";";
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1, f.getNomeFunc());
			ps.setString(2, f.getEmail());
			ps.setString(3, f.getTelefone());
			
			ps.execute();
			ps.close();
			con.close();
			
			return true;
		} catch (SQLException e) {
			System.out.println("Excpetion metodo edit FUNCIONARIO -> " + e);
			return false;
		}
	}
	
	 public boolean cpfExiste(String cpf) {
		 String sql = "select * from funcionario WHERE cpf = ?;";
		 con = ConnectionDB.getConnection();
		 
		 try {
			ps = con.prepareStatement(sql);
			ps.setString(1, cpf);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("cpf").equals(cpf)) {
					return true;
				}
			}
			
			ps.execute();
			ps.close();
			con.close();
		 } catch (SQLException e) {
			System.out.println("Exception metodo cpfExiste - " + e.toString());
		}
		 return false;
	 }
	 	 
	 public String hash(int id) {
		 String hash;
		
		 try {
			String sql = "select id_func, hash from funcionario where id_func = '" + id + "';";
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
			System.out.println("deu erro no metodo hash de funcionario " + e);
		}
		 
		 return null;
		 
	 }
	 
	 public String nomeEmpresa(int idEmpresa) {
		 String nomeEmpresa = null;
		 String sql = "select id_empresa,razao_social from empresa where id_empresa = '" + idEmpresa + "';";
		 con = ConnectionDB.getConnection();
		 
		 try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
			if(rs.getInt("id_empresa") == idEmpresa) {
					nomeEmpresa = rs.getString("razao_social");
					return nomeEmpresa;
				}
			}
			
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Exception metodo nomeEmpresa - " + e.toString());
		}
		 return null;
	 }
	 
	 public JSONArray listar(int empresa) {
		 JSONArray ret = new JSONArray();
		 JSONObject obj = new JSONObject();
		 
		 try {
			String sql = "select * from funcionario where id_empresa = '" + empresa + "';";
			con = ConnectionDB.getConnection();
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
			System.out.println("Exception no metodo list funcionario - " + e.toString());
			return null;
		}
		 
	 }
	 
	 public JSONArray list() {

			JSONArray ret = new JSONArray();
			JSONObject obj = new JSONObject();
			
			String sql = "select * from funcionario;";
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

				System.out.println("Exception no metodo list funcionario - " + e.toString());
				return null;
			}
			
		}

		 public JSONObject listid(int id_func) {

			JSONObject obj = new JSONObject();
			
			String sql = "select * from funcionario where id_func = '" + id_func + "';";
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
				System.out.println("Exception no metodo listid funcionario - " + e.toString());
				return null;
			}	
		}
	 public boolean status(Funcionario f, int id) {
		
		 try {
			String sql = "update funcionario set disponibilidade = ? where id_func = " + id + ";";
			con = ConnectionDB.getConnection();
		    ps = con.prepareStatement(sql);
		    
		    ps.setBoolean(1, f.isDisponibilidade());
		    ps.execute();
			ps.close();
			con.close();
			
			return true;
		} catch (SQLException e) {
			System.out.println("Exception metodo status funcionario -> " + e);
			return false;
		}
	 }
	 
	 
	 public boolean mandaEmail(String email, String nomeFunc, String nomeEmpresa, String codigoGerado) {
		 
			boolean retorno = false;
			
			 Properties props = new Properties();
			 
			    props.put("mail.smtp.host", "smtp.gmail.com");
			    props.put("mail.smtp.socketFactory.port", "465");
			    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			    props.put("mail.smtp.auth", "true");
			    props.put("mail.smtp.port", "465");
			 
			    Session session = Session.getDefaultInstance(props,
			      new javax.mail.Authenticator() {
			           protected PasswordAuthentication getPasswordAuthentication() 
			           {
			                 return new PasswordAuthentication("", 
			                 "");
			           }
			      });
			 
			    session.setDebug(true);
			 
			    try {
			 
			      Message message = new MimeMessage(session);
			      message.setFrom(new InternetAddress(")); 
			      //Remetente
			 
			      Address[] toUser = InternetAddress.parse(email);  
			 
			      message.setRecipients(Message.RecipientType.TO, toUser);
			      message.setSubject("Boas vindas " + nomeFunc);//Assunto
			      message.setText("Sua conta foi criada em nosso sistema, está vinculada com a empresa " + nomeEmpresa + " , este é o seu codigo de acesso"
			      		+ ", utilize-o junto ao seu CPF para efetuar LOGIN. " + codigoGerado);
			      Transport.send(message);
			      retorno = true;
			      System.out.println("Feito!!!");
			     } catch (MessagingException e) {
			        throw new RuntimeException(e);
			        
			    }
			    return retorno;
			 
		 }
	 
		public static boolean isCNH(String cnh) {
			char char1 = cnh.charAt(0);

			if (cnh.replaceAll("\\D+", "").length() != 11
					|| String.format("%0" + 11 + "d", 0).replace('0', char1).equals(cnh)) {
				return false;
			}

			long v = 0, j = 9;

			for (int i = 0; i < 9; ++i, --j) {
				v += ((cnh.charAt(i) - 48) * j);
			}

			long dsc = 0, vl1 = v % 11;

			if (vl1 >= 10) {
				vl1 = 0;
				dsc = 2;
			}

			v = 0;
			j = 1;

			for (int i = 0; i < 9; ++i, ++j) {
				v += ((cnh.charAt(i) - 48) * j);
			}

			long x = v % 11;
			long vl2 = (x >= 10) ? 0 : x - dsc;

			return (String.valueOf(vl1) + String.valueOf(vl2)).equals(cnh.substring(cnh.length() - 2));

		}
		
	public static boolean isCPF(String CPF) {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
		
	}
}
