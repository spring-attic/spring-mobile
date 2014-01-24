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
 * Site URL factory implementation that differentiates each site by the value of the server name field.
 * For example, your 'normal' site might be bound to 'myapp.com', while your mobile site might be bound to 'm.myapp.com'. 
 * @author Keith Donald
 * @author Roy Clarkson
 */
public class StandardSiteUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {
	
	private final String serverName;

	/**
	 * Creates a new {@link StandardSiteUrlFactory}.
	 * @param serverName the server name
	 */
	public StandardSiteUrlFactory(String serverName) {
		this.serverName = serverName;
	}

	public boolean isRequestForSite(HttpServletRequest request) {
		return serverName.equals(request.getServerName());
	}

	public String createSiteUrl(HttpServletRequest request) {
		return createSiteUrlInternal(request, this.serverName, request.getRequestURI());
	}
	
}
