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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.CookieSitePreferenceRepository;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.StandardSitePreferenceHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Servlet 2.3 Filter that switches the user between the mobile and normal site by employing a specific switching
 * algorithm. The switching algorithm is as follows:
 * <ul>
 * <li>If the request originates from the mobile site:
 *     <ul>
 *         <li>If the user prefers the normal site, then redirect the user to the normal site.</li>
 *     </ul>
 * </li>
 * <li>Otherwise, the request originates from the normal site, so:</li>
 *     <ul>
 *         <li>If the user prefers the mobile site, or the user has no site preference and is on a mobile device, 
 *         redirect the user to the mobile site.</li>
 *     </ul>
 * </li>
 * </ul>
 * <p>Four initialization parameters are available for configuring the servlet filter: 
 * <ul>
 *     <li><code>switcherMode</code></li>
 *     <li><code>serverName</code></li>
 *     <li><code>mobilePath</code></li>
 *     <li><code>rootPath</code></li>
 * </ul>
 * <p>The <code>switcherMode</code> init parameter requires that you specify one of three values. And for each of these 
 * values, the corresponding additional required init params are listed.
 * <ul>
 *     <li><code>mDot</code></li>
 *         <ul>
 *             <li><code>serverName</code> is required</li>
 *         </ul>
 *     <li><code>dotMobi</code></li>
 *         <ul>
 *             <li><code>serverName</code> is required</li>
 *         </ul>
 *     <li><code>urlPath</code></li>
 *         <ul>
 *             <li><code>mobilePath</code> is required</li>
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
 * <p>Creates a site switcher that switches between <code>app.com</code> and <code>app.com/mob/</code>:
 * <pre>
 * &lt;init-param&gt;
 *     &lt;param-name&gt;switcherMode&lt;/param-name&gt;
 *     &lt;param-value&gt;urlPath&lt;/param-value&gt;
 *     &lt;param-name&gt;mobilePath&lt;/param-name&gt;
 *     &lt;param-value&gt;mob&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * @author Guilherme Willian de Oliveira
 * @author Roy Clarkson
 */
public class SiteSwitcherRequestFilter extends OncePerRequestFilter {

	private SiteUrlFactory normalSiteUrlFactory;

	private SiteUrlFactory mobileSiteUrlFactory;

	private SitePreferenceHandler sitePreferenceHandler;

	private String switcherMode;

	private String serverName;

	private String mobilePath;

	private String rootPath;

	public SiteSwitcherRequestFilter() {
	}

	/**
	 * Creates a new site switcher.
	 * @param normalSiteUrlFactory the factory for a "normal" site URL e.g. http://app.com
	 * @param mobileSiteUrlFactory the factory for a "mobile" site URL e.g. http://m.app.com
	 * @param sitePreferenceHandler the handler for the user site preference
	 */
	public SiteSwitcherRequestFilter(SiteUrlFactory normalSiteUrlFactory, SiteUrlFactory mobileSiteUrlFactory,
			SitePreferenceHandler sitePreferenceHandler) {
		this.normalSiteUrlFactory = normalSiteUrlFactory;
		this.mobileSiteUrlFactory = mobileSiteUrlFactory;
		this.sitePreferenceHandler = sitePreferenceHandler;
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

	public String getMobilePath() {
		return mobilePath;
	}

	public void setMobilePath(String mobilePath) {
		this.mobilePath = mobilePath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	protected void initFilterBean() throws ServletException {
		if (switcherMode == null) {
			throw new ServletException("switcherMode init parameter not found");
		}
		SiteSwitcherMode mode;
		try {
			mode = Enum.valueOf(SiteSwitcherMode.class, switcherMode.toUpperCase());
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

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SitePreference sitePreference = sitePreferenceHandler.handleSitePreference(request, response);
		if (mobileSiteUrlFactory.isRequestForSite(request)) {
			if (sitePreference == SitePreference.NORMAL) {
				response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
			}
		} else {
			Device device = DeviceUtils.getRequiredCurrentDevice(request);
			if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
				response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));
			}
		}
		filterChain.doFilter(request, response);
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
		this.normalSiteUrlFactory = new StandardSiteUrlFactory(serverName);
		this.mobileSiteUrlFactory = new StandardSiteUrlFactory("m." + serverName);
		this.sitePreferenceHandler = new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("."
				+ serverName));
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
		this.normalSiteUrlFactory = new StandardSiteUrlFactory(serverName);
		this.mobileSiteUrlFactory = new StandardSiteUrlFactory(serverName.substring(0, lastDot) + ".mobi");
		this.sitePreferenceHandler = new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("."
				+ serverName));
	}

	/**
	 * Configures a site switcher that redirects to a path on the current domain for normal site requests that either
	 * originate from a mobile device or indicate a mobile site preference.
	 * Uses a {@link CookieSitePreferenceRepository} that saves a cookie that is stored on the root path.
	 */
	private void urlPath() throws ServletException {
		if (mobilePath == null) {
			throw new ServletException("mobilePath init parameter not found");
		}
		if (rootPath == null) {
			this.normalSiteUrlFactory = new NormalSitePathUrlFactory(mobilePath);
			this.mobileSiteUrlFactory = new MobileSitePathUrlFactory(mobilePath);
		} else {
			this.normalSiteUrlFactory = new NormalSitePathUrlFactory(mobilePath, rootPath);
			this.mobileSiteUrlFactory = new MobileSitePathUrlFactory(mobilePath, rootPath);
		}
		this.sitePreferenceHandler = new StandardSitePreferenceHandler(new CookieSitePreferenceRepository());
	}

	private enum SiteSwitcherMode {
		MDOT, DOTMOBI, URLPATH;
	}

}