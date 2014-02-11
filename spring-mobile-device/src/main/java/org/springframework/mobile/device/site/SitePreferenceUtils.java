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

import static org.springframework.mobile.device.site.SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;

/**
 * Static helper for accessing request-scoped SitePreference values.
 * @author Keith Donald
 */
public class SitePreferenceUtils {

	/**
	 * Get the current site preference for the user that originated this web request.
	 * @return the site preference, or null if none has been set
	 */
	public static SitePreference getCurrentSitePreference(HttpServletRequest request) {
		return (SitePreference) request.getAttribute(CURRENT_SITE_PREFERENCE_ATTRIBUTE);
	}

	/**
	 * Get the current site preference for the user from the request attributes map.
	 * @return the site preference, or null if none has been set
	 */
	public static SitePreference getCurrentSitePreference(RequestAttributes attributes) {
		return (SitePreference) attributes.getAttribute(CURRENT_SITE_PREFERENCE_ATTRIBUTE,
				RequestAttributes.SCOPE_REQUEST);
	}

	private SitePreferenceUtils() {

	}

}
