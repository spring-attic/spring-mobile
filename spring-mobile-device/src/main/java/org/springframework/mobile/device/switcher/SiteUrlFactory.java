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
 * Strategy for constructing different site URLs, such as the 'mobile' site URL.
 * @author Keith Donald
 */
public interface SiteUrlFactory {

	/**
	 * Did the request originate from this site?
	 * Used to determine what site the user is currently viewing.
	 * For example, if the user is viewing a page on "m.app.com", this method would likely return 'true' if it constructs m.app.com URLs.
	 * The {@link SiteSwitcherHandlerInterceptor} uses this knowledge to implement its switching algorithm.
	 * @see SiteSwitcherHandlerInterceptor#preHandle(HttpServletRequest, javax.servlet.http.HttpServletResponse, Object)
	 */
	boolean isRequestForSite(HttpServletRequest request);

	/**
	 * Create a fully-qualified URL that can be used to redirect the user to this site.
	 * A typical implementation changes the target host but preserves the request path.
	 */
	String createSiteUrl(HttpServletRequest request);

}
