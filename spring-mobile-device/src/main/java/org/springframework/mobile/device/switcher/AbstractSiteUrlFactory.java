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

/**
 * Abstract implementation of the {@link SiteUrlFactory} interface.
 * Doesn't mandate the strategy for constructing different site URLs; 
 * simply implements common functionality.
 * @author Keith Donald
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public abstract class AbstractSiteUrlFactory implements SiteUrlFactory {

	/**
	 * Returns the HTTP port specified on the given request if it's a non-standard port. 
	 * The port is considered non-standard if it's not port 80 for insecure request and not 
	 * port 443 of secure requests.
	 * @param request the <code>HttpServletRequest</code> to check for a non-standard port.
	 * @return the HTTP port specified on the given request if it's a non-standard port, <code>null<code> otherwise
	 */
	protected String optionalPort(HttpServletRequest request) {
		if ("http".equals(request.getScheme()) && request.getServerPort() != 80 || "https".equals(request.getScheme())
				&& request.getServerPort() != 443) {
			return ":" + request.getServerPort();
		} else {
			return null;
		}
	}
	
	protected String createSiteUrlInternal(HttpServletRequest request, String serverName, String path) {
		StringBuilder builder = new StringBuilder();
		builder.append(request.getScheme()).append("://").append(serverName);
		String optionalPort = optionalPort(request);
		if (optionalPort != null) {
			builder.append(optionalPort);
		}
		builder.append(path);
		if (request.getQueryString() != null) {
			builder.append('?').append(request.getQueryString());
		}
		return builder.toString();
	}

}
