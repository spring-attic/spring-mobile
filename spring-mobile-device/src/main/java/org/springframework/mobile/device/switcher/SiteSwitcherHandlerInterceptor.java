/*
 * Copyright 2010 the original author or authors.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.mvc.DeviceResolvingHandlerInterceptor;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceResolvingHandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A Spring MVC interceptor that redirects the user to a dedicated mobile web-site if appropriate.
 * Requires that the current device already be resolved by {@link DeviceResolvingHandlerInterceptor}.
 * Also respects the current user's site preference value if resolved by a {@link SitePreferenceResolvingHandlerInterceptor}.
 * @author Keith Donald
 */
public class SiteSwitcherHandlerInterceptor extends HandlerInterceptorAdapter {

	public static final String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

	private final SiteUrlFactory normalSiteUrlFactory;
	
	private final SiteUrlFactory mobileSiteUrlFactory;

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param sitePreferenceRepository the store for recording user site preference
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory) {
		this.normalSiteUrlFactory = normalSiteUrlFactory;
		this.mobileSiteUrlFactory = mobileSiteUrlFactory;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SitePreference sitePreference = SitePreferenceResolvingHandlerInterceptor.getCurrentSitePreference(request);
		if (mobileSiteUrlFactory.isRequestForSite(request)) {
			if (sitePreference == SitePreference.NORMAL) {
				response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
				return false;				
			}
		} else {
			Device device = DeviceResolvingHandlerInterceptor.getRequiredCurrentDevice(request);
			if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
				response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));				
				return false;
			}
		}
		return true;
	}

	// static factory methods

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> host for requests originated by mobile devices (or when there is a mobile site preference).
	 * Uses {@link CookieSitePreferenceRepository}.
	 */
	public static SiteSwitcherHandlerInterceptor mDot(String serverName) {
		return new SiteSwitcherHandlerInterceptor(new ServerNameSiteUrlFactory(serverName), new ServerNameSiteUrlFactory("m." + serverName));
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for requests originated by mobile devices (or when there is a mobile site preference).
	 * Will strip off the trailing domain name when building the mobile domain e.g. "app.com" will become "app.mobi", as the .com will be stripped.
	 * Uses {@link CookieSitePreferenceRepository}.
	 */
	public static SiteSwitcherHandlerInterceptor dotMobi(String serverName) {
		int lastDot = serverName.lastIndexOf('.');
		return new SiteSwitcherHandlerInterceptor(new ServerNameSiteUrlFactory(serverName), new ServerNameSiteUrlFactory(serverName.substring(0, lastDot) + ".mobi"));		
	}
	
}