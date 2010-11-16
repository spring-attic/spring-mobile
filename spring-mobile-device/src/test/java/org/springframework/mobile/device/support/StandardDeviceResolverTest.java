package org.springframework.mobile.device.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.lite.LiteDeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

public class StandardDeviceResolverTest {
	
	private LiteDeviceResolver resolver = new LiteDeviceResolver();
	
	static class TestRequestFactory {

		private MockHttpServletRequest request;
		
		public TestRequestFactory() {
			this.request = new MockHttpServletRequest();
			this.request.addHeader("User-Agent", "test");
		}
		
		public TestRequestFactory(String userAgent) {
			this.request = new MockHttpServletRequest();
			this.request.addHeader("User-Agent", userAgent);
		}

		public HttpServletRequest getRequest() {
			return request;
		}
	}

}
