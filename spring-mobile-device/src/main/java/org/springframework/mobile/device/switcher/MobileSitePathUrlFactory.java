package org.springframework.mobile.device.switcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Path based site URL factory implementation that handles requests for the "mobile" site.
 * 
 * @author Scott Rossillo
 *
 */
public class MobileSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {
	
	/**
	 * Creates a new mobile site path URL factory.
	 */
	public MobileSitePathUrlFactory(final String mobilePath) {
		super(mobilePath);
	}

	public boolean isRequestForSite(HttpServletRequest request) {
		return request.getRequestURI().startsWith(this.getMobilePath());
	}

	public String createSiteUrl(HttpServletRequest request) {
		final StringBuilder builder = new StringBuilder();
		builder.append(request.getScheme()).append("://").append(request.getServerName());
		String optionalPort = optionalPort(request);
		if (optionalPort != null) {
			builder.append(optionalPort);
		}
		builder.append(this.getCleanMobilePath()).append(request.getRequestURI());
		
		return builder.toString();
	}
}
