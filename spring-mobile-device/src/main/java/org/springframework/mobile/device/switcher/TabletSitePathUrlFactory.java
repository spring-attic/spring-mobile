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

import javax.servlet.http.HttpServletRequest;

/**
 * Path based site URL factory implementation that handles requests for the "tablet" site.
 * @author Roy Clarkson
 */
public class TabletSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {

	/**
	 * Creates a new tablet site path URL factory.
	 * @param tabletPath the path to the tablet site
	 */
	public TabletSitePathUrlFactory(final String tabletPath) {
		this(tabletPath, null);
	}
	
	/**
	 * Creates a new tablet site path URL factory.
	 * @param tabletPath the path to the tablet site
	 * @param rootPath the root path of the application
	 */
	public TabletSitePathUrlFactory(final String tabletPath, final String rootPath) {
		super(null, tabletPath, rootPath);
	}

	public boolean isRequestForSite(HttpServletRequest request) {
		return request.getRequestURI().startsWith(this.getFullTabletPath());
	}

	public String createSiteUrl(HttpServletRequest request) {
		String adjustedRequestURI;
		if (getRootPath() != null && request.getRequestURI().startsWith(getRootPath())) {
			adjustedRequestURI = getFullTabletPath() + request.getRequestURI().substring(getRootPath().length());
		} else {
			adjustedRequestURI = this.getCleanTabletPath() + request.getRequestURI();
		}
		return createSiteUrlInternal(request, request.getServerName(), adjustedRequestURI);
	}

}
