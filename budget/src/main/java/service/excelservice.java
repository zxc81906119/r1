package service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class excelservice {
	private Properties props = new Properties();
	
	private Session session;
	
	private String to;
	//撱箸���
	public excelservice() {
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.setProperty("mail.smtp.auth", "true");
		this.session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("zxc81906119@gmail.com", "game54088");
			}
		});
	}
	
	private String Subject = "";
	public void getexcel(List<String[]> slist,HttpServletRequest request, OutputStream output) {
		HSSFWorkbook workbook =null;
		try {
			workbook=createxcel(slist,request);
			workbook.write(output);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private	HSSFWorkbook createxcel(List<String[]> slist,HttpServletRequest request) throws IOException {
		String path=request.getRealPath("/").replace("/","\\")+"WEB-INF\\excel\\收支報表.xls";
		FileInputStream fis=new FileInputStream(path);
		
		HSSFWorkbook workbook=new HSSFWorkbook(fis);
		HSSFSheet worksheet = workbook.getSheetAt(0);
		for(int i=1;i<=slist.size();i++) {
			 HSSFRow row = worksheet.createRow(i);
			 String[] onerow=slist.get(i-1);
				 for(int j=0;j<onerow.length;j++) {
					 HSSFCell cell=row.createCell((short)j);
					 cell.setCellValue(onerow[j]);
				 }	 
		}
		int afterlastoneindex=slist.size()+1;
		while(worksheet.getRow(afterlastoneindex)!=null) {
			worksheet.removeRow(worksheet.getRow(afterlastoneindex));
			afterlastoneindex++;	
		}
		
		for(int k=0;k<slist.get(0).length;k++) {
			worksheet.autoSizeColumn((short)k);
		}
			return workbook;
	}
	
	private Message getExcelFileMessage(String html, String fileName, byte[] data)
			throws AddressException, MessagingException {
		Message message = new MimeMessage(this.session);
		
		try {
			message.setFrom(new InternetAddress("zxc81906119@gmail.com", "收支系統"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		message.setSubject(this.Subject);
		message.setSentDate(new Date());

		MimeBodyPart htmlPart = new MimeBodyPart();
		MimeBodyPart filePart = new MimeBodyPart();

		htmlPart.setContent(html, "text/html;charset=UTF-8");

		filePart.setContent(data, "application/vnd.ms-excel");
		try {
			filePart.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(htmlPart);
		multipart.addBodyPart(filePart);

		message.setContent(multipart);

		return message;
	}
	
	private void sendExcelFileMail(String To, String Subject, String html, String fileName, byte[] data)
			throws AddressException, MessagingException {
		this.Subject = Subject;
		try {
			
			Message message = this.getExcelFileMessage(html, fileName+".xls", data);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(To));
			Transport.send(message);
		} catch (AddressException e) {
			throw new AddressException(e.getMessage());
		} catch (MessagingException e) {
			throw new MessagingException(e.getMessage());
		}
	}
	
	public void sendexcel(List<String[]> slist,HttpServletRequest request,String email,String filename) throws IOException, AddressException, MessagingException {
		HSSFWorkbook workbook = createxcel(slist,request);
		ByteArrayOutputStream out =new ByteArrayOutputStream();	
		String title="收支系統vs娃娃機2018版本";
		StringBuilder html=new StringBuilder();
		String nowTime=new SimpleDateFormat("yyyy/MM/dd hh:mm ").format(new Date());
		html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
		html.append("<center><h1>哥哥!</h1><hr>");
		html.append("<p>寄發時間"+nowTime+"</p>");
		html.append("</center>");
		workbook.write(out);
		sendExcelFileMail(email, title, html.toString(), filename , out.toByteArray());
		workbook.close();
		out.close();
	}
}
