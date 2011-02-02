/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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