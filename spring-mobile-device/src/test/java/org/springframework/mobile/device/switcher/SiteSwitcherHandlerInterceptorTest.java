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

package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.StubDevice;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.mobile.device.site.StubSitePreferenceRepository;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriUtils;

public class SiteSwitcherHandlerInterceptorTest {

	private SiteSwitcherHandlerInterceptor siteSwitcher;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private StubDevice device = new StubDevice();

	private StubSitePreferenceRepository sitePreferenceRepository = new StubSitePreferenceRepository();

	@Before
	public void setup() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		SiteUrlFactory normalSiteUrlFactory = new SiteUrlFactory() {
			public boolean isRequestForSite(HttpServletRequest request) {
				return request.getServerName().equals("app.com");
			}

			public String createSiteUrl(HttpServletRequest request) {
				return "http://app.com";
			}
		};
		SiteUrlFactory mobileSiteUrlFactory = new SiteUrlFactory() {
			public boolean isRequestForSite(HttpServletRequest request) {
				return request.getServerName().equals("m.app.com");
			}

			public String createSiteUrl(HttpServletRequest request) {
				return "http://m.app.com";
			}
		};
		SiteUrlFactory tabletSiteUrlFactory = new SiteUrlFactory() {
			public boolean isRequestForSite(HttpServletRequest request) {
				return request.getServerName().equals("app.com/tab");
			}

			public String createSiteUrl(HttpServletRequest request) {
				return "http://app.com/tab";
			}
		};
		SitePreferenceHandler sitePreferenceHandler = new StandardSitePreferenceHandler(sitePreferenceRepository);
		siteSwitcher = new SiteSwitcherHandlerInterceptor(normalSiteUrlFactory, mobileSiteUrlFactory,
				tabletSiteUrlFactory, sitePreferenceHandler);
	}

	@Test
	public void normalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://app.com/tab", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://app.com/tab", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://app.com/tab", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://app.com/tab", response.getRedirectedUrl());
	}

	// mDot tests

	@Test
	public void mDotNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceTabletPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreferenceQueryString() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setQueryString(UriUtils.encodeQuery("city=Z\u00fcrich", "UTF-8"));
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com?city=Z%C3%BCrich", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceTabletPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceTabletPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreferenceTabletIsMobileQueryString() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setQueryString("x=123");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com?x=123", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNormalPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNormalPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "normal");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceMobilePreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "mobile");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceMobilePreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "mobile");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceTabletPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceTabletPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("m.app.com");
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com", true);
		request.addParameter("site_preference", "tablet");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	// dotMobi tests

	@Test
	public void dotMobiNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNoPreferenceRequestMobileSiteQueryString() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("app.mobi");
		request.setQueryString("x=123");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.com?x=123", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceTabletPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreferenceQueryString() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setQueryString("x=123");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi?x=123", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNormalPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceMobilePreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "mobile");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceTabletPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreferenceTabletIsMobileQueryString() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setQueryString("x=123");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi?x=123", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNormalPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "normal");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNormalPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "normal");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceMobilePreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "mobile");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceMobilePreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "mobile");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceTabletPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "tablet");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceTabletPreferenceTabletIsMobileRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("app.mobi");
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com", true);
		request.addParameter("site_preference", "tablet");
		assertTrue(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}
	
	// standard tests

	@Test
	public void standardNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void standardNormalDeviceNoPreferenceRequestNormalSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("normal.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void standardNormalDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("mobile.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://normal.com", response.getRedirectedUrl());
	}

	@Test
	public void standardNormalDeviceNoPreferenceRequestTabletSite() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setServerName("tablet.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://normal.com", response.getRedirectedUrl());
	}

	@Test
	public void standardMobileDeviceNoPreferenceTabletIsNotMobile() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://mobile.com", response.getRedirectedUrl());
	}

	@Test
	public void standardMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://mobile.com", response.getRedirectedUrl());
	}

	@Test
	public void standardMobileDeviceNoPreferenceRequestNormalSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("normal.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://mobile.com", response.getRedirectedUrl());
	}

	@Test
	public void standardMobileDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("mobile.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void standardMobileDeviceNoPreferenceRequestTabletSite() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setServerName("tablet.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://mobile.com", response.getRedirectedUrl());
	}

	@Test
	public void standardTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://tablet.com", response.getRedirectedUrl());
	}

	@Test
	public void standardTabletDeviceNoPreferenceRequestNormalSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("normal.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://tablet.com", response.getRedirectedUrl());
	}

	@Test
	public void standardTabletDeviceNoPreferenceRequestMobileSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("mobile.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertFalse(interceptor.preHandle(request, response, null));
		assertEquals("http://tablet.com", response.getRedirectedUrl());
	}

	@Test
	public void standardTabletDeviceNoPreferenceRequestTabletSite() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setServerName("tablet.com");
		SiteSwitcherHandlerInterceptor interceptor = SiteSwitcherHandlerInterceptor.standard("normal.com", "mobile.com", "tablet.com", "." + "normal.com");
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	// urlPath tests

	@Test
	public void urlPathMobileDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceQueryString() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setQueryString("x=123");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob?x=123", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob/");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceQueryString() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setQueryString("x=123");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab?x=123", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab/");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreferenceQueryString() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setQueryString("x=123");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob?x=123", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob/");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreferenceQueryString() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setQueryString("x=123");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab?x=123", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.MOBILE);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.TABLET);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab/");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName(), response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "normal");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "mobile");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreference() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreferenceRequestMobileSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreferenceRequestMobileSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/mob/");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab/", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreferenceRequestTabletSite() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreferenceRequestTabletSiteWithSlash() throws Exception {
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		device.setDeviceType(DeviceType.NORMAL);
		request.setRequestURI("/app/tab/");
		request.addParameter("site_preference", "tablet");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

}
