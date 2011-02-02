package org.springframework.mobile.device.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

public class DeviceNamespaceHandlerTest {
	
	@Test
	public void wurflDeviceResolutionService() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/mobile/device/config/device.xml");
		DeviceResolver service = context.getBean("root", DeviceResolver.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		Device device = service.resolveDevice(request);
		assertNotNull(device);
		assertTrue(device.isMobile());
		context.getBean("rootAndPatches", DeviceResolver.class);
	}
	
}