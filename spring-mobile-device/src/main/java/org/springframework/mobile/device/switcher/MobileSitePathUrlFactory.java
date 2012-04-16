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
 * Path based site URL factory implementation that handles requests for the "mobile" site.
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public class MobileSitePathUrlFactory extends AbstractSitePathUrlFactory implements SiteUrlFactory {

	/**
	 * Creates a new mobile site path URL factory.
	 */
	public MobileSitePathUrlFactory(final String mobilePath) {
		super(mobilePath);
	}

	/**
	 * Creates a new mobile site path URL factory.
	 */
	public MobileSitePathUrlFactory(final String mobilePath, final String rootPath) {
		super(mobilePath, rootPath);
	}

	public boolean isRequestForSite(HttpServletRequest request) {
		return request.getRequestURI().startsWith(this.getFullMobilePath());
	}

	public String createSiteUrl(HttpServletRequest request) {
		String adjustedRequestURI;
		if (getRootPath() != null && request.getRequestURI().startsWith(getRootPath())) {
			adjustedRequestURI = getFullMobilePath() + request.getRequestURI().substring(getRootPath().length());
		} else {
			adjustedRequestURI = this.getCleanMobilePath() + request.getRequestURI();
		}
		return createSiteUrlInternal(request, request.getServerName(), adjustedRequestURI);
	}

}
