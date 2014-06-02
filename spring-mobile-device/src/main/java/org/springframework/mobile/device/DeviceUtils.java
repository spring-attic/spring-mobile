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

package org.springframework.mobile.device;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;

/**
 * Static helper for accessing request-scoped Device values.
 * @author Keith Donald
 */
public class DeviceUtils {

	/**
	 * The name of the request attribute the current Device is indexed by.
	 * The attribute name is 'currentDevice'.
	 */
	public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";

	/**
	 * Static utility method that extracts the current device from the web request.
	 * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
	 * @param request the servlet request
	 * @return the current device, or null if no device has been resolved for the request
	 */
	public static Device getCurrentDevice(HttpServletRequest request) {
		return (Device) request.getAttribute(CURRENT_DEVICE_ATTRIBUTE);
	}

	/**
	 * Static utility method that extracts the current device from the web request.
	 * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
	 * Throws a runtime exception if the current device has not been resolved.
	 * @param request the servlet request
	 * @return the current device
	 */
	public static Device getRequiredCurrentDevice(HttpServletRequest request) {
		Device device = getCurrentDevice(request);
		if (device == null) {
			throw new IllegalStateException("No current device is set in this request and one is required - have you configured a DeviceResolvingHandlerInterceptor?");
		}
		return device;
	}

	/**
	 * Static utility method that extracts the current device from the request attributes map.
	 * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
	 * @param attributes the request attributes
	 * @return the current device, or null if no device has been resolved for the request
	 */
	public static Device getCurrentDevice(RequestAttributes attributes) {
		return (Device) attributes.getAttribute(CURRENT_DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}


}
