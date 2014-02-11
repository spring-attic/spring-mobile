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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Servlet 2.3 Filter that delegates to a {@link SitePreferenceHandler} to resolve the SitePreference of the user 
 * that originated the web request. The resolved SitePreference is exported as a request attribute under the well-known 
 * name of {@link SitePreferenceHandler#CURRENT_SITE_PREFERENCE_ATTRIBUTE}. Request handlers such as @Controllers and 
 * views may then access the currentSitePreference to vary their control and rendering logic, respectively.
 * 
 * Note: do not use this filter in conjunction with the SiteSwitcherRequestFilter, as the switcher internally delegates 
 * to a {@link SitePreferenceHandler} as part of its own workflow. This filter should only be used when site switching 
 * is not needed but site preference management is still desired.
 * 
 * @author Roy Clarkson
 */
public class SitePreferenceRequestFilter extends OncePerRequestFilter {
	
	private final SitePreferenceHandler sitePreferenceHandler;
	
	public SitePreferenceRequestFilter() {
		this(new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}
	
	public SitePreferenceRequestFilter(SitePreferenceHandler sitePreferenceHandler) {
		this.sitePreferenceHandler = sitePreferenceHandler;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		sitePreferenceHandler.handleSitePreference(request, response);
		filterChain.doFilter(request, response);
	}

}
