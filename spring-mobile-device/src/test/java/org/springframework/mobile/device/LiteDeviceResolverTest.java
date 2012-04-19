package org.springframework.mobile.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class LiteDeviceResolverTest {

	private LiteDeviceResolver resolver = new LiteDeviceResolver();

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp() {
		resolver.getNormalUserAgentKeywords().clear();
	}

	@Test
	public void wapProfileHeader() {
		request.addHeader("x-wap-profile", "http://nds.nokia.com/uaprof/N3650r100.xml");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void profileHeader() {
		request.addHeader("Profile", "http://nds.nokia.com/uaprof/N3650r100.xml");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void userAgentHeaderPrefix() {
		request.addHeader("User-Agent", UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void acceptHeader() {
		request.addHeader(
				"Accept",
				"application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void userAgentKeyword() {
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void operaMini() {
		request.addHeader("X-OperaMini-Phone-UA",
				"X-OperaMini-Phone-UA: SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void notMobile() {
		request.addHeader("User-Agent", UserAgent.FireFox3_6_Mac10_6);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void notMobileNoHeaders() {
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void mobileToString() {
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), MOBILE_TO_STRING);
	}
	
	@Test
	public void tabletToString() {
		request.addHeader("User-Agent", UserAgent.MotorolaXoom_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), TABLET_TO_STRING);
	}
	
	@Test
	public void normalToString() {
		request.addHeader("User-Agent", UserAgent.Safari5_1_5_Mac10_7_3);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}
	
	@Test
	public void normalDeviceMobileOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
	}
	
	@Test
	public void normalDeviceTabletOverride() {
		String[] normalDevices = new String[] { "ipad" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.addHeader("User-Agent", UserAgent.iPad_iOS5);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isTablet());
	}
	
	@Test
	public void constructor() {
		String[] normalDevices = new String[] { "android", "iphone" };
		LiteDeviceResolver resolver2  = new LiteDeviceResolver(Arrays.asList(normalDevices));
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver2.resolveDevice(request);
		assertFalse(device.isMobile());
	}
	
	// Mobile device User-Agent tests

	@Test
	public void iPodTouch() {
		request.addHeader("User-Agent", UserAgent.iPodTouch_iOS1_1_3);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void iPhone() {
		request.addHeader("User-Agent", UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), MOBILE_TO_STRING);
	}

	@Test
	public void googleNexusOne() {
		request.addHeader("User-Agent", UserAgent.GoogleNexusOne_Android2_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void googleNexusS() {
		request.addHeader("User-Agent", UserAgent.GoogleNexusS_Android2_3);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void blackBerry9850() {
		request.addHeader("User-Agent", UserAgent.BlackBerry9850_OS7);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void blackBerry9800() {
		request.addHeader("User-Agent", UserAgent.BlackBerry9800_OS5);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void palmCentro() {
		request.addHeader("User-Agent", UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void palmPre() {
		request.addHeader("User-Agent", UserAgent.PalmPre_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void palmPre2() {
		request.addHeader("User-Agent", UserAgent.PalmPre2_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void hpPre3() {
		request.addHeader("User-Agent", UserAgent.HPPre3_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void palmPixi() {
		request.addHeader("User-Agent", UserAgent.PalmPixi_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	// Tablet device User-Agent tests

	@Test
	public void iPad() {
		request.addHeader("User-Agent", UserAgent.iPad_iOS3_2);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}

	@Test
	public void samsungGalaxyTab() {
		request.addHeader("User-Agent", UserAgent.SamsungGalaxyTab_Android2_2);
		Device device = resolver.resolveDevice(request);
		// Device reports standard Android user agent, indicating it as a mobile device
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
	}

	@Test
	public void samsungGalaxyTab10_1V() {
		request.addHeader("User-Agent", UserAgent.SamsungGalaxyTab10_1V_Android3_0);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}
	
	@Test
	public void samsungGalaxyTab10_1() {
		request.addHeader("User-Agent", UserAgent.SamsungGalaxyTab10_1_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}

	@Test
	public void motorolaXoom() {
		request.addHeader("User-Agent", UserAgent.MotorolaXoom_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}

	@Test
	public void blackBerryPlaybook() {
		request.addHeader("User-Agent", UserAgent.BlackBerryPlaybook);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}

	@Test
	public void hpTouchPad() {
		request.addHeader("User-Agent", UserAgent.HPTouchPad_webOS3);
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
	}

	private static final String MOBILE_TO_STRING = "[LiteDevice type=MOBILE]";
	
	private static final String TABLET_TO_STRING = "[LiteDevice type=TABLET]";
	
	private static final String NORMAL_TO_STRING = "[LiteDevice type=NORMAL]";
}
