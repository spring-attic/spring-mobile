package org.springframework.mobile.device.wurfl.wng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class BufferedHttpServletResponse extends HttpServletResponseWrapper  {
	
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	private PrintWriter writer = new PrintWriter(new OutputStreamWriter(buffer));
	
	private ServletOutputStream outputStream = new ServletOutputStream(){
		public void write(int b) throws IOException {
			buffer.write(b);
		}
	};
	
	public BufferedHttpServletResponse(HttpServletResponse response) {
		super(response);
	}
	
	public PrintWriter getWriter() throws IOException {
		return writer;
	}
	
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
	}
	
	public void writeTo(OutputStream out) throws IOException {
		buffer.writeTo(out);
	}
	
}