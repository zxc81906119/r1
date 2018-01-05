package controller;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class picturefile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public picturefile() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		uploadpicture(request);
		request.getRequestDispatcher("uploadpicture.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	private String uploadpicture(HttpServletRequest request) throws IOException{
		String contentType = request.getContentType();
        int formDataLength = request.getContentLength();
        // Ū���ШD����
        DataInputStream dataStream =
                new DataInputStream(request.getInputStream());
        byte datas[] = new byte[formDataLength];
        int totalBytes = 0;
        while (totalBytes < formDataLength) {
            int bytes = dataStream.read(datas, totalBytes, formDataLength);
            totalBytes += bytes;
        }
 
        // ���o�Ҧ����餺�e���r����
        String reqBody = new String(datas);
        System.out.println(reqBody);
        // ���o�W�Ǫ��ɮצW��
        String filename = reqBody.substring(
                reqBody.indexOf("filename=\"") + 10);
        filename = filename.substring(0, filename.indexOf("\n"));
        filename = filename.substring(
                filename.lastIndexOf("\\") + 1, filename.indexOf("\""));
 
        // ���o�ɮװϬq��ɸ�T
        String boundary = contentType.substring(
                contentType.lastIndexOf("=") + 1, contentType.length());
 
        // ���o��ڤW���ɮת��_�l�P������m
        int pos;
        pos = reqBody.indexOf("filename=\"");
        pos = reqBody.indexOf("\n", pos) + 1;
        pos = reqBody.indexOf("\n", pos) + 1;
        pos = reqBody.indexOf("\n", pos) + 1;
        int boundaryLoc = reqBody.indexOf(boundary, pos) - 4;
        int startPos = ((reqBody.substring(0, pos)).getBytes()).length;
        int endPos = ((reqBody.substring(0, boundaryLoc)).getBytes()).length;
 
        // ��X���ɮ�
        FileOutputStream fileOutputStream =
                new FileOutputStream("D:\\user\\Desktop\\budget\\WebContent\\images\\" + filename);
        fileOutputStream.write(datas, startPos, (endPos - startPos));
        fileOutputStream.flush();
        fileOutputStream.close();
		return "uploadpicture.jsp";
		
	}
}
