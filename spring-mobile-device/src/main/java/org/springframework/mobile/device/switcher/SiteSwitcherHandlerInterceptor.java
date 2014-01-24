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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A Spring MVC interceptor that switches the user between the mobile, normal, and tablet sites by employing a specific 
 * switching algorithm. The switching algorithm is as follows:
 * <ul>
 * <li>If the request originates from the mobile site:
 *     <ul>
 *        <li>If the user prefers the normal site, then redirect the user to the normal site.</li>
 *     </ul>
 *     <ul>
 *        <li>If the user prefers the tablet site, then redirect the user to the tablet site.</li>
 *     </ul>
 * </li>
 * <li>If the request originates from the tablet site:
 *     <ul>
 *        <li>If the user prefers the normal site, then redirect the user to the normal site.</li>
 *     </ul>
 *     <ul>
 *        <li>If the user prefers the mobile site, then redirect the user to the mobile site.</li>
 *     </ul>
 * </li>
 * <li>Otherwise, the request originates from the normal site, so:</li>
 *     <ul>
 *        <li>If the user prefers the mobile site, or the user has no site preference and is on a mobile device, 
 * redirect the user to the mobile site.</li>
 *     </ul>
 *     <ul>
 *        <li>If the user prefers the tablet site, or the user has no site preference and is on a tablet device, 
 * redirect the user to the tablet site.</li>
 *     </ul>
 * </ul>
 * @author Keith Donald
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public class SiteSwitcherHandlerInterceptor extends HandlerInterceptorAdapter {

	private final SiteSwitcherHandler siteSwitcherHandler;

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param sitePreferenceHandler the handler for the user site preference
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SitePreferenceHandler sitePreferenceHandler) {
		this(normalSiteUrlFactory, mobileSiteUrlFactory, sitePreferenceHandler, false);
	}

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com 
	 * @param sitePreferenceHandler the handler for the user site preference
	 * @param tabletIsMobile true to send tablets to the 'mobile' site.
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SitePreferenceHandler sitePreferenceHandler, Boolean tabletIsMobile) {
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory, null,
				sitePreferenceHandler, tabletIsMobile);
	}

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param tabletSiteUrlFactory the factory for a "tablet" site URL e.g. http://app.com/tablet
	 * @param sitePreferenceHandler the handler for the user site preference
	 */
	public SiteSwitcherHandlerInterceptor(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory,
				tabletSiteUrlFactory, sitePreferenceHandler, null);
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return siteSwitcherHandler.handleSiteSwitch(request, response);
	}

	// static factory methods

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor mDot(String serverName) {
		return mDot(serverName, false);
	}

	/**
	 * Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor mDot(String serverName, Boolean tabletIsMobile) {
		return standard(serverName, "m." + serverName, "." + serverName, tabletIsMobile);
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor dotMobi(String serverName) {
		return dotMobi(serverName, false);
	}

	/**
	 * Creates a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	public static SiteSwitcherHandlerInterceptor dotMobi(String serverName, Boolean tabletIsMobile) {
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
	public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String cookieDomain) {
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
	public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String cookieDomain, Boolean tabletIsMobile) {
		return new SiteSwitcherHandlerInterceptor(
				new StandardSiteUrlFactory(normalServerName),
				new StandardSiteUrlFactory(mobileServerName),
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
	public static SiteSwitcherHandlerInterceptor standard(String normalServerName, String mobileServerName, String tabletServerName, String cookieDomain) {
		return new SiteSwitcherHandlerInterceptor(
				new StandardSiteUrlFactory(normalServerName),
				new StandardSiteUrlFactory(mobileServerName),
				new StandardSiteUrlFactory(tabletServerName),
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository(cookieDomain)));
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath) {
		return new SiteSwitcherHandlerInterceptor(
				new NormalSitePathUrlFactory(mobilePath),
				new MobileSitePathUrlFactory(mobilePath, null), 
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Allows you to configure a root path for an application. For example, if your app is running at <code>http://www.domain.com/myapp</code>,
	 * then the root path is <code>/myapp</code>.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath, String rootPath) {
		return new SiteSwitcherHandlerInterceptor(
				new NormalSitePathUrlFactory(mobilePath, rootPath),
				new MobileSitePathUrlFactory(mobilePath, null, rootPath), 
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}

	/**
	 * Creates a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or tablet device, or indicate a mobile or tablet site preference.
	 * Allows you to configure a root path for an application. For example, if your app is running at <code>http://www.domain.com/myapp</code>,
	 * then the root path is <code>/myapp</code>.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	public static SiteSwitcherHandlerInterceptor urlPath(String mobilePath, String tabletPath, String rootPath) {
		return new SiteSwitcherHandlerInterceptor(
				new NormalSitePathUrlFactory(mobilePath, tabletPath, rootPath),
				new MobileSitePathUrlFactory(mobilePath, tabletPath, rootPath), 
				new TabletSitePathUrlFactory(tabletPath, mobilePath, rootPath), 
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()));
	}

}
