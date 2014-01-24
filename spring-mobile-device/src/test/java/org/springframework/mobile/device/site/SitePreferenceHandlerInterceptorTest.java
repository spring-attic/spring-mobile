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
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SitePreferenceHandlerInterceptorTest {
	
	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();

	@Test
	public void preHandleDefault() throws Exception {
		SitePreferenceHandlerInterceptor interceptor = new SitePreferenceHandlerInterceptor();
		boolean result = interceptor.preHandle(request, response, null);
		assertNull(SitePreferenceUtils.getCurrentSitePreference(request));
		assertTrue(result);
	}
	
	@Test
	public void preHandleCustomNormal() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.NORMAL);
				return SitePreference.NORMAL;
			}
		};
		SitePreferenceHandlerInterceptor interceptor = new SitePreferenceHandlerInterceptor(handler);
		boolean result = interceptor.preHandle(request, response, null);
		assertEquals(SitePreference.NORMAL, request.getAttribute("currentSitePreference"));
		assertTrue(result);
	}

	@Test
	public void preHandleCustomMobile() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.MOBILE);
				return SitePreference.MOBILE;
			}
		};
		SitePreferenceHandlerInterceptor interceptor = new SitePreferenceHandlerInterceptor(handler);
		boolean result = interceptor.preHandle(request, response, null);
		assertEquals(SitePreference.MOBILE, request.getAttribute("currentSitePreference"));
		assertTrue(result);
	}
	
	@Test
	public void preHandleCustomTablet() throws Exception {
		SitePreferenceHandler handler = new SitePreferenceHandler() {
			public SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response) {
				request.setAttribute("currentSitePreference", SitePreference.TABLET);
				return SitePreference.TABLET;
			}
		};
		SitePreferenceHandlerInterceptor interceptor = new SitePreferenceHandlerInterceptor(handler);
		boolean result = interceptor.preHandle(request, response, null);
		assertEquals(SitePreference.TABLET, request.getAttribute("currentSitePreference"));
		assertTrue(result);
	}

}
