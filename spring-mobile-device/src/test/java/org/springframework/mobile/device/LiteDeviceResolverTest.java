package org.springframework.mobile.device;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.mock.web.MockHttpServletRequest;

public class LiteDeviceResolverTest {
	
	private LiteDeviceResolver resolver = new LiteDeviceResolver();

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void wapProfileHeader() {
		request.addHeader("x-wap-profile", "http://nds.nokia.com/uaprof/N3650r100.xml");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}
	
	@Test
	public void profileHeader() {
		request.addHeader("Profile", "http://nds.nokia.com/uaprof/N3650r100.xml");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());		
	}

	@Test
	public void userAgentHeaderPrefix() {
		request.addHeader("User-Agent", "PalmCentro/v0001 Mozilla/4.0 (compatible; MSIE 6.0; Windows 98; PalmSource/Palm-D061; Blazer/4.5) 16;320x320");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());		
	}

	@Test
	public void acceptHeader() {
		request.addHeader("Accept", "application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());		
	}

	@Test
	public void userAgentKeyword() {
		request.addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());		
	}

	@Test
	public void operaMini() {
		request.addHeader("X-OperaMini-Phone-UA", "X-OperaMini-Phone-UA: SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());		
	}

	@Test
	public void notMobile() {
		request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12");
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());		
	}

	@Test
	public void notMobileNoHeaders() {
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());		
	}

}
