package org.springframework.mobile.device.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.support.AppleDeviceResolver;

public class AppleDeviceResolverTest {
	
	@Test
	public void resolve() {
		AppleDeviceResolver resolver = new AppleDeviceResolver();
		Device device = resolver.resolveDevice(new TestDeviceRequest("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/7D11"));
		assertNotNull(device);
		assertTrue(device.isMobileBrowser());
		assertTrue(device.isApple());
	}
	
	@Test
	public void noResolve() {
		AppleDeviceResolver resolver = new AppleDeviceResolver();
		Device device = resolver.resolveDevice(new TestDeviceRequest());
		assertNull(device);
	}
	
}
