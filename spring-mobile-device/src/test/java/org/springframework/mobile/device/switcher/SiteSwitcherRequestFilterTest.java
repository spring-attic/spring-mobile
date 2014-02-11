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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriUtils;

public class SiteSwitcherRequestFilterTest {

	private SiteSwitcherRequestFilter siteSwitcher;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private MockFilterChain filterChain = new MockFilterChain();

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
				return request.getServerName().equals("app.com/t");
			}

			public String createSiteUrl(HttpServletRequest request) {
				return "http://app.com/t";
			}
		};
		SitePreferenceHandler sitePreferenceHandler = new StandardSitePreferenceHandler(sitePreferenceRepository);
		siteSwitcher = new SiteSwitcherRequestFilter(normalSiteUrlFactory, mobileSiteUrlFactory, tabletSiteUrlFactory,
				sitePreferenceHandler);
		siteSwitcher.initFilterBean();
	}

	@Test
	public void normalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://app.com/t", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://app.com/t", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://app.com/t", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void tabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		sitePreferenceRepository.setSitePreference(SitePreference.TABLET);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://app.com/t", response.getRedirectedUrl());
	}

	@Test(expected = ServletException.class)
	public void invalidSwitcherModeInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "xyz");
		try {
			filter.init(filterConfig);
		} catch (ServletException ex) {
			assertEquals("Invalid switcherMode init parameter", ex.getLocalizedMessage());
			throw ex;
		}
	}

	@Test(expected = ServletException.class)
	public void missingServerNameInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		try {
			filter.init(filterConfig);
		} catch (ServletException ex) {
			assertEquals("serverName init parameter not found", ex.getLocalizedMessage());
			throw ex;
		}
	}

	//	@Test(expected=ServletException.class)
	//	public void missingMobilePathInitParameter() throws Exception {
	//		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
	//		MockFilterConfig filterConfig = new MockFilterConfig();
	//		filterConfig.addInitParameter("switcherMode", "urlPath");
	//		filter.init(filterConfig);
	//	}

	@Test
	public void mDotNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		filterTest("mDot");
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "normal");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "mobile");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "tablet");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		filterTest("mDot");
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}
	
	@Test
	public void mDotMobileDeviceNoPreferenceQueryString() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setQueryString(UriUtils.encodeQuery("city=Z\u00fcrich", "UTF-8"));
		filterTest("mDot");
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com?city=Z%C3%BCrich", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "normal");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "mobile");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "tablet");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		filterTest("mDot");
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "normal");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "mobile");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "tablet");
		filterTest("mDot");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotTabletDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	// dotMobi tests

	@Test
	public void dotMobiNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		filterTest("dotMobi");
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "normal");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "mobile");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.addParameter("site_preference", "tablet");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		filterTest("dotMobi");
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "normal");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "mobile");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.addParameter("site_preference", "tablet");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		filterTest("dotMobi");
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "normal");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "mobile");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.addParameter("site_preference", "tablet");
		filterTest("dotMobi");
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiTabletDeviceNoPreferenceTabletIsMobile() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		filterConfig.addInitParameter("tabletIsMobile", "true");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	private void filterTest(String switcherMode) throws Exception {
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", switcherMode);
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
	}

	// urlPath Tests

	@Test
	public void urlPathNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("tabletPath", "/tab");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("tabletPath", "/tab");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreference() throws Exception {
		urlPathDeviceNormalPreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathMobileDeviceNormalPreference() throws Exception {
		urlPathDeviceNormalPreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathTabletDeviceNormalPreference() throws Exception {
		urlPathDeviceNormalPreference(DeviceType.TABLET);
	}

	private void urlPathDeviceNormalPreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("tabletPath", "/tab");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "normal");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreference() throws Exception {
		urlPathRootPathDeviceNormalPreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreference() throws Exception {
		urlPathRootPathDeviceNormalPreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathRootPathTabletDeviceNormalPreference() throws Exception {
		urlPathRootPathDeviceNormalPreference(DeviceType.TABLET);
	}

	private void urlPathRootPathDeviceNormalPreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("tablet", "/tab");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "normal");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreference() throws Exception {
		urlPathDeviceMobilePreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathMobileDeviceMobilePreference() throws Exception {
		urlPathDeviceMobilePreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathTabletDeviceMobilePreference() throws Exception {
		urlPathDeviceMobilePreference(DeviceType.TABLET);
	}

	private void urlPathDeviceMobilePreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("tabletPath", "/tab");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreference() throws Exception {
		urlPathRootPathDeviceMobilePreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreference() throws Exception {
		urlPathRootPathDeviceMobilePreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathRootPathTabletDeviceMobilePreference() throws Exception {
		urlPathRootPathDeviceMobilePreference(DeviceType.TABLET);
	}

	private void urlPathRootPathDeviceMobilePreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("tabletPath", "/tab");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceTabletPreference() throws Exception {
		urlPathDeviceTabletPreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathMobileDeviceTabletPreference() throws Exception {
		urlPathDeviceTabletPreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathTabletDeviceTabletPreference() throws Exception {
		urlPathDeviceTabletPreference(DeviceType.TABLET);
	}

	private void urlPathDeviceTabletPreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("tabletPath", "/tab");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "tablet");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceTabletPreference() throws Exception {
		urlPathRootPathDeviceTabletPreference(DeviceType.NORMAL);
	}

	@Test
	public void urlPathRootPathMobileDeviceTabletPreference() throws Exception {
		urlPathRootPathDeviceTabletPreference(DeviceType.MOBILE);
	}

	@Test
	public void urlPathRootPathTabletDeviceTabletPreference() throws Exception {
		urlPathRootPathDeviceTabletPreference(DeviceType.TABLET);
	}

	private void urlPathRootPathDeviceTabletPreference(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("tabletPath", "/tab");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "tablet");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreferenceNoConfig() throws Exception {
		urlPathDeviceNoPreferenceNoConfig(DeviceType.NORMAL);
	}

	@Test
	public void urlPathMobileDeviceNoPreferenceNoConfig() throws Exception {
		urlPathDeviceNoPreferenceNoConfig(DeviceType.MOBILE);
	}

	@Test
	public void urlPathTabletDeviceNoPreferenceNoConfig() throws Exception {
		urlPathDeviceNoPreferenceNoConfig(DeviceType.TABLET);
	}

	private void urlPathDeviceNoPreferenceNoConfig(DeviceType deviceType) throws Exception {
		device.setDeviceType(deviceType);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		urlPath.init(filterConfig);
		urlPath.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void doFilterWithoutRedirect() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		Servlet servlet = createMock(Servlet.class);
		servlet.service(this.request, this.response);
		replay(servlet);
		MockFilter otherFilter = new MockFilter(servlet);
		MockFilterChain chain = new MockFilterChain(servlet, siteSwitcher, otherFilter);
		chain.doFilter(this.request, this.response);
		assertNull(response.getRedirectedUrl());
		assertTrue(otherFilter.invoked);
		verify(servlet);
	}

	@Test
	public void doFilterWithRedirect() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		Servlet servlet = createMock(Servlet.class);
		replay(servlet);
		MockFilter otherFilter = new MockFilter(servlet);
		MockFilterChain chain = new MockFilterChain(servlet, siteSwitcher, otherFilter);
		chain.doFilter(this.request, this.response);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
		assertFalse(otherFilter.invoked);
		verify(servlet);
	}

	private static class MockFilter implements Filter {

		private final Servlet servlet;

		private boolean invoked;

		public MockFilter(Servlet servlet) {
			this.servlet = servlet;
		}

		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
				ServletException {

			this.invoked = true;

			if (this.servlet != null) {
				this.servlet.service(request, response);
			} else {
				chain.doFilter(request, response);
			}
		}

		public void init(FilterConfig filterConfig) throws ServletException {
		}

		public void destroy() {
		}
	}

}
