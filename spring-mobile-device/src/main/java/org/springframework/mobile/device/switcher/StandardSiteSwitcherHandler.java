/*
 * Copyright 2012 the original author or authors.
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
			if (sitePreference == SitePreference.NORMAL) {
				redirectToNormalSite(request, response);
				return false;
			} else if (sitePreference == SitePreference.TABLET || device.isTablet() && sitePreference == null) {
				redirectToTabletSite(request, response);
				return false;
			}
		} else if (tabletSiteUrlFactory != null && tabletSiteUrlFactory.isRequestForSite(request)) {
			if (sitePreference == SitePreference.NORMAL) {
				redirectToNormalSite(request, response);
				return false;
			} else if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
				redirectToMobileSite(request, response);
				return false;
			}
		} else {
			if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null
					|| tabletIsMobile == true && device.isTablet() && sitePreference != SitePreference.NORMAL) {
				redirectToMobileSite(request, response);
				return false;
			} else if (sitePreference == SitePreference.TABLET || device.isTablet() && sitePreference == null) {
				redirectToTabletSite(request, response);
				return false;
			}
		}
		return true;

	}

	// helpers

	private void redirectToNormalSite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (normalSiteUrlFactory != null) {
			response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
		}
	}

	private void redirectToMobileSite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (mobileSiteUrlFactory != null) {
			response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));
		}
	}

	private void redirectToTabletSite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (tabletSiteUrlFactory != null) {
			response.sendRedirect(response.encodeRedirectURL(tabletSiteUrlFactory.createSiteUrl(request)));
		}
	}

}
