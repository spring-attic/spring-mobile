/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mobile.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class LiteDeviceResolverTest {

	private static final String USER_AGENT = "User-Agent";

	private static final String X_WAP_PROFILE = "x-wap-profile";

	private static final String PROFILE = "Profile";

	private static final String MOBILE_TO_STRING = "[LiteDevice type=MOBILE]";

	private static final String TABLET_TO_STRING = "[LiteDevice type=TABLET]";

	private static final String NORMAL_TO_STRING = "[LiteDevice type=NORMAL]";

	private LiteDeviceResolver resolver = new LiteDeviceResolver();

	private MockHttpServletRequest request = new MockHttpServletRequest();

	@Before
	public void setUp() {
		resolver.getNormalUserAgentKeywords().clear();
	}

	@Test
	public void wapProfileHeader() {
		request.addHeader(X_WAP_PROFILE, WapProfile.Nokia3650);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void profileHeader() {
		request.addHeader(PROFILE, WapProfile.Nokia3650);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void userAgentHeaderPrefix() {
		request.addHeader(USER_AGENT, UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void acceptHeader() {
		request.addHeader(
				"Accept",
				"application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*");
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void userAgentKeyword() {
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void operaMini() {
		request.addHeader("X-OperaMini-Phone-UA",
				"X-OperaMini-Phone-UA: SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void notMobileNoHeaders() {
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void normalDeviceMobileOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void normalDeviceTabletOverride() {
		String[] normalDevices = new String[] { "ipad" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS5);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void constructorNormalDeviceOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		LiteDeviceResolver resolver2 = new LiteDeviceResolver(Arrays.asList(normalDevices));
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS5);
		Device device = resolver2.resolveDevice(request);
		assertNormal(device);
	}

	// Normal device User-Agent tests

	@Test
	public void osX_10_7_3_Safari5_1_5() {
		request.addHeader(USER_AGENT, UserAgent.OSX_10_7_3_Safari5_1_5);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	@Test
	public void osX_10_6_FireFox3_6() {
		request.addHeader(USER_AGENT, UserAgent.OSX_10_6_FireFox3_6);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsXP_IE8() {
		request.addHeader(USER_AGENT, UserAgent.WindowsXP_IE8);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsRT_IE10() {
		request.addHeader(USER_AGENT, UserAgent.WindowsRT_IE10);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsRT_IE10_touch() {
		request.addHeader(USER_AGENT, UserAgent.WindowsRT_IE10_touch);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windows8_1_IE11() {
		request.addHeader(USER_AGENT, UserAgent.Windows8_1_IE11);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windows8_1_IE11_compatibility() {
		request.addHeader(USER_AGENT, UserAgent.Windows8_1_IE11_compatibility);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	// Mobile device User-Agent tests

	@Test
	public void iPodTouch() {
		request.addHeader(USER_AGENT, UserAgent.iPodTouch_iOS1_1_3);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS5() {
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS5);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS6() {
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS6);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS7() {
		request.addHeader(USER_AGENT, UserAgent.iPhone_iOS7);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void googleNexusOne() {
		request.addHeader(USER_AGENT, UserAgent.GoogleNexusOne_Android2_1);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void googleNexusS() {
		request.addHeader(USER_AGENT, UserAgent.GoogleNexusS_Android2_3);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void samsungGalaxyNexus() {
		request.addHeader(USER_AGENT, UserAgent.SamsungGalaxyNexus_Android4);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerry9850() {
		request.addHeader(USER_AGENT, UserAgent.BlackBerry9850_OS7);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerry9800() {
		request.addHeader(USER_AGENT, UserAgent.BlackBerry9800_OS5);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerryTouch() {
		request.addHeader(USER_AGENT, UserAgent.BlackBerryTouch_OS10);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmCentro() {
		request.addHeader(USER_AGENT, UserAgent.PalmCentro);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPre() {
		request.addHeader(USER_AGENT, UserAgent.PalmPre_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPre2() {
		request.addHeader(USER_AGENT, UserAgent.PalmPre2_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void hpPre3() {
		request.addHeader(USER_AGENT, UserAgent.HPPre3_webOS2_1);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPixi() {
		request.addHeader(USER_AGENT, UserAgent.PalmPixi_webOS1_4);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void kindleFireGen1SilkMobile() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire1_Silk_mobile);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void kindleFireGen2SilkMobile() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire2_Silk_mobile);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void firefoxOSMobile() {
		request.addHeader(USER_AGENT, UserAgent.FirefoxOS_mobile);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void asusGalaxy6() {
		request.addHeader(USER_AGENT, UserAgent.AsusGalaxy6_WindowsPhone7);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void samsungFocus() {
		request.addHeader(USER_AGENT, UserAgent.SamsungFocus_WindowsPhone7_5);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void nokiaLumia920_mobile() {
		request.addHeader(USER_AGENT, UserAgent.NokiaLumia920_WindowsPhone8_mobile);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void nokiaLumia920_desktop() {
		request.addHeader(USER_AGENT, UserAgent.NokiaLumia920_WindowsPhone8_desktop);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	// Tablet device User-Agent tests

	@Test
	public void iPad_iOS3_2() {
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS3_2);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS4_3_5() {
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS4_3_5);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS5() {
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS5);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS6() {
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS6);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS7() {
		request.addHeader(USER_AGENT, UserAgent.iPad_iOS7);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SamsungGalaxyTab_GT_P1000);
		request.addHeader(USER_AGENT, UserAgent.SamsungGalaxyTab_GT_P1000_Android2_2);
		Device device = resolver.resolveDevice(request);
		// Device reports standard Android user agent, indicating it as a mobile device
		assertMobile(device);
	}

	@Test
	public void samsungGalaxyTab10_1V() {
		request.addHeader(USER_AGENT, UserAgent.SamsungGalaxyTab10_1V_GT_P7100_Android3_0);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab10_1() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SamsungGalaxyTab10_1_GT_P7510);
		request.addHeader(USER_AGENT, UserAgent.SamsungGalaxyTab10_1_GT_P7510_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab8_9() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SamsungGalaxyTab8_9_GT_P7310);
		request.addHeader(USER_AGENT, UserAgent.SamsungGalaxyTab8_9_GT_P7310_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void motorolaXoom() {
		request.addHeader(USER_AGENT, UserAgent.MotorolaXoom_Android3_1);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void blackBerryPlaybook() {
		request.addHeader(USER_AGENT, UserAgent.BlackBerryPlaybook);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void hpTouchPad() {
		request.addHeader(USER_AGENT, UserAgent.HPTouchPad_webOS3);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle1() {
		request.addHeader(USER_AGENT, UserAgent.Kindle1);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle2() {
		request.addHeader(USER_AGENT, UserAgent.Kindle2);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle2_5() {
		request.addHeader(USER_AGENT, UserAgent.Kindle2_5);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle3() {
		request.addHeader(USER_AGENT, UserAgent.Kindle3);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen1SilkDesktop() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire1_Silk_desktop);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen1AndroidWebView() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire2_Android_webview);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen2SilkDesktop() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire2_Silk_desktop);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen2AndroidWebView() {
		request.addHeader(USER_AGENT, UserAgent.KindleFire2_Android_webview);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	// helpers

	private static void assertNormal(Device device) {
		assertTrue(device.isNormal());
		assertFalse(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	private static void assertMobile(Device device) {
		assertFalse(device.isNormal());
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), MOBILE_TO_STRING);
	}

	private static void assertTablet(Device device) {
		assertFalse(device.isNormal());
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
		assertEquals(device.toString(), TABLET_TO_STRING);
	}

}
