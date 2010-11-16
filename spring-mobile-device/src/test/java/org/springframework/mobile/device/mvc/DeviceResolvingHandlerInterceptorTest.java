package org.springframework.mobile.device.mvc;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class DeviceResolvingHandlerInterceptorTest {
	
	private Device device = new Device() {
		public boolean isMobile() {
			return true;
		}
	};

	private DeviceResolvingHandlerInterceptor interceptor = new DeviceResolvingHandlerInterceptor(new DeviceResolver() {
		public Device resolveDevice(HttpServletRequest request) {
			return device;
		}
	});

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	public void resolve() throws Exception {
		assertTrue(interceptor.preHandle(request, response, null));
		assertSame(device, request.getAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE));
	}
	
}
