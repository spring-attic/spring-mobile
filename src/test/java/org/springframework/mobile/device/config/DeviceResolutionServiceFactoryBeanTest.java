package org.springframework.mobile.device.config;

import org.junit.Test;
import org.springframework.mobile.device.config.DeviceResolutionServiceFactoryBean;

public class DeviceResolutionServiceFactoryBeanTest {
	
	@Test
	public void wurflDeviceResolutionService() throws Exception {
		DeviceResolutionServiceFactoryBean factoryBean = new DeviceResolutionServiceFactoryBean();
		factoryBean.getObject();
	}
	
}
