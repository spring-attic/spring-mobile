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
 * <ul>
 * <li>If the user prefers the normal site, then redirect the user to the normal site.</li>
 * </ul>
 * </li>
 * <li>Otherwise, the request originates from the normal site, so:</li>
 * <ul>
 * <li>If the user prefers the mobile site, or the user has no site preference and is on a mobile device, redirect the
 * user to the mobile site.</li>
 * </ul>
 * </ul> Creates a site switcher that redirects to a <code>m.</code> domain for normal site requests that either
 * originate from a mobile device or indicate a mobile site preference. Uses a {@link CookieSitePreferenceRepository}
 * that saves a cookie that is shared between the two domains. need define the server name as parameter of the filter
 * <init-param> <param-name>serverName</param-name> <param-value>facebook.com</param-value> </init-param>
 * 
 * @author Guilherme Willian de Oliveira
 */
public class SiteSwitcherRequestFilter extends OncePerRequestFilter {
	private SiteUrlFactory normalSiteUrlFactory;
	private SiteUrlFactory mobileSiteUrlFactory;
	private SitePreferenceHandler sitePreferenceHandler;

	public SiteSwitcherRequestFilter() {
	}

	public SiteSwitcherRequestFilter(String serverName) {
		configureDotMobi(serverName);
	}

	public void initFilterBean() throws ServletException {
		String serverName = getFilterConfig().getInitParameter("serverName");
		configureDotMobi(serverName);
	}

	public void configureDotMobi(String serverName) {
		normalSiteUrlFactory = new StandardSiteUrlFactory(serverName);
		mobileSiteUrlFactory = new StandardSiteUrlFactory("m." + serverName);
		sitePreferenceHandler = new StandardSitePreferenceHandler(new CookieSitePreferenceRepository("." + serverName));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SitePreference sitePreference = sitePreferenceHandler.handleSitePreference(request, response);
		if (mobileSiteUrlFactory.isRequestForSite(request)) {
			if (sitePreference == SitePreference.NORMAL) {
				response.sendRedirect(response.encodeRedirectURL(normalSiteUrlFactory.createSiteUrl(request)));
			} else {
				filterChain.doFilter(request, response);
			}
		} else {
			Device device = DeviceUtils.getRequiredCurrentDevice(request);
			if (sitePreference == SitePreference.MOBILE || device.isMobile() && sitePreference == null) {
				response.sendRedirect(response.encodeRedirectURL(mobileSiteUrlFactory.createSiteUrl(request)));
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}
}