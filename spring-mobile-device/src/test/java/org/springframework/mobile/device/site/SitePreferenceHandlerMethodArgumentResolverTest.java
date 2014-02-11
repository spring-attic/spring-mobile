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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class SitePreferenceHandlerMethodArgumentResolverTest {

	private SitePreferenceHandlerMethodArgumentResolver resolver = new SitePreferenceHandlerMethodArgumentResolver();

	private ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

	@Test
	public void resolveNormal() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", SitePreference.class), 0);
		assertTrue(resolver.supportsParameter(parameter));
		Object resolved = resolver.resolveArgument(parameter, null, request, null);
		assertEquals(SitePreference.NORMAL, resolved);
	}
	
	@Test
	public void resolveMobile() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", SitePreference.class), 0);
		assertTrue(resolver.supportsParameter(parameter));
		Object resolved = resolver.resolveArgument(parameter, null, request, null);
		assertEquals(SitePreference.MOBILE, resolved);
	}
	
	@Test
	public void resolveTablet() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", SitePreference.class), 0);
		assertTrue(resolver.supportsParameter(parameter));
		Object resolved = resolver.resolveArgument(parameter, null, request, null);
		assertEquals(SitePreference.TABLET, resolved);
	}
	
	@Test
	public void unresolvedNormal() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		assertFalse(resolver.supportsParameter(parameter));
	}

	@Test
	public void unresolvedMobile() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		assertFalse(resolver.supportsParameter(parameter));
	}
	
	@Test
	public void unresolvedTablet() throws Exception {
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		assertFalse(resolver.supportsParameter(parameter));
	}

	@RequestMapping
	public void handlerMethod(SitePreference sitePreference) {
		
	}

	@RequestMapping
	public void handlerMethodUnresolved(String foo) {
		
	}
	
}
