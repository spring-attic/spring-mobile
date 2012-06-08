package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.StubDevice;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.mobile.device.site.StubSitePreferenceRepository;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SiteSwitcherRequestFilterTest {

	private SiteSwitcherRequestFilter filter;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();
	private MockFilterChain filterChain = new MockFilterChain();

	private StubDevice device = new StubDevice();

	@Before
	public void setup() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		filter = new SiteSwitcherRequestFilter("app.com");
	}

	@Test
	public void mobileDeviceNormalSiteNoPreference() throws Exception {
		filter.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteMobilePreference() throws Exception {
		request.addParameter("site_preference", "mobile");
		filter.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void mobileDeviceNormalSiteNormalPreference() throws Exception {
		request.addParameter("site_preference", "normal");
		filter.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNoPreference() throws Exception {
		device.setMobile(false);
		filter.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}
	@Test
	public void normalDeviceNormalSiteMobilePreference() throws Exception {
		device.setMobile(false);
		request.addParameter("site_preference", "mobile");
		filter.doFilter(request, response, filterChain);
		assertEquals("http://m.app.com", response.getRedirectedUrl());
	}

	@Test
	public void normalDeviceNormalSiteNormalPreference() throws Exception {
		device.setMobile(false);
		request.addParameter("site_preference", "normal");
		filter.doFilter(request, response, filterChain);
		assertNull(response.getRedirectedUrl());
	}
}