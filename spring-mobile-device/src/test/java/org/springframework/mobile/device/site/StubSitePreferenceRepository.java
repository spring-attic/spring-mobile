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

import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceRepository;

public class StubSitePreferenceRepository implements SitePreferenceRepository {

	private SitePreference sitePreference;

	public void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response) {
		sitePreference = preference;
	}

	public SitePreference loadSitePreference(HttpServletRequest request) {
		return sitePreference;
	}
	
	public SitePreference getSitePreference() {
		return sitePreference;
	}
	
	public void setSitePreference(SitePreference sitePreference) {
		this.sitePreference = sitePreference;
	}
}
