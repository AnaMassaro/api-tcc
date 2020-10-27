package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.InputMismatchException;

import java.util.Properties;
import java.util.Random;

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

import javax.mail.PasswordAuthentication;

import model.Empresa;

public class EmpresaDAO {
	
	private Connection con;
	private PreparedStatement ps;
	private Empresa e;

	
	public boolean insert(Empresa e){
		
		String sql = "insert into empresa values(default, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		con = ConnectionDB.getConnection();
		
		try {
			ps = con.prepareStatement(sql);
			
			ps.setString(1, e.getRazaoSocial());
			ps.setString(2, e.getCnpj());
			ps.setString(3, e.getEmailEmpresa());
			ps.setString(4, e.getLogradouro());
			ps.setString(5, e.getRua());
			ps.setString(6, e.getBairro());
			ps.setInt(7, e.getNumero());
			ps.setString(8, e.getCep());
			ps.setString(9, e.getSenhaEmpresa());
			
			ps.execute();
			ps.close();
			con.close();
			return true;
			
		} catch (SQLException e1) {
			System.out.println("Exception metodo cadastraEmpresa -" + e1.toString());
		}
		return false;
	}
	
	public boolean mandaEmail(String email, String codigoGerado) {
		 
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
		      message.setFrom(new InternetAddress("")); 
		      //Remetente
		 
		      Address[] toUser = InternetAddress.parse(email);  
		 
		      message.setRecipients(Message.RecipientType.TO, toUser);
		      message.setSubject("Boas vindas empresário!");//Assunto
		      message.setText("Sua conta foi criada com sucesso em nosso sistema, bem vindo a VOITURE!");
		      Transport.send(message);
		      retorno = true;
		      System.out.println("Feito!!!");
		     } catch (MessagingException e) {
		        throw new RuntimeException(e);
		        
		    }
		    return retorno;
		 
	 }
	
	public JSONArray idEmpresa(String cnpj) {
		
		JSONArray ret = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String sql = "select id_empresa from empresa where cnpj = '" + cnpj + "';";
		
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

			System.out.println("Exception no metodo idEmpresa - " + e.toString());
			return null;
		}
		
		
	}
	

	
	 public static boolean isCNPJ(String CNPJ) {
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
		        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
		        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
		        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
		        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
		       (CNPJ.length() != 14))
		       return(false);
		 
		    char dig13, dig14;
		    int sm, i, r, num, peso;
		 
		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		    try {
		// Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=11; i>=0; i--) {
		// converte o i-ésimo caractere do CNPJ em um número:
		// por exemplo, transforma o caractere '0' no inteiro 0
		// (48 eh a posição de '0' na tabela ASCII)
		        num = (int)(CNPJ.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig13 = '0';
		      else dig13 = (char)((11-r) + 48);
		 
		// Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=12; i>=0; i--) {
		        num = (int)(CNPJ.charAt(i)- 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig14 = '0';
		      else dig14 = (char)((11-r) + 48);
		 
		// Verifica se os dígitos calculados conferem com os dígitos informados.
		      if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
		         return(true);
		      else return(false);
		    } catch (InputMismatchException erro) {
		        return(false);
		    }
		  }
	 
	 public boolean cnpjExiste(String cnpj) {
		 String sql = "select * from empresa WHERE cnpj = ?;";
		 con = ConnectionDB.getConnection();
		 
		 try {
			ps = con.prepareStatement(sql);
			ps.setString(1, cnpj);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("cnpj").equals(cnpj)) {
					return true;
				}
			}
			
			ps.execute();
			ps.close();
			con.close();
		 } catch (SQLException e) {
			System.out.println("Exception metodo cnojExiste - " + e);
		}
		 return false;
	 }
	 
	 public JSONArray autenticacaoEmpresa(String cnpj, String senhaEmpresa) {
		 
		 JSONArray ret = new JSONArray();
		 JSONObject obj = new JSONObject();
		 String sql = "select * from empresa WHERE cnpj = '" + cnpj + "' AND senhaEmpresa = '" + senhaEmpresa + "';";
		 
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
			System.out.println("Exception no metodo dados - " + e.toString());
			return null;
		}
	 }
	

}
