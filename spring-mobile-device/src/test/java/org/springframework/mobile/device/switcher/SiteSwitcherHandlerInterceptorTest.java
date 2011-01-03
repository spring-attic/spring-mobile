package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.mvc.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.mvc.StubDevice;
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
		request.setAttribute(DeviceResolverHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, device);
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
		siteSwitcher = new SiteSwitcherHandlerInterceptor(normalSiteUrlFactory, mobileSiteUrlFactory, sitePreferenceHandler);
	}
	
	@Test
	public void mobileDeviceNormalSiteNoPreference() throws Exception {
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteMobilePreference() throws Exception {
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteNormalPreference() throws Exception {
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());		
	}

	@Test
	public void normalDeviceNormalSiteNoPreference() throws Exception {	
		device.setMobile(false);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());		
	}

	@Test
	public void normalDeviceNormalSiteMobilePreference() throws Exception {
		device.setMobile(false);
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNormalPreference() throws Exception {
		device.setMobile(false);
		sitePreferenceRepository.setSitePreference(SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalSiteNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mDotMobileDeviceNormalSiteNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void dotMobiMobileDeviceNormalSiteNoPreference() throws Exception {
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());		
	}

	@Test
	public void dotMobiMobileDeviceNormalSiteNormalPreference() throws Exception {
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.dotMobi("app.com");
		request.addParameter("site_preference", "normal");
		assertTrue(mDot.preHandle(request, response, null));
		assertEquals(1, response.getCookies().length);
		assertEquals(".app.com", response.getCookies()[0].getDomain());
		assertEquals("NORMAL", response.getCookies()[0].getValue());
		assertNull(response.getRedirectedUrl());
	}

}