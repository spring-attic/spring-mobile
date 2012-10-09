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
		SitePreferenceHandler sitePreferenceHandler = new StandardSitePreferenceHandler(sitePreferenceRepository);
		siteSwitcher = new SiteSwitcherHandlerInterceptor(normalSiteUrlFactory, mobileSiteUrlFactory,
				sitePreferenceHandler);
	}

	@Test
	public void mobileDeviceNormalSiteNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
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
	public void dotMobiMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
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
	public void urlPathMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathMobileDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		request.addParameter("site_preference", "normal");
		assertTrue(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void urlPathNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}

	@Test
	public void urlPathRootPathNormalDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/app");
		request.addParameter("site_preference", "mobile");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("MOBILE", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/mob", response.getRedirectedUrl());
	}

}