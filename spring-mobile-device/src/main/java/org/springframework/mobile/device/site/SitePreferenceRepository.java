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

/**
 * A strategy for storing a user's site preference.
 * Supports allowing the user to explicitly choose which site they want e.g. 'normal', 'mobile', or 'tablet'.
 */
public interface SitePreferenceRepository {

	/**
	 * Load the user's site preference.
	 * Returns null if the user has not specified a preference.
	 */
	SitePreference loadSitePreference(HttpServletRequest request);
	
	/**
	 * Save the user's site preference.
	 */
	void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response);

}
