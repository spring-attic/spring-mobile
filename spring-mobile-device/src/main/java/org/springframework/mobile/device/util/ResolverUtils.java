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

package org.springframework.mobile.device.util;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.site.SitePreference;

/**
 * Static helper for determining how to handle the combination of device and
 * site preference.
 * @author Roy Clarkson
 */
public class ResolverUtils {

	/**
	 * Should the combination of {@link Device} and {@link SitePreference} be handled
	 * as a normal device
	 * @param device the resolved device
	 * @param sitePreference the specified site preference
	 * @return true if normal
	 */
	public static boolean isNormal(Device device, SitePreference sitePreference) {
		return sitePreference == SitePreference.NORMAL ||
				(device == null || device.isNormal() && sitePreference == null);
	}

	/**
	 * Should the combination of {@link Device} and {@link SitePreference} be handled
	 * as a mobile device
	 * @param device the resolved device
	 * @param sitePreference the specified site preference
	 * @return true if mobile
	 */
	public static boolean isMobile(Device device, SitePreference sitePreference) {
		return sitePreference == SitePreference.MOBILE || device != null && device.isMobile() && sitePreference == null;
	}

	/**
	 * Should the combination of {@link Device} and {@link SitePreference} be handled
	 * as a tablet device
	 * @param device the resolved device
	 * @param sitePreference the specified site preference
	 * @return true if tablet
	 */
	public static boolean isTablet(Device device, SitePreference sitePreference) {
		return sitePreference == SitePreference.TABLET || device != null && device.isTablet() && sitePreference == null;
	}

	private ResolverUtils() {

	}

}
