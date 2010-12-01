package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CookieSitePreferenceRepositoryTest {

	private CookieSitePreferenceRepository repository = new CookieSitePreferenceRepository();
	
	@Test
	public void setSitePreference() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		assertNull(repository.loadSitePreference(request));
		repository.saveSitePreference(SitePreference.MOBILE, request, response);
		request.setCookies(response.getCookies());
		assertEquals(SitePreference.MOBILE, repository.loadSitePreference(request));
	}
}
