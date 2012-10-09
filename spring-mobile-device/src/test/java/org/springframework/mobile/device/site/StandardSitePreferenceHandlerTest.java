package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.StubDevice;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class StandardSitePreferenceHandlerTest {
	
	private StandardSitePreferenceHandler sitePreferenceHandler;
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	private StubSitePreferenceRepository sitePreferenceRepository = new StubSitePreferenceRepository();
	
	@Before
	public void setup() throws Exception {
		sitePreferenceHandler = new StandardSitePreferenceHandler(sitePreferenceRepository);
	}
	
	@Test
	public void saveInvalidSitePreference() throws Exception {
		request.addParameter("site_preference", "invalid");
		assertEquals(null, sitePreferenceHandler.handleSitePreference(request, response));
		assertEquals(null, sitePreferenceRepository.getSitePreference());
		assertEquals(null, SitePreferenceUtils.getCurrentSitePreference(request));
	}
	
	@Test
	public void saveSitePreference() throws Exception {
		request.addParameter("site_preference", "normal");
		assertEquals(SitePreference.NORMAL, sitePreferenceHandler.handleSitePreference(request, response));
		assertEquals(SitePreference.NORMAL, sitePreferenceRepository.getSitePreference());
		assertEquals(SitePreference.NORMAL, SitePreferenceUtils.getCurrentSitePreference(request));
	}

	@Test
	public void loadSitePreference() throws Exception {
		sitePreferenceRepository.setSitePreference(SitePreference.MOBILE);
		assertEquals(SitePreference.MOBILE, sitePreferenceHandler.handleSitePreference(request, response));
		assertEquals(SitePreference.MOBILE, SitePreferenceUtils.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreference() throws Exception {
		assertNull(sitePreferenceHandler.handleSitePreference(request, response));
		assertNull(SitePreferenceUtils.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreferenceMobileDevice() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new StubDevice(DeviceType.MOBILE));
		assertEquals(SitePreference.MOBILE, sitePreferenceHandler.handleSitePreference(request, response));
		assertEquals(SitePreference.MOBILE, SitePreferenceUtils.getCurrentSitePreference(request));
	}

	@Test
	public void defaultSitePreferenceNormalDevice() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new StubDevice(DeviceType.NORMAL));
		assertEquals(SitePreference.NORMAL, sitePreferenceHandler.handleSitePreference(request, response));
		assertEquals(SitePreference.NORMAL, SitePreferenceUtils.getCurrentSitePreference(request));
	}

}