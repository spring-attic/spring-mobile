package org.springframework.mobile.device.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.resolver.lib.AppleDeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

public class GenericDeviceResolutionServiceTest {
	
	private GenericDeviceResolutionService resolver = new GenericDeviceResolutionService();
	
	@Test
	public void resolve() {
		resolver.addDeviceResolver(new AppleDeviceResolver());
		Device device = resolver.resolveDevice(new TestRequestFactory("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/7D11").getRequest());
		assertNotNull(device);
		assertTrue(device.isMobileBrowser());
		assertTrue(device.isApple());		
	}

	@Test
	public void resolveDefault() {
		resolver.addDeviceResolver(new AppleDeviceResolver());
		Device device = resolver.resolveDevice(new TestRequestFactory().getRequest());
		assertNotNull(device);
		assertFalse(device.isMobileBrowser());
		assertFalse(device.isApple());		
	}

	@Test
	public void resolveDefaultNone() {
		Device device = resolver.resolveDevice(new TestRequestFactory().getRequest());
		assertNotNull(device);
		assertFalse(device.isMobileBrowser());
		assertFalse(device.isApple());		
	}
	
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
