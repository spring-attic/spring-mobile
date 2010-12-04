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
import org.springframework.mobile.device.mvc.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreferenceResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A Spring MVC interceptor that switches the user between the mobile and normal site by employing a specific switching algorithm.
 * The switching algorithm is as follows:
 * <ul>
 * <li>If the request originates from the mobile site:
 *     <ul>
 *        <li>If the user prefers the normal site, then redirect the user to the normal site.</li>
 *     </ul>
 * </li>
 * <li>Otherwise, the request originates from the normal site, so:</li>
 *     <ul>
 *        <li>If the user prefers the mobile site, or the user has no site preference and is on a mobile device, 
 * redirect the user to the mobile site.</li>
 *     </ul>
 * </ul>
 * @author Keith Donald
 */
public class SiteSwitcherHandlerInterceptor extends HandlerInterceptorAdapter {

	public static final String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

	private final SiteUrlFactory normalSiteUrlFactory;
	
	private final SiteUrlFactory mobileSiteUrlFactory;

	private final SitePreferenceResolver sitePreferenceResolver;
	
	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param sitePreferenceRepository the store for resolving user site preference
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SitePreferenceRepository sitePreferenceRepository) {
		this.normalSiteUrlFactory = normalSiteUrlFactory;
		this.mobileSiteUrlFactory = mobileSiteUrlFactory;
		this.sitePreferenceResolver = new SitePreferenceResolver(sitePreferenceRepository);
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SitePreference sitePreference = sitePreferenceResolver.resolveSitePreference(request, response);
		if (mobileSiteUrlFactory.isRequestForSite(request)) {
			if (sitePreference == SitePreference.NORMAL) {
				response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
				return false;				
			}
		} else {
			Device device = DeviceResolverHandlerInterceptor.getRequiredCurrentDevice(request);
			if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
				response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));				
				return false;
			}
		}
		return true;
	}

	// static factory methods

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor mDot(String serverName) {
		return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(serverName),
				new StandardSiteUrlFactory("m." + serverName), new CookieSitePreferenceRepository("." + serverName));
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor dotMobi(String serverName) {
		int lastDot = serverName.lastIndexOf('.');
		return new SiteSwitcherHandlerInterceptor(new StandardSiteUrlFactory(serverName),
				new StandardSiteUrlFactory(serverName.substring(0, lastDot) + ".mobi"),
				new CookieSitePreferenceRepository("." + serverName));		
	}
	
}