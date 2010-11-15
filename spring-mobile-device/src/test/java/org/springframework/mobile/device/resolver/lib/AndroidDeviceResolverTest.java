package org.springframework.mobile.device.resolver.lib;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.resolver.StubDeviceRequest;
import org.springframework.mobile.device.resolver.lib.AndroidDeviceResolver;

public class AndroidDeviceResolverTest {
	
	@Test
	public void resolve() {
		AndroidDeviceResolver resolver = new AndroidDeviceResolver();
		Device device = resolver.resolveDevice(new StubDeviceRequest("Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"));
		assertNotNull(device);
		assertTrue(device.isMobile());
	}
	
	@Test
	public void noResolve() {
		AndroidDeviceResolver resolver = new AndroidDeviceResolver();
		Device device = resolver.resolveDevice(new StubDeviceRequest());
		assertNull(device);
	}
	
}
