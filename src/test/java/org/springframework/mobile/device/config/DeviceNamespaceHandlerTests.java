package org.springframework.mobile.device.config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolutionService;
import org.springframework.mock.web.MockHttpServletRequest;

public class DeviceNamespaceHandlerTests {
	
	@Test
	public void wurflDeviceResolutionService() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/mobile/device/config/device.xml");
		DeviceResolutionService service = context.getBean("root", DeviceResolutionService.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		Device device = service.resolveDevice(request);
		assertNotNull(device);
		assertFalse(device.isMobileBrowser());
		assertFalse(device.isApple());
		context.getBean("rootAndPatches", DeviceResolutionService.class);
	}
	
}