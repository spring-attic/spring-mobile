package org.springframework.mobile;

import static org.junit.Assert.*;

import org.junit.Test;

public class AndroidDeviceResolverTest {
	
	@Test
	public void resolve() {
		AndroidDeviceResolver resolver = new AndroidDeviceResolver();
		Device device = resolver
				.resolveDeviceForUserAgent("Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
		assertNotNull(device);
		assertTrue(device.isMobileBrowser());
		assertFalse(device.isApple());
	}
	
	@Test
	public void noResolve() {
		AndroidDeviceResolver resolver = new AndroidDeviceResolver();
		Device device = resolver.resolveDeviceForUserAgent("");
		assertNull(device);
	}
	
}
