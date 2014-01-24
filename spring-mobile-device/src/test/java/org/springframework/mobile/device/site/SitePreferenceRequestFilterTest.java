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
