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
 * Path based site URL factory implementation that handles requests for the "normal" site.
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public class NormalSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {

	/**
	 * Creates a new normal site path URL factory.
	 * @param mobilePath the path to the mobile site
	 */
	public NormalSitePathUrlFactory(final String mobilePath) {
		this(mobilePath, null, null);
	}
	
	/**
	 * Creates a new normal site path URL factory.
	 * @param mobilePath the path to the mobile site
	 * @param rootPath the root path of the application
	 */
	public NormalSitePathUrlFactory(final String mobilePath, final String rootPath) {
		this(mobilePath, null, rootPath);
	}
	
	/**
	 * Creates a new normal site path URL factory.
	 * @param mobilePath the path to the mobile site
	 * @param tabletPath the path to the tablet site
	 * @param rootPath the root path of the application
	 */
	public NormalSitePathUrlFactory(final String mobilePath, final String tabletPath, final String rootPath) {
		super(mobilePath, tabletPath, rootPath);
	}

	public boolean isRequestForSite(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		if (hasMobilePath() && (requestURI.startsWith(getFullMobilePath()) || requestURI.equals(getCleanMobilePath()))) {
			return false;
		} else if (hasTabletPath() && (requestURI.startsWith(getFullTabletPath()) || requestURI.equals(getCleanTabletPath()))) {
			return false;
		}
		return true;
	}

	public String createSiteUrl(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String adjustedRequestURI = "";
		if (hasMobilePath() && requestURI.equals(getCleanMobilePath()) || 
				hasTabletPath() && requestURI.equals(getCleanTabletPath())) {
			if (hasRootPath()) {
				adjustedRequestURI = getCleanRootPath();
			}
		} else if (hasMobilePath() && requestURI.startsWith(getFullMobilePath())) {
			if (hasRootPath()) {
				adjustedRequestURI = getRootPath() + requestURI.substring(getFullMobilePath().length());
			} else {
				adjustedRequestURI = requestURI.substring(getCleanMobilePath().length());
			}
		} else if (hasTabletPath() && requestURI.startsWith(getFullTabletPath())) {
			if (hasRootPath()) {
				adjustedRequestURI = getRootPath() + requestURI.substring(getFullTabletPath().length());
			} else {
				adjustedRequestURI = requestURI.substring(getCleanTabletPath().length());
			}
		}
		return createSiteUrlInternal(request, request.getServerName(), adjustedRequestURI);
	}

}
