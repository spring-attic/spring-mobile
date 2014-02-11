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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Roy Clarkson
 */
public interface SiteSwitcherHandler {

	/**
	 * Handles the site switching aspect of the web request. For the requesting device, implementations should 
	 * determine the appropriate site with which to respond.
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return true if the request should be processed as received. false if the handler is handling the response.
	 * @throws IOException
	 */
	boolean handleSiteSwitch(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
