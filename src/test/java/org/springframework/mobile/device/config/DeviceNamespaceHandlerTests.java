package org.springframework.mobile.device.config;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mobile.device.DeviceResolutionService;

public class DeviceNamespaceHandlerTests {
	
	@Test
	@Ignore
	public void wurflDeviceResolutionService() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("org/springframework/mobile/device/config/device.xml");
		context.getBean("root", DeviceResolutionService.class);
		context.getBean("rootAndPatches", DeviceResolutionService.class);
	}
	
}