package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.mvc.DeviceResolvingHandlerInterceptor;
import org.springframework.mobile.device.mvc.StubDevice;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SitePreferenceResolvingHandlerInterceptorTest {
	
	private SitePreferenceResolvingHandlerInterceptor interceptor;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private StubSitePreferenceRepository repository = new StubSitePreferenceRepository();
	
	@Before
	public void setup() throws Exception {
		interceptor = new SitePreferenceResolvingHandlerInterceptor(repository);
	}
	
	@Test
	public void saveSitePreference() throws Exception {
		request.addParameter("site_preference", "normal");
		assertTrue(interceptor.preHandle(request, response, null));
		assertEquals(SitePreference.NORMAL, repository.loadSitePreference(request));
		assertEquals(SitePreference.NORMAL, SitePreferenceResolvingHandlerInterceptor.getCurrentSitePreference(request));
	}

	@Test
	public void loadSitePreference() throws Exception {
		repository.saveSitePreference(SitePreference.MOBILE, request, response);
		assertTrue(interceptor.preHandle(request, response, null));
		assertEquals(SitePreference.MOBILE, SitePreferenceResolvingHandlerInterceptor.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreference() throws Exception {
		assertTrue(interceptor.preHandle(request, response, null));
		assertNull(SitePreferenceResolvingHandlerInterceptor.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreferenceFromDevice() throws Exception {
		request.setAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, new StubDevice());
		assertTrue(interceptor.preHandle(request, response, null));
		assertEquals(SitePreference.MOBILE, SitePreferenceResolvingHandlerInterceptor.getCurrentSitePreference(request));
	}

}