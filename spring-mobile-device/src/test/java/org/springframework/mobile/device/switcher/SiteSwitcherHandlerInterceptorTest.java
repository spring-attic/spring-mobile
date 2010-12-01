package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.mvc.DeviceResolvingHandlerInterceptor;
import org.springframework.mobile.device.mvc.StubDevice;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceResolvingHandlerInterceptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SiteSwitcherHandlerInterceptorTest {
	
	private SiteSwitcherHandlerInterceptor siteSwitcher;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private StubDevice device;
	
	@Before
	public void setup() throws Exception {
		device = new StubDevice();
		request.setAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, device);
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
		siteSwitcher = new SiteSwitcherHandlerInterceptor(normalSiteUrlFactory, mobileSiteUrlFactory);
	}
	
	@Test
	public void mobileDeviceNormalSiteNoPreference() throws Exception {
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteMobilePreference() throws Exception {
		request.setAttribute(SitePreferenceResolvingHandlerInterceptor.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteNormalPreference() throws Exception {
		request.setAttribute(SitePreferenceResolvingHandlerInterceptor.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
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
		request.setAttribute(SitePreferenceResolvingHandlerInterceptor.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		assertFalse(siteSwitcher.preHandle(request, response, null));
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNormalPreference() throws Exception {
		device.setMobile(false);
		request.setAttribute(SitePreferenceResolvingHandlerInterceptor.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		assertTrue(siteSwitcher.preHandle(request, response, null));
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void mDot() throws Exception {
		SiteSwitcherHandlerInterceptor mDot = SiteSwitcherHandlerInterceptor.mDot("app.com");
		assertFalse(mDot.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void dotMobi() throws Exception {
		SiteSwitcherHandlerInterceptor dotMobi = SiteSwitcherHandlerInterceptor.dotMobi("app.com");		
		assertFalse(dotMobi.preHandle(request, response, null));
		assertEquals(0, response.getCookies().length);
		assertEquals("http://app.mobi", response.getRedirectedUrl());
	}
	
}