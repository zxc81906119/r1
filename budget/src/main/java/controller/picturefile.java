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
        // 讀取請求本體
        DataInputStream dataStream =
                new DataInputStream(request.getInputStream());
        byte datas[] = new byte[formDataLength];
        int totalBytes = 0;
        while (totalBytes < formDataLength) {
            int bytes = dataStream.read(datas, totalBytes, formDataLength);
            totalBytes += bytes;
        }
 
        // 取得所有本體內容的字串表示
        String reqBody = new String(datas);
        System.out.println(reqBody);
        // 取得上傳的檔案名稱
        String filename = reqBody.substring(
                reqBody.indexOf("filename=\"") + 10);
        filename = filename.substring(0, filename.indexOf("\n"));
        filename = filename.substring(
                filename.lastIndexOf("\\") + 1, filename.indexOf("\""));
 
        // 取得檔案區段邊界資訊
        String boundary = contentType.substring(
                contentType.lastIndexOf("=") + 1, contentType.length());
 
        // 取得實際上傳檔案的起始與結束位置
        int pos;
        pos = reqBody.indexOf("filename=\"");
        pos = reqBody.indexOf("\n", pos) + 1;
        pos = reqBody.indexOf("\n", pos) + 1;
        pos = reqBody.indexOf("\n", pos) + 1;
        int boundaryLoc = reqBody.indexOf(boundary, pos) - 4;
        int startPos = ((reqBody.substring(0, pos)).getBytes()).length;
        int endPos = ((reqBody.substring(0, boundaryLoc)).getBytes()).length;
 
        // 輸出至檔案
        FileOutputStream fileOutputStream =
                new FileOutputStream("D:\\user\\Desktop\\budget\\WebContent\\images\\" + filename);
        fileOutputStream.write(datas, startPos, (endPos - startPos));
        fileOutputStream.flush();
        fileOutputStream.close();
		return "uploadpicture.jsp";
		
	}
}
