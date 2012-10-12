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
	public void mDotNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
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
	public void mDotMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}
	
	@Test
	public void mDotTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
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
	public void mDotTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
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
	public void dotMobiNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(dotMobi.preHandle(request, response, null));
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
	public void dotMobiTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertNull(response.getRedirectedUrl());
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
	public void dotMobiTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "tablet");
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
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
	
	
	// urlPath tests

	@Test
	public void urlPathMobileDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/mob", response.getRedirectedUrl());
	}
	
	@Test
	public void urlPathTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
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
	public void urlPathRootPathTabletDeviceNoPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
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
	public void urlPathTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
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
	public void urlPathRootPathTabletDeviceNormalPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
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
	public void urlPathTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}
	
	@Test
	public void urlPathMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
	}
	
	@Test
	public void urlPathTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
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
	public void urlPathRootPathTabletDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}
	
	@Test
	public void urlPathRootPathMobileDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}
	
	@Test
	public void urlPathRootPathTabletDeviceMobilePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
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
	public void urlPathNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", null);
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/tab", response.getRedirectedUrl());
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
	
	@Test
	public void urlPathRootPathNormalDeviceTabletPreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		SiteSwitcherHandlerInterceptor urlPath = SiteSwitcherHandlerInterceptor.urlPath("/mob", "/tab", "/app");
		request.addParameter("site_preference", "tablet");
		assertFalse(urlPath.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals("TABLET", response.getCookies()[0].getValue());
		assertEquals("http://" + request.getServerName() + "/app/tab", response.getRedirectedUrl());
	}

}