package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.mvc.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.mvc.StubDevice;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SitePreferenceResolverTest {
	
	private SitePreferenceResolver sitePreferenceResolver;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private StubSitePreferenceRepository repository = new StubSitePreferenceRepository();
	
	@Before
	public void setup() throws Exception {
		sitePreferenceResolver = new SitePreferenceResolver(repository);
	}
	
	@Test
	public void saveSitePreference() throws Exception {
		request.addParameter("site_preference", "normal");
		assertEquals(SitePreference.NORMAL, sitePreferenceResolver.resolveSitePreference(request, response));
		assertEquals(SitePreference.NORMAL, repository.getSitePreference());
		assertEquals(SitePreference.NORMAL, SitePreferenceResolver.getCurrentSitePreference(request));
	}

	@Test
	public void loadSitePreference() throws Exception {
		repository.setSitePreference(SitePreference.MOBILE);
		assertEquals(SitePreference.MOBILE, sitePreferenceResolver.resolveSitePreference(request, response));
		assertEquals(SitePreference.MOBILE, SitePreferenceResolver.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreference() throws Exception {
		assertNull(sitePreferenceResolver.resolveSitePreference(request, response));
		assertNull(SitePreferenceResolver.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreferenceMobileDevice() throws Exception {
		request.setAttribute(DeviceResolverHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, new StubDevice());
		assertEquals(SitePreference.MOBILE, sitePreferenceResolver.resolveSitePreference(request, response));
		assertEquals(SitePreference.MOBILE, SitePreferenceResolver.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreferenceNormalDevice() throws Exception {
		request.setAttribute(DeviceResolverHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, new StubDevice(false));
		assertEquals(SitePreference.NORMAL, sitePreferenceResolver.resolveSitePreference(request, response));
		assertEquals(SitePreference.NORMAL, SitePreferenceResolver.getCurrentSitePreference(request));
	}

}