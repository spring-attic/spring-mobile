package org.springframework.mobile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ChainedDeviceResolverTest {
	
	private ChainedDeviceResolver resolver = new ChainedDeviceResolver();
	
	@Test
	public void resolve() {
		resolver.add(new AppleDeviceResolver());
		Device device = resolver.resolveDeviceForUserAgent("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/7D11");
		assertNotNull(device);
		assertTrue(device.isMobileBrowser());
		assertTrue(device.isApple());		
	}

	@Test
	public void resolveDefault() {
		resolver.add(new AppleDeviceResolver());
		Device device = resolver.resolveDeviceForUserAgent("test");
		assertNotNull(device);
		assertFalse(device.isMobileBrowser());
		assertFalse(device.isApple());		
	}

	@Test
	public void resolveDefaultNone() {
		Device device = resolver.resolveDeviceForUserAgent("test");
		assertNotNull(device);
		assertFalse(device.isMobileBrowser());
		assertFalse(device.isApple());		
	}
	

}
