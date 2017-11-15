/*
 * Copyright 2010-2017 the original author or authors.
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

import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;

/**
 * @author Roy Clarkson
 * @since 2.0
 */
public class StandardSiteSwitcherHandlerFactory {

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandler mDot(String serverName) {
		return mDot(serverName, false);
	}

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandler mDot(String serverName, Boolean tabletIsMobile) {
		return standard(serverName, "m." + serverName, "." + serverName, tabletIsMobile);
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandler dotMobi(String serverName) {
		return dotMobi(serverName, false);
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandler dotMobi(String serverName, Boolean tabletIsMobile) {
		int lastDot = serverName.lastIndexOf('.');
		return standard(serverName, serverName.substring(0, lastDot) + ".mobi", "." + serverName, tabletIsMobile);
	}

	/**
	 * Creates a site switcher that redirects to a custom domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 * @param normalServerName the 'normal' domain name e.g. "normal.com"
	 * @param mobileServerName the 'mobile domain name e.g. "mobile.com"
	 * @param cookieDomain the name to use for saving the cookie
	 * @see #standard(String, String, String, Boolean)
	 * @see #standard(String, String, String, String)
	 * @see StandardSiteUrlFactory
	 */
	public static SiteSwitcherHandler standard(String normalServerName, String mobileServerName, String cookieDomain) {
		return standard(normalServerName, mobileServerName, cookieDomain, false);
	}

	/**
	 * Creates a site switcher that redirects to a custom domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 * @param normalServerName the 'normal' domain name e.g. "normal.com"
	 * @param mobileServerName the 'mobile domain name e.g. "mobile.com"
	 * @param cookieDomain the name to use for saving the cookie
	 * @param tabletIsMobile true if tablets should be presented with the 'mobile' site
	 * @see #standard(String, String, String)
	 * @see #standard(String, String, String, String)
	 * @see StandardSiteUrlFactory
	 */
	public static SiteSwitcherHandler standard(String normalServerName, String mobileServerName, String cookieDomain, Boolean tabletIsMobile) {
		return new StandardSiteSwitcherHandler(
				new StandardSiteUrlFactory(normalServerName),
				new StandardSiteUrlFactory(mobileServerName),
				null,
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository(cookieDomain)),
				tabletIsMobile);
	}

	/**
	 * Creates a site switcher that redirects to a custom domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 * @param normalServerName the 'normal' domain name e.g. "normal.com"
	 * @param mobileServerName the 'mobile domain name e.g. "mobile.com"
	 * @param tabletServerName the 'tablet' domain name e.g. "tablet.com"
	 * @param cookieDomain the name to use for saving the cookie
	 * @see #standard(String, String, String)
	 * @see #standard(String, String, String, Boolean)
	 * @see StandardSiteUrlFactory
	 */
	public static SiteSwitcherHandler standard(String normalServerName, String mobileServerName, String tabletServerName, String cookieDomain) {
		return new StandardSiteSwitcherHandler(
				new StandardSiteUrlFactory(normalServerName),
				new StandardSiteUrlFactory(mobileServerName),
				new StandardSiteUrlFactory(tabletServerName),
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository(cookieDomain)),
				null);
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandler urlPath(String mobilePath) {
		return new StandardSiteSwitcherHandler(
				new NormalSitePathUrlFactory(mobilePath),
				new MobileSitePathUrlFactory(mobilePath, null),
				null,
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()),
				false);
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Allows you to configure a root path for an application. For example, if your app is running at <code>http://www.domain.com/myapp</code>,
	 * then the root path is <code>/myapp</code>.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandler urlPath(String mobilePath, String rootPath) {
		return new StandardSiteSwitcherHandler(
				new NormalSitePathUrlFactory(mobilePath, rootPath),
				new MobileSitePathUrlFactory(mobilePath, null, rootPath),
				null,
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()),
				false);
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or tablet device, or indicate a mobile or tablet site preference.
	 * Allows you to configure a root path for an application. For example, if your app is running at <code>http://www.domain.com/myapp</code>,
	 * then the root path is <code>/myapp</code>.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandler urlPath(String mobilePath, String tabletPath, String rootPath) {
		return new StandardSiteSwitcherHandler(
				new NormalSitePathUrlFactory(mobilePath, tabletPath, rootPath),
				new MobileSitePathUrlFactory(mobilePath, tabletPath, rootPath),
				new TabletSitePathUrlFactory(tabletPath, mobilePath, rootPath),
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()),
				null);
	}
}
