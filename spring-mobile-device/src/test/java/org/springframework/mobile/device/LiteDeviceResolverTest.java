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
		request.addHeader("User-Agent", UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void acceptHeader() {
		request.addHeader(
				"Accept",
				"application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void userAgentKeyword() {
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void operaMini() {
		request.addHeader("X-OperaMini-Phone-UA",
				"X-OperaMini-Phone-UA: SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void notMobile() {
		request.addHeader("User-Agent", UserAgent.FireFox3_6_Mac10_6);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
	}

	@Test
	public void notMobileNoHeaders() {
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
	}

	// iOS

	@Test
	public void iPad() {
		request.addHeader("User-Agent", UserAgent.iPad_iOS3_2);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isTablet());
	}
	
	// Android
	@Test
	public void isGoogleNexusOne() {
		request.addHeader("User-Agent", UserAgent.GoogleNexusOne_Android2_2);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}
	
	@Test
	public void isMotorolaXoom() {
		request.addHeader("User-Agent", UserAgent.MotorolaXoom_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isTablet());
	}
	
	// HP
	@Test
	public void isHpTouchPad() {
		request.addHeader("User-Agent", UserAgent.HPTouchPad_webOS3);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isTablet());
	}
	
	// BlackBerry
	@Test
	public void isBlackBerryPlayBook() {
		request.addHeader("User-Agent", UserAgent.BlackBerryPlaybook);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isTablet());
	}
	
	@Test
	public void isBlackBerry9850() {
		request.addHeader("User-Agent", UserAgent.BlackBerry9850_OS7);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	// Palm

	@Test
	public void palmCentro() {
		request.addHeader("User-Agent", UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void palmPre() {
		request.addHeader("User-Agent", UserAgent.PalmPre_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void palmPre2() {
		request.addHeader("User-Agent", UserAgent.PalmPre2_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void hpPre3() {
		request.addHeader("User-Agent", UserAgent.HPPre3_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

	@Test
	public void palmPixi() {
		request.addHeader("User-Agent", UserAgent.PalmPixi_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
	}

}
