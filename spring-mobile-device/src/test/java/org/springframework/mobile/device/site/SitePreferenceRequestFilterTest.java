package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SitePreferenceRequestFilterTest {
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private MockFilterChain filterChain = new MockFilterChain();

	@Test
	public void doFilterDefault() throws Exception {
		SitePreferenceRequestFilter filter = new SitePreferenceRequestFilter();
		filter.doFilter(request, response, filterChain);
		assertNull(SitePreferenceUtils.getCurrentSitePreference(request));
	}
	
	@Test
	public void doFilterCustomNormal() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.NORMAL);
				return SitePreference.NORMAL;
			}
		};
		SitePreferenceRequestFilter filter = new SitePreferenceRequestFilter(handler);
		filter.doFilter(request, response, filterChain);
		assertEquals(SitePreference.NORMAL, request.getAttribute("currentSitePreference"));
	}

	@Test
	public void doFilterCustomMobile() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.MOBILE);
				return SitePreference.MOBILE;
			}
		};
		SitePreferenceRequestFilter filter = new SitePreferenceRequestFilter(handler);
		filter.doFilter(request, response, filterChain);
		assertEquals(SitePreference.MOBILE, request.getAttribute("currentSitePreference"));
	}
	
	@Test
	public void doFilterCustomTablet() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.TABLET);
				return SitePreference.TABLET;
			}
		};
		SitePreferenceRequestFilter filter = new SitePreferenceRequestFilter(handler);
		filter.doFilter(request, response, filterChain);
		assertEquals(SitePreference.TABLET, request.getAttribute("currentSitePreference"));
	}

}
