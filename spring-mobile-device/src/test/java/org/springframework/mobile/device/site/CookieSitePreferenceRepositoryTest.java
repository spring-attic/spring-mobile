/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	public void setSitePreferenceNormal() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		assertNull(repository.loadSitePreference(request));
		repository.saveSitePreference(SitePreference.NORMAL, request, response);
		request.setCookies(response.getCookies());
		assertEquals(SitePreference.NORMAL, repository.loadSitePreference(request));
	}
	
	@Test
	public void setSitePreferenceMobile() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		assertNull(repository.loadSitePreference(request));
		repository.saveSitePreference(SitePreference.MOBILE, request, response);
		request.setCookies(response.getCookies());
		assertEquals(SitePreference.MOBILE, repository.loadSitePreference(request));
	}
	
	@Test
	public void setSitePreferenceTablet() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		assertNull(repository.loadSitePreference(request));
		repository.saveSitePreference(SitePreference.TABLET, request, response);
		request.setCookies(response.getCookies());
		assertEquals(SitePreference.TABLET, repository.loadSitePreference(request));
	}
}
