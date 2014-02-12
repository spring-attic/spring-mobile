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
		request.addHeader(X_WAP_PROFILE, WapProfile.NOKIA3650_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void profileHeader() {
		request.addHeader(PROFILE, WapProfile.NOKIA3650_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void userAgentHeaderPrefix() {
		request.addHeader(USER_AGENT, UserAgent.PALM_CENTRO_STRING);
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
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void operaMini() {
		request.addHeader(
				"X-OperaMini-Phone-UA",
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
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void normalDeviceTabletOverride() {
		String[] normalDevices = new String[] { "ipad" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void constructorNormalDeviceOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		LiteDeviceResolver resolver2 = new LiteDeviceResolver(
				Arrays.asList(normalDevices));
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS5_STRING);
		Device device = resolver2.resolveDevice(request);
		assertNormal(device);
	}

	// Normal device User-Agent tests

	@Test
	public void osX_10_7_3_Safari5_1_5() {
		request.addHeader(USER_AGENT, UserAgent.OSX_10_7_3_SAFARI5_1_5_STRING);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	@Test
	public void osX_10_6_FireFox3_6() {
		request.addHeader(USER_AGENT, UserAgent.OSX_10_6_FIREFOX3_6_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsXP_IE8() {
		request.addHeader(USER_AGENT, UserAgent.WINDOWSXP_IE8_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsRT_IE10() {
		request.addHeader(USER_AGENT, UserAgent.WINDOWSRT_IE10_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windowsRT_IE10_touch() {
		request.addHeader(USER_AGENT, UserAgent.WINDOWSRT_IE10_TOUCH_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windows8_1_IE11() {
		request.addHeader(USER_AGENT, UserAgent.WINDOWS8_1_IE_11_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	@Test
	public void windows8_1_IE11_compatibility() {
		request.addHeader(USER_AGENT, UserAgent.WINDOWS8_1_IE11_COMPATIBILITY_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	// Mobile device User-Agent tests

	@Test
	public void iPodTouch() {
		request.addHeader(USER_AGENT, UserAgent.IPODTOUCH_IOS1_1_3_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS5() {
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS6() {
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS6_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void iPhone_iOS7() {
		request.addHeader(USER_AGENT, UserAgent.IPHONE_IOS7_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void googleNexusOne() {
		request.addHeader(USER_AGENT, UserAgent.GOOGLE_NEXUSONE_ANDROID2_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void googleNexusS() {
		request.addHeader(USER_AGENT, UserAgent.GOOGLE_NEXUSS_ANDROID2_3_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void samsungGalaxyNexus() {
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_GALAXYNEXUS_ANDROID4_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerry9850() {
		request.addHeader(USER_AGENT, UserAgent.BLACKBERRY9850_OS7_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerry9800() {
		request.addHeader(USER_AGENT, UserAgent.BLACKBERRY9800_OS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void blackBerryTouch() {
		request.addHeader(USER_AGENT, UserAgent.BLACKBERRY_TOUCH_OS10_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmCentro() {
		request.addHeader(USER_AGENT, UserAgent.PALM_CENTRO_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPre() {
		request.addHeader(USER_AGENT, UserAgent.PALM_PRE_WEBOS1_4_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPre2() {
		request.addHeader(USER_AGENT, UserAgent.PALM_PRE2_WEBOS2_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void hpPre3() {
		request.addHeader(USER_AGENT, UserAgent.HP_PRE3_WEBOS2_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void palmPixi() {
		request.addHeader(USER_AGENT, UserAgent.PALM_PIXI_WEBOS1_4_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void kindleFireGen1SilkMobile() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE1_SILK_MOBILE_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void kindleFireGen2SilkMobile() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE2_SILK_MOBILE_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void firefoxOSMobile() {
		request.addHeader(USER_AGENT, UserAgent.FIREFOXOS_MOBILE_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void asusGalaxy6() {
		request.addHeader(USER_AGENT, UserAgent.ASUS_GALAXY6_WINDOWSPHONE7_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void samsungFocus() {
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_FOCUS_WINDOWSPHONE7_5_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void nokiaLumia920_mobile() {
		request.addHeader(USER_AGENT, UserAgent.NOKIA_LUMIA920_WINDOWSPHONE8_MOBILE_STRING);
		Device device = resolver.resolveDevice(request);
		assertMobile(device);
	}

	@Test
	public void nokiaLumia920_desktop() {
		request.addHeader(USER_AGENT, UserAgent.NOKIA_LUMIA920_WINDOWSPHONE8_DESKTOP_STRING);
		Device device = resolver.resolveDevice(request);
		assertNormal(device);
	}

	// Tablet device User-Agent tests

	@Test
	public void iPad_iOS3_2() {
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS3_2_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS4_3_5() {
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS4_3_5_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS5() {
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS5_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS6() {
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS6_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void iPad_iOS7() {
		request.addHeader(USER_AGENT, UserAgent.IPAD_IOS7_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SAMSUNG_GALAXYTAB_GT_P1000_STRING);
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_GALAXYTAB_GT_P1000_ANDROID2_2_STRING);
		Device device = resolver.resolveDevice(request);
		// Device reports standard Android user agent, indicating it as a mobile device
		assertMobile(device);
	}

	@Test
	public void samsungGalaxyTab10_1V() {
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_GALAXYTAB10_1V_GT_P7100_ANDROID3_0_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab10_1() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SAMSUNG_GALAXYTAB10_1_GT_P7510_STRING);
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_GALAXYTAB10_1_GT_P7510_ANDROID3_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void samsungGalaxyTab8_9() {
		request.addHeader(X_WAP_PROFILE, WapProfile.SAMSUNG_GALAXYTAB8_9_GT_P7310_STRING);
		request.addHeader(USER_AGENT, UserAgent.SAMSUNG_GALAXYTAB8_9_GT_P7310_ANDROID3_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void motorolaXoom() {
		request.addHeader(USER_AGENT, UserAgent.MOTOROLA_XOOM_ANDROID3_1_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void blackBerryPlaybook() {
		request.addHeader(USER_AGENT, UserAgent.BLACKBERRY_PLAYBOOK_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void hpTouchPad() {
		request.addHeader(USER_AGENT, UserAgent.HPTOUCHPAD_WEBOS3_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle1() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE1_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle2() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE2_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle2_5() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE2_5_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindle3() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE3_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen1SilkDesktop() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE1_SILK_DESKTOP_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen1AndroidWebView() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE2_ANDROID_WEBVIEW_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen2SilkDesktop() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE2_SILK_DESKTOP_STRING);
		Device device = resolver.resolveDevice(request);
		assertTablet(device);
	}

	@Test
	public void kindleFireGen2AndroidWebView() {
		request.addHeader(USER_AGENT, UserAgent.KINDLE_FIRE2_ANDROID_WEBVIEW_STRING);
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
