package org.springframework.mobile.device.switcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract implementation of the {@link SiteUrlFactory} interface.
 * Doesn't mandate the strategy for constructing different site URLs; 
 * simply implements common functionality.
 *  
 * @author Scott Rossillo
 *
 */
public abstract class AbstractSiteUrlFactory implements SiteUrlFactory {

	/**
	 * Returns the HTTP port specified on the given request if it's a non-standard port. 
	 * The port is considered non-standard if it's not port 80 for insecure request and not 
	 * port 443 of secure requests.
	 * 
	 * @param request the <code>HttpServletRequest</code> to check for a non-standard port.
	 *  
	 * @return the HTTP port specified on the given request if it's a non-standard port, <code>null<code> otherwise
	 */
	protected String optionalPort(HttpServletRequest request) {
        if ("http".equals(request.getScheme()) && request.getServerPort() != 80 || "https".equals(request.getScheme()) && request.getServerPort() != 443) {
            return ":" + request.getServerPort();
        } else {
        	return null;
        }
	}

}
