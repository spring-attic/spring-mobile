package org.springframework.mobile.device.mvc;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class DeviceResolverHandlerInterceptorTest {
	
	private Device device = new StubDevice();

	private DeviceResolverHandlerInterceptor interceptor = new DeviceResolverHandlerInterceptor(new DeviceResolver() {
		public Device resolveDevice(HttpServletRequest request) {
			return device;
		}
	});

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	public void resolve() throws Exception {
		assertTrue(interceptor.preHandle(request, response, null));
		assertSame(device, DeviceResolverHandlerInterceptor.getCurrentDevice(request));
	}

	@Test
	public void resolveDefaultResolver() throws Exception {
		interceptor = new DeviceResolverHandlerInterceptor();
		request.addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7");
		assertTrue(interceptor.preHandle(request, response, null));
		Device device = DeviceResolverHandlerInterceptor.getCurrentDevice(request);
		assertTrue(device.isMobile());
	}

}
