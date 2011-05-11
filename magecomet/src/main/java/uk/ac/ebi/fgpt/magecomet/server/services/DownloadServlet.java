package uk.ac.ebi.fgpt.magecomet.server.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Get file
		String fileurl = request.getParameter("fileURL");
		File file = new File(fileurl);
		FileInputStream fis = new FileInputStream(file);

		
		response.setContentType("application/download");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ request.getParameter("fileName")  +"\"");


		// Configure output
		long length = file.length();
		response.setContentLength((int) length);
		response.setBufferSize(32768);
		int bufSize = response.getBufferSize();
		byte[] buffer = new byte[bufSize];

		// Write File Back Out
		ServletOutputStream out = response.getOutputStream();

		BufferedInputStream bis = new BufferedInputStream(fis, bufSize);
		int bytes;
		while ((bytes = bis.read(buffer, 0, bufSize)) >= 0)
			out.write(buffer, 0, bytes);

		bis.close();
		fis.close();
		out.flush();
		out.close();

	}
}
