package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.ServletException;
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
		SitePreferenceHandler sitePreferenceHandler = new StandardSitePreferenceHandler(sitePreferenceRepository);
		siteSwitcher = new SiteSwitcherRequestFilter(normalSiteUrlFactory, mobileSiteUrlFactory, sitePreferenceHandler);
	}

	@Test
	public void mobileDeviceNormalSiteNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		siteSwitcher.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		siteSwitcher.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void missingSwitcherModeInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		try {
			filter.init(new MockFilterConfig());
		} catch (ServletException ex) {
			assertEquals("switcherMode init parameter not found", ex.getLocalizedMessage());
		}
	}

	@Test
	public void invalidSwitcherModeInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "xyz");
		try {
			filter.init(filterConfig);
		} catch (ServletException ex) {
			assertEquals("Invalid switcherMode init parameter", ex.getLocalizedMessage());
		}
	}

	@Test
	public void missingServerNameInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		try {
			filter.init(filterConfig);
		} catch (ServletException ex) {
			assertEquals("serverName init parameter not found", ex.getLocalizedMessage());
		}
	}

	@Test
	public void missingMobilePathInitParameter() throws Exception {
		SiteSwitcherRequestFilter filter = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		try {
			filter.init(filterConfig);
		} catch (ServletException ex) {
			assertEquals("mobilePath init parameter not found", ex.getLocalizedMessage());
		}
	}

	@Test
	public void mDotNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "normal");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "normal");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "mDot");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "normal");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		mDot.doFilter(request, response, filterChain);
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "normal");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter mDot = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "dotMobi");
		filterConfig.addInitParameter("serverName", "app.com");
		mDot.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		mDot.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}

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
	public void urlPathNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "normal");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
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
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
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
	public void urlPathMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "normal");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "normal");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherRequestFilter urlPath = new SiteSwitcherRequestFilter();
		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter("switcherMode", "urlPath");
		filterConfig.addInitParameter("mobilePath", "/mob");
		filterConfig.addInitParameter("rootPath", "/app");
		urlPath.init(filterConfig);
		request.addParameter("site_preference", "mobile");
		urlPath.doFilter(request, response, filterChain);
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

}