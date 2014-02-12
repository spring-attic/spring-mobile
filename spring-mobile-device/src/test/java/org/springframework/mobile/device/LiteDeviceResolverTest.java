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

	private static final String MOBILE_TO_STRING = "[LiteDevice type=MOBILE]";

	private static final String TABLET_TO_STRING = "[LiteDevice type=TABLET]";

	private static final String NORMAL_TO_STRING = "[LiteDevice type=NORMAL]";

	private LiteDeviceResolver resolver = new LiteDeviceResolver();

	private MockMobileRequest request = new MockMobileRequest();

	@Before
	public void setUp() {
		resolver.getNormalUserAgentKeywords().clear();
	}

	@Test
	public void wapProfileHeader() {
		request.setWapProfileHeader(WapProfile.NOKIA3650_STRING);
		assertMobile(request);
	}

	@Test
	public void profileHeader() {
		request.setProfileHeader(WapProfile.NOKIA3650_STRING);
		assertMobile(request);
	}

	@Test
	public void userAgentHeaderPrefix() {
		request.setUserAgentHeader(UserAgent.PALM_CENTRO_STRING);
		assertMobile(request);
	}

	@Test
	public void acceptHeader() {
		request.addHeader(
				"Accept",
				"application/vnd.wap.wmlscriptc, text/vnd.wap.wml, application/vnd.wap.xhtml+xml, application/xhtml+xml, text/html, multipart/mixed, */*");
		assertMobile(request);
	}

	@Test
	public void userAgentKeyword() {
		request.setUserAgentHeader(UserAgent.IPHONE_IOS5_STRING);
		assertMobile(request);
	}

	@Test
	public void operaMini() {
		request.addHeader(
				"X-OperaMini-Phone-UA",
				"X-OperaMini-Phone-UA: SonyEricssonK750i/R1AA Browser/SEMC-Browser/4.2 Profile/MIDP-2.0 Configuration/CLDC-1.1");
		assertMobile(request);
	}

	@Test
	public void notMobileNoHeaders() {
		assertNormal(request);
	}

	@Test
	public void normalDeviceMobileOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.setUserAgentHeader(UserAgent.IPHONE_IOS5_STRING);
		assertNormal(request);
	}

	@Test
	public void normalDeviceTabletOverride() {
		String[] normalDevices = new String[] { "ipad" };
		resolver.getNormalUserAgentKeywords().addAll(Arrays.asList(normalDevices));
		request.setUserAgentHeader(UserAgent.IPAD_IOS5_STRING);
		assertNormal(request);
	}

	@Test
	public void constructorNormalDeviceOverride() {
		String[] normalDevices = new String[] { "android", "iphone" };
		LiteDeviceResolver resolver2 = new LiteDeviceResolver(
				Arrays.asList(normalDevices));
		request.setUserAgentHeader(UserAgent.IPHONE_IOS5_STRING);
		Device device = resolver2.resolveDevice(request);
		assertTrue(device.isNormal());
		assertFalse(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	// Normal device User-Agent tests

	@Test
	public void osX_10_7_3_Safari5_1_5() {
		request.setUserAgentHeader(UserAgent.OSX_10_7_3_SAFARI5_1_5_STRING);
		Device device = resolver.resolveDevice(request);
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	@Test
	public void osX_10_6_FireFox3_6() {
		request.setUserAgentHeader(UserAgent.OSX_10_6_FIREFOX3_6_STRING);
		assertNormal(request);
	}

	@Test
	public void windowsXP_IE8() {
		request.setUserAgentHeader(UserAgent.WINDOWSXP_IE8_STRING);
		assertNormal(request);
	}

	@Test
	public void windowsRT_IE10() {
		request.setUserAgentHeader(UserAgent.WINDOWSRT_IE10_STRING);
		assertNormal(request);
	}

	@Test
	public void windowsRT_IE10_touch() {
		request.setUserAgentHeader(UserAgent.WINDOWSRT_IE10_TOUCH_STRING);
		assertNormal(request);
	}

	@Test
	public void windows8_1_IE11() {
		request.setUserAgentHeader(UserAgent.WINDOWS8_1_IE_11_STRING);
		assertNormal(request);
	}

	@Test
	public void windows8_1_IE11_compatibility() {
		request.setUserAgentHeader(UserAgent.WINDOWS8_1_IE11_COMPATIBILITY_STRING);
		assertNormal(request);
	}

	// Mobile device User-Agent tests

	@Test
	public void iPodTouch() {
		request.setUserAgentHeader(UserAgent.IPODTOUCH_IOS1_1_3_STRING);
		assertMobile(request);
	}

	@Test
	public void iPhone_iOS5() {
		request.setUserAgentHeader(UserAgent.IPHONE_IOS5_STRING);
		assertMobile(request);
	}

	@Test
	public void iPhone_iOS6() {
		request.setUserAgentHeader(UserAgent.IPHONE_IOS6_STRING);
		assertMobile(request);
	}

	@Test
	public void iPhone_iOS7() {
		request.setUserAgentHeader(UserAgent.IPHONE_IOS7_STRING);
		assertMobile(request);
	}

	@Test
	public void googleNexusOne() {
		request.setUserAgentHeader(UserAgent.GOOGLE_NEXUSONE_ANDROID2_1_STRING);
		assertMobile(request);
	}

	@Test
	public void googleNexusS() {
		request.setUserAgentHeader(UserAgent.GOOGLE_NEXUSS_ANDROID2_3_STRING);
		assertMobile(request);
	}

	@Test
	public void samsungGalaxyNexus() {
		request.setUserAgentHeader(UserAgent.SAMSUNG_GALAXYNEXUS_ANDROID4_STRING);
		assertMobile(request);
	}

	@Test
	public void blackBerry9850() {
		request.setUserAgentHeader(UserAgent.BLACKBERRY9850_OS7_STRING);
		assertMobile(request);
	}

	@Test
	public void blackBerry9800() {
		request.setUserAgentHeader(UserAgent.BLACKBERRY9800_OS5_STRING);
		assertMobile(request);
	}

	@Test
	public void blackBerryTouch() {
		request.setUserAgentHeader(UserAgent.BLACKBERRY_TOUCH_OS10_STRING);
		assertMobile(request);
	}

	@Test
	public void palmCentro() {
		request.setUserAgentHeader(UserAgent.PALM_CENTRO_STRING);
		assertMobile(request);
	}

	@Test
	public void palmPre() {
		request.setUserAgentHeader(UserAgent.PALM_PRE_WEBOS1_4_STRING);
		assertMobile(request);
	}

	@Test
	public void palmPre2() {
		request.setUserAgentHeader(UserAgent.PALM_PRE2_WEBOS2_1_STRING);
		assertMobile(request);
	}

	@Test
	public void hpPre3() {
		request.setUserAgentHeader(UserAgent.HP_PRE3_WEBOS2_1_STRING);
		assertMobile(request);
	}

	@Test
	public void palmPixi() {
		request.setUserAgentHeader(UserAgent.PALM_PIXI_WEBOS1_4_STRING);
		assertMobile(request);
	}

	@Test
	public void kindleFireGen1SilkMobile() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE1_SILK_MOBILE_STRING);
		assertMobile(request);
	}

	@Test
	public void kindleFireGen2SilkMobile() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE2_SILK_MOBILE_STRING);
		assertMobile(request);
	}

	@Test
	public void firefoxOSMobile() {
		request.setUserAgentHeader(UserAgent.FIREFOXOS_MOBILE_STRING);
		assertMobile(request);
	}

	@Test
	public void asusGalaxy6() {
		request.setUserAgentHeader(UserAgent.ASUS_GALAXY6_WINDOWSPHONE7_STRING);
		assertMobile(request);
	}

	@Test
	public void samsungFocus() {
		request.setUserAgentHeader(UserAgent.SAMSUNG_FOCUS_WINDOWSPHONE7_5_STRING);
		assertMobile(request);
	}

	@Test
	public void nokiaLumia920_mobile() {
		request.setUserAgentHeader(UserAgent.NOKIA_LUMIA920_WINDOWSPHONE8_MOBILE_STRING);
		assertMobile(request);
	}

	@Test
	public void nokiaLumia920_desktop() {
		request.setUserAgentHeader(UserAgent.NOKIA_LUMIA920_WINDOWSPHONE8_DESKTOP_STRING);
		assertNormal(request);
	}

	// Tablet device User-Agent tests

	@Test
	public void iPad_iOS3_2() {
		request.setUserAgentHeader(UserAgent.IPAD_IOS3_2_STRING);
		assertTablet(request);
	}

	@Test
	public void iPad_iOS4_3_5() {
		request.setUserAgentHeader(UserAgent.IPAD_IOS4_3_5_STRING);
		assertTablet(request);
	}

	@Test
	public void iPad_iOS5() {
		request.setUserAgentHeader(UserAgent.IPAD_IOS5_STRING);
		assertTablet(request);
	}

	@Test
	public void iPad_iOS6() {
		request.setUserAgentHeader(UserAgent.IPAD_IOS6_STRING);
		assertTablet(request);
	}

	@Test
	public void iPad_iOS7() {
		request.setUserAgentHeader(UserAgent.IPAD_IOS7_STRING);
		assertTablet(request);
	}

	@Test
	public void samsungGalaxyTab() {
		request.setWapProfileHeader(WapProfile.SAMSUNG_GALAXYTAB_GT_P1000_STRING);
		request.setUserAgentHeader(UserAgent.SAMSUNG_GALAXYTAB_GT_P1000_ANDROID2_2_STRING);
		// Device reports standard Android user agent, indicating it as a mobile device
		assertMobile(request);
	}

	@Test
	public void samsungGalaxyTab10_1V() {
		request.setUserAgentHeader(UserAgent.SAMSUNG_GALAXYTAB10_1V_GT_P7100_ANDROID3_0_STRING);
		assertTablet(request);
	}

	@Test
	public void samsungGalaxyTab10_1() {
		request.setWapProfileHeader(WapProfile.SAMSUNG_GALAXYTAB10_1_GT_P7510_STRING);
		request.setUserAgentHeader(UserAgent.SAMSUNG_GALAXYTAB10_1_GT_P7510_ANDROID3_1_STRING);
		assertTablet(request);
	}

	@Test
	public void samsungGalaxyTab8_9() {
		request.setWapProfileHeader(WapProfile.SAMSUNG_GALAXYTAB8_9_GT_P7310_STRING);
		request.setUserAgentHeader(UserAgent.SAMSUNG_GALAXYTAB8_9_GT_P7310_ANDROID3_1_STRING);
		assertTablet(request);
	}

	@Test
	public void motorolaXoom() {
		request.setUserAgentHeader(UserAgent.MOTOROLA_XOOM_ANDROID3_1_STRING);
		assertTablet(request);
	}

	@Test
	public void blackBerryPlaybook() {
		request.setUserAgentHeader(UserAgent.BLACKBERRY_PLAYBOOK_STRING);
		assertTablet(request);
	}

	@Test
	public void hpTouchPad() {
		request.setUserAgentHeader(UserAgent.HPTOUCHPAD_WEBOS3_STRING);
		assertTablet(request);
	}

	@Test
	public void kindle1() {
		request.setUserAgentHeader(UserAgent.KINDLE1_STRING);
		assertTablet(request);
	}

	@Test
	public void kindle2() {
		request.setUserAgentHeader(UserAgent.KINDLE2_STRING);
		assertTablet(request);
	}

	@Test
	public void kindle2_5() {
		request.setUserAgentHeader(UserAgent.KINDLE2_5_STRING);
		assertTablet(request);
	}

	@Test
	public void kindle3() {
		request.setUserAgentHeader(UserAgent.KINDLE3_STRING);
		assertTablet(request);
	}

	@Test
	public void kindleFireGen1SilkDesktop() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE1_SILK_DESKTOP_STRING);
		assertTablet(request);
	}

	@Test
	public void kindleFireGen1AndroidWebView() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE2_ANDROID_WEBVIEW_STRING);
		assertTablet(request);
	}

	@Test
	public void kindleFireGen2SilkDesktop() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE2_SILK_DESKTOP_STRING);
		assertTablet(request);
	}

	@Test
	public void kindleFireGen2AndroidWebView() {
		request.setUserAgentHeader(UserAgent.KINDLE_FIRE2_ANDROID_WEBVIEW_STRING);
		assertTablet(request);
	}

	// helpers

	private void assertNormal(MockMobileRequest request) {
		Device device = resolver.resolveDevice(request);
		assertTrue(device.isNormal());
		assertFalse(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), NORMAL_TO_STRING);
	}

	private void assertMobile(MockMobileRequest request) {
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isNormal());
		assertTrue(device.isMobile());
		assertFalse(device.isTablet());
		assertEquals(device.toString(), MOBILE_TO_STRING);
	}

	private void assertTablet(MockMobileRequest request) {
		Device device = resolver.resolveDevice(request);
		assertFalse(device.isNormal());
		assertFalse(device.isMobile());
		assertTrue(device.isTablet());
		assertEquals(device.toString(), TABLET_TO_STRING);
	}

	private static class MockMobileRequest extends MockHttpServletRequest {

		private static final String USER_AGENT = "User-Agent";

		private static final String X_WAP_PROFILE = "x-wap-profile";

		private static final String PROFILE = "Profile";

		public void setUserAgentHeader(String value) {
			this.addHeader(USER_AGENT, value);
		}

		public void setWapProfileHeader(String value) {
			this.addHeader(X_WAP_PROFILE, value);
		}

		public void setProfileHeader(String value) {
			this.addHeader(PROFILE, value);
		}

	}

}
