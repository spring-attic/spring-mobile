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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

/**
 * SitePreferenceRepository implementation that stores user preference in a Cookie.
 * Generally the preferred implementation.
 * Extends CookieGenerator to allow for fine-grained control over the cookie attributes.
 * @author Keith Donald
 */
public class CookieSitePreferenceRepository extends CookieGenerator implements SitePreferenceRepository {

	/**
	 * Creates a cookie-based SitePreferenceRepository.
	 * By default, the name of the cookie is <code>org.springframework.mobile.device.site.CookiteSitePreferenceRepository.SITE_PREFERENCE</code>
	 */
	public CookieSitePreferenceRepository() {
		setCookieName(DEFAULT_COOKIE_NAME);
	}

	/**
	 * Creates a cookie-based SitePreferenceRepository with a custom domain value.
	 * Allows for convenient specification of to a shared domain such as <code>.app.com</code>.
	 */
	public CookieSitePreferenceRepository(String cookieDomain) {
		setCookieName(DEFAULT_COOKIE_NAME);
		setCookieDomain(cookieDomain);
	}

	public void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response) {
		addCookie(response, preference.name());
	}

	public SitePreference loadSitePreference(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, getCookieName());
		return cookie != null ? SitePreference.valueOf(cookie.getValue()) : null;
	}

	private static final String DEFAULT_COOKIE_NAME = CookieSitePreferenceRepository.class.getName() + ".SITE_PREFERENCE";

}
