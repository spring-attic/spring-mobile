package org.springframework.mobile.device.site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;

/**
 * Service interface for site preference management.
 * @author Keith Donald
 */
public interface SitePreferenceHandler {

	/**
	 * The name of the request attribute that holds the current user's site preference value.
	 */
	final String CURRENT_SITE_PREFERENCE_ATTRIBUTE = "currentSitePreference";

	/**
	 * Handle the site preference aspect of the web request.
	 * Implementations should check if the user has indicated a site preference.
	 * If so, the indicated site preference should be saved and remembered for future requests.
	 * If no site preference has been indicated, an implementation may derive a default site preference from the {@link Device} that originated the request.
	 * After handling, the resolved site preference is available as a {@link #CURRENT_SITE_PREFERENCE_ATTRIBUTE request attribute}.
	 * @param request the web request
	 * @param response the web response
	 * @return the resolved site preference for the user that originated the web request
	 */
	SitePreference handleSitePreference(HttpServletRequest request, HttpServletResponse response);

}