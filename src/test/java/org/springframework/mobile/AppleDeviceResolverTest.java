package org.springframework.mobile;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppleDeviceResolverTest {
	
	@Test
	public void resolve() {
		AppleDeviceResolver resolver = new AppleDeviceResolver();
		Device device = resolver.resolveDeviceForUserAgent("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/7D11");
		assertNotNull(device);
		assertTrue(device.isMobileBrowser());
		assertTrue(device.isApple());
	}
	
	@Test
	public void noResolve() {
		AppleDeviceResolver resolver = new AppleDeviceResolver();
		Device device = resolver.resolveDeviceForUserAgent("");
		assertNull(device);
	}
	
}
