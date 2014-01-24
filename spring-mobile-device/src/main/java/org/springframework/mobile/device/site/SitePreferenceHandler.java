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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

/**
 * Service interface for site preference management.
 * @author Keith Donald
 */
public interface SitePreferenceHandler {

	/**
	 * The name of the request attribute that holds the current user's site preference value.
	 */
	final String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

	/**
	 * Handle the site preference aspect of the web request.
	 * Implementations should check if the user has indicated a site preference.
	 * If so, the indicated site preference should be saved and remembered for future requests.
	 * If no site preference has been indicated, an implementation may derive a default site preference from the {@link Device} that originated the request.
	 * After handling, the resolved site preference is available as a {@link #CURRENT_SITE_PREFERENCE_ATTRIBUTE request attribute}.
	 * @param request the web request
	 * @param response the web response
	 * @return the resolved site preference for the user that originated the web request
	 */
	SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response);

}
