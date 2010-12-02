package org.springframework.mobile.device.mvc;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class DeviceWebArgumentResolverTest {
	
	private DeviceWebArgumentResolver resolver = new DeviceWebArgumentResolver();

	private ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

	private Device device = new StubDevice();
	
	@Test
	public void resolve() throws Exception {
		request.setAttribute(DeviceResolverHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, device, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", Device.class), 0);
		Object resolved = resolver.resolveArgument(parameter, request);
		assertSame(device, resolved);
	}

	@Test
	public void unresolved() throws Exception {
		request.setAttribute(DeviceResolverHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, device, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		Object resolved = resolver.resolveArgument(parameter, request);
		assertSame(WebArgumentResolver.UNRESOLVED, resolved);
	}

	@RequestMapping
	public void handlerMethod(Device device) {
		
	}

	@RequestMapping
	public void handlerMethodUnresolved(String foo) {
		
	}

}
