package org.springframework.mobile.device.site;

import static org.springframework.mobile.device.site.SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;

/**
 * Static helper for accessing request-scoped SitePreference values.
 * @author Keith Donald
 */
public class SitePreferenceUtils {

	/**
	 * Get the current site preference for the user that originated this web request.
	 * @return the site preference, or null if none has been set
	 */
	public static SitePreference getCurrentSitePreference(HttpServletRequest request) {
		return (SitePreference) request.getAttribute(CURRENT_SITE_PREFERENCE_ATTRIBUTE);		
	}

	/**
	 * Get the current site preference for the user from the request attributes map.
	 * @return the site preference, or null if none has been set
	 */
	public static SitePreference getCurrentSitePreference(RequestAttributes attributes) {
		return (SitePreference) attributes.getAttribute(CURRENT_SITE_PREFERENCE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);		
	}

	private SitePreferenceUtils() {
		
	}
	
}
