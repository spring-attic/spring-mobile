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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Servlet 2.3 Filter that switches the user between the mobile and normal site by employing a specific switching
 * algorithm. The switching algorithm is as follows:
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
 * <p>The initialization parameters available for configuring the servlet filter: 
 * <ul>
 *     <li><code>switcherMode</code></li>
 *     <li><code>serverName</code></li>
 *     <li><code>tabletIsMobile</code></li>
 *     <li><code>mobilePath</code></li>
 *     <li><code>tabletPath</code></li>
 *     <li><code>rootPath</code></li>
 * </ul>
 * <p>The <code>switcherMode</code> init parameter requires that you specify one of three values. And for each of these 
 * values, the corresponding additional required init params are listed.
 * <ul>
 *     <li><code>mDot</code></li>
 *         <ul>
 *             <li><code>serverName</code> is required</li>
 *             <li><code>tabletIsMobile</code> is optional</li>
 *         </ul>
 *     <li><code>dotMobi</code></li>
 *         <ul>
 *             <li><code>serverName</code> is required</li>
 *             <li><code>tabletIsMobile</code> is optional</li>
 *         </ul>
 *     <li><code>urlPath</code></li>
 *         <ul>
 *             <li><code>mobilePath</code> is optional</li>
 *             <li><code>tabletPath</code> is optional</li>
 *             <li><code>rootPath</code> is optional</li>
 *         </ul>
 * </ul>
 * <p>The following examples illustrate the different parameter configurations.
 * <p>Creates a site switcher that switches between <code>app.com</code> and <code>m.app.com</code>:
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;switcherMode&lt;/param-name&gt;
 *     &lt;param-value&gt;mDot&lt;/param-value&gt;
 *     &lt;param-name&gt;serverName&lt;/param-name&gt;
 *     &lt;param-value&gt;app.com&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * <p>Creates a site switcher that switches between <code>app.com</code> and <code>app.mobi</code>:
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;switcherMode&lt;/param-name&gt;
 *     &lt;param-value&gt;dotMobi&lt;/param-value&gt;
 *     &lt;param-name&gt;serverName&lt;/param-name&gt;
 *     &lt;param-value&gt;app.com&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * <p>By default tablets are presented with the 'normal' site when using <code>mDot</code> or 
 * <code>dotMobi</code>. However you can have the site switcher direct tablets to the mobile site by
 * setting the <code>tabletIsMobile</code> parameter to true.
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;switcherMode&lt;/param-name&gt;
 *     &lt;param-value&gt;mDot&lt;/param-value&gt;
 *     &lt;param-name&gt;serverName&lt;/param-name&gt;
 *     &lt;param-value&gt;app.com&lt;/param-value&gt;
 *     &lt;param-name&gt;tabletIsMobile&lt;/param-name&gt;
 *     &lt;param-value&gt;true&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre> 
 * <p>Creates a site switcher that switches between <code>app.com</code> and <code>app.com/mob/</code> 
 * for mobile devices, and <code>app.com</code> and <code>app.com/tab/</code> for tablet devices:
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;switcherMode&lt;/param-name&gt;
 *     &lt;param-value&gt;urlPath&lt;/param-value&gt;
 *     &lt;param-name&gt;mobilePath&lt;/param-name&gt;
 *     &lt;param-value&gt;mob&lt;/param-value&gt;
 *     &lt;param-name&gt;tabletPath&lt;/param-name&gt;
 *     &lt;param-value&gt;tab&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * @author Guilherme Willian de Oliveira
 * @author Roy Clarkson
 */
public class SiteSwitcherRequestFilter extends OncePerRequestFilter {

	private SiteSwitcherHandler siteSwitcherHandler;

	private String switcherMode;

	private String serverName;

	private Boolean tabletIsMobile;

	private String mobilePath;

	private String tabletPath;

	private String rootPath;

	public SiteSwitcherRequestFilter() {
	}

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param tabletSiteUrlFactory the factory for a "tablet" site
	 * @param sitePreferenceHandler the handler for the user site preference
	 */
	public SiteSwitcherRequestFilter(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SiteUrlFactory tabletSiteUrlFactory, SitePreferenceHandler sitePreferenceHandler) {
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory,
				tabletSiteUrlFactory, sitePreferenceHandler, tabletIsMobile);
	}

	public String getSwitcherMode() {
		return switcherMode;
	}

	public void setSwitcherMode(String switcherMode) {
		this.switcherMode = switcherMode;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Boolean getTabletIsMobile() {
		return tabletIsMobile;
	}

	public void setTabletIsMobile(Boolean tabletIsMobile) {
		this.tabletIsMobile = tabletIsMobile;
	}

	public String getMobilePath() {
		return mobilePath;
	}

	public void setMobilePath(String mobilePath) {
		this.mobilePath = mobilePath;
	}

	public String getTabletPath() {
		return tabletPath;
	}

	public void setTabletPath(String tabletPath) {
		this.tabletPath = tabletPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	protected void initFilterBean() throws ServletException {
		if (switcherMode != null) {
			SiteSwitcherMode mode;
			try {
				mode = SiteSwitcherMode.valueOf(switcherMode.toUpperCase());
			} catch (IllegalArgumentException ex) {
				throw new ServletException("Invalid switcherMode init parameter", ex);
			}
			if (mode == SiteSwitcherMode.MDOT) {
				mDot();
			} else if (mode == SiteSwitcherMode.DOTMOBI) {
				dotMobi();
			} else if (mode == SiteSwitcherMode.URLPATH) {
				urlPath();
			}
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (siteSwitcherHandler.handleSiteSwitch(request, response)) {
			filterChain.doFilter(request, response);
		}
	}

	// helpers

	/**
	 * Configures a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	private void mDot() throws ServletException {
		if (serverName == null) {
			throw new ServletException("serverName init parameter not found");
		}
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(new StandardSiteUrlFactory(serverName),
				new StandardSiteUrlFactory("m." + serverName), null, new StandardSitePreferenceHandler(
						new CookieSitePreferenceRepository("." + serverName)), tabletIsMobile);
	}

	/**
	 * Configures a site switcher that redirects to a <code>.mobi</code> domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Will strip off the trailing domain name when building the mobile domain
	 * e.g. "app.com" will become "app.mobi" (the .com will be stripped).
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is shared between the two domains.
	 */
	private void dotMobi() throws ServletException {
		if (serverName == null) {
			throw new ServletException("serverName init parameter not found");
		}
		int lastDot = serverName.lastIndexOf('.');
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(new StandardSiteUrlFactory(serverName),
				new StandardSiteUrlFactory(serverName.substring(0, lastDot) + ".mobi"), null,
				new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + serverName)), tabletIsMobile);
	}

	/**
	 * Configures a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile or tablet device, or indicate a mobile or tablet site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	private void urlPath() throws ServletException {
		SiteUrlFactory normalSiteUrlFactory = new NormalSitePathUrlFactory(mobilePath, tabletPath, rootPath);
		SiteUrlFactory mobileSiteUrlFactory = null;
		SiteUrlFactory tabletSiteUrlFactory = null;
		if (mobilePath != null) {
			mobileSiteUrlFactory = new MobileSitePathUrlFactory(mobilePath, tabletPath, rootPath);
		}
		if (tabletPath != null) {
			tabletSiteUrlFactory = new TabletSitePathUrlFactory(tabletPath, mobilePath, rootPath);
		}
		this.siteSwitcherHandler = new StandardSiteSwitcherHandler(normalSiteUrlFactory, mobileSiteUrlFactory,
				tabletSiteUrlFactory, new StandardSitePreferenceHandler(new CookieSitePreferenceRepository()), null);
	}

	private enum SiteSwitcherMode {
		MDOT, DOTMOBI, URLPATH;
	}

}
