package org.springframework.mobile.device.config;

import org.junit.Test;
import org.springframework.mobile.device.config.DefaultDeviceResolutionServiceFactoryBean;

public class DefaultDeviceResolutionServiceFactoryBeanTest {
	
	@Test
	public void wurflDeviceResolutionService() throws Exception {
		DefaultDeviceResolutionServiceFactoryBean factoryBean = new DefaultDeviceResolutionServiceFactoryBean();
		factoryBean.getObject();
	}
	
}