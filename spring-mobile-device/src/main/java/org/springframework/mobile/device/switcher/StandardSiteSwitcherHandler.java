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

package org.springframework.mobile.device.switcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.util.ResolverUtils;

/**
 * @author Roy Clarkson
 */
public class StandardSiteSwitcherHandler implements SiteSwitcherHandler {

	private final SiteUrlFactory normalSiteUrlFactory;

	private final SiteUrlFactory mobileSiteUrlFactory;

	private final SiteUrlFactory tabletSiteUrlFactory;

	private final SitePreferenceHandler sitePreferenceHandler;

	private final boolean tabletIsMobile;

	/**
	 * Creates a new site switcher handler
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param tabletSiteUrlFactory the factory for a "tablet" site URL e.g. http://app.com/tablet
	 * @param sitePreferenceHandler the handler for the user site preference
	 * @param tabletIsMobile true if tablets should be redirected to the mobile site
	 */
	public StandardSiteSwitcherHandler(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler, Boolean tabletIsMobile) {
		this.normalSiteUrlFactory = normalSiteUrlFactory;
		this.mobileSiteUrlFactory = mobileSiteUrlFactory;
		this.tabletSiteUrlFactory = tabletSiteUrlFactory;
		this.sitePreferenceHandler = sitePreferenceHandler;
		this.tabletIsMobile = tabletIsMobile == null ? false : tabletIsMobile;
	}

	public boolean handleSiteSwitch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SitePreference sitePreference = sitePreferenceHandler.handleSitePreference(request, response);
		Device device = DeviceUtils.getRequiredCurrentDevice(request);
		if (mobileSiteUrlFactory != null && mobileSiteUrlFactory.isRequestForSite(request)) {
			if (ResolverUtils.isTablet(device, sitePreference)) {
				if (tabletSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(tabletSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			}
			if (ResolverUtils.isNormal(device, sitePreference) || handleTabletIsNormal(device, sitePreference)) {
				if (normalSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			}
		} else if (tabletSiteUrlFactory != null && tabletSiteUrlFactory.isRequestForSite(request)) {
			if (ResolverUtils.isNormal(device, sitePreference)) {
				if (normalSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			}
			if (ResolverUtils.isMobile(device, sitePreference)) {
				if (mobileSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			}
		} else {
			if (ResolverUtils.isMobile(device, sitePreference) || handleTabletIsMobile(device, sitePreference)) {
				if (mobileSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			} else if (ResolverUtils.isTablet(device, sitePreference)) {
				if (tabletSiteUrlFactory != null) {
					response.sendRedirect(response.encodeRedirectURL(tabletSiteUrlFactory.createSiteUrl(request)));
					return false;
				}
			}
		}
		return true;

	}

	// Helpers

	private boolean handleTabletIsNormal(Device device, SitePreference sitePreference) {
		return sitePreference == SitePreference.TABLET && tabletIsMobile == false
				&& (device.isTablet() || device.isMobile());
	}

	private boolean handleTabletIsMobile(Device device, SitePreference sitePreference) {
		return tabletIsMobile == true && (sitePreference == SitePreference.TABLET || sitePreference == null)
				&& (device.isTablet() || device.isMobile());
	}

}
