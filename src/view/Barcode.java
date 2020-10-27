package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import control.FuncionarioDAO;
import control.VeiculoDAO;

/**
 * Servlet implementation class Barcode
 */
@WebServlet("/barcode")
public class Barcode extends HttpServlet {
  
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Document document = new Document(PageSize.A6, 20, 10, 20, 20);
		Paragraph p;
		
		VeiculoDAO vd = new VeiculoDAO();
		FuncionarioDAO fd = new FuncionarioDAO();
		String tipo = req.getParameter("tipo");
		//Tipo 1 = veiculo
		//Tipo 2 = funcionario
		String id = req.getParameter("id");
		String codigo = null;
		Random gerador = new Random();

		String fileName = "D://arquivo"+gerador.nextInt(100)+".pdf";

		if(tipo.equals("1")) {
			codigo = vd.hash(Integer.parseInt(id));
		}else {
			codigo = fd.hash(Integer.parseInt(id));
		}
		
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));

			document.open();
			PdfContentByte cb = writer.getDirectContent();
	        
            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(codigo, 500, 500, null);
	        Image codeQrImage = barcodeQRCode.getImage();
	        codeQrImage.scaleAbsolute(100, 100);
	        
            p = new Paragraph();
            p.add(codeQrImage);
            document.add(p);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		document.close();
		
		File file = new File(fileName);
        resp.setHeader("Content-Type", getServletContext().getMimeType(file.getName()));
        resp.setHeader("Content-Length", String.valueOf(file.length()));
        Files.copy(file.toPath(), resp.getOutputStream());
	}



}