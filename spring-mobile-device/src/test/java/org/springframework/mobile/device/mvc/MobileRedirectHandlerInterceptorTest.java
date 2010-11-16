package org.springframework.mobile.device.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class MobileRedirectHandlerInterceptorTest {
	
	private MobileRedirectHandlerInterceptor interceptor = new MobileRedirectHandlerInterceptor("http://mobile.springsource.com");

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	public void redirect() throws Exception {
		Device mobile = new Device() {
			public boolean isMobile() {
				return true;
			}
		};
		request.setAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, mobile);
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://mobile.springsource.com", response.getRedirectedUrl());
	}
	
	@Test
	public void noRedirect() throws Exception {
		Device mobile = new Device() {
			public boolean isMobile() {
				return false;
			}
		};
		request.setAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, mobile);
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}
	
	@Test
	public void redirectContextRelative() throws Exception {
		interceptor = new MobileRedirectHandlerInterceptor("/mobile", true);
		Device mobile = new Device() {
			public boolean isMobile() {
				return true;
			}
		};
		request.setContextPath("/app");
		request.setAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, mobile);
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("/app/mobile", response.getRedirectedUrl());
	}

	
}
