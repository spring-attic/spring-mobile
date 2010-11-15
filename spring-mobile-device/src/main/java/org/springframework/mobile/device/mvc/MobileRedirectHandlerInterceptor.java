/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.mobile.device.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * A Spring MVC interceptor that redirects the user to a dedicated mobile web-site if the Device that originated the request is a mobile device.
 * Resolves the Device model from the value of the {@link DeviceResolvingHandlerInterceptor#CURRENT_DEVICE_ATTRIBUTE currentDevice request attribute}.
 * @author Keith Donald
 */
public class MobileRedirectHandlerInterceptor implements HandlerInterceptor {

	private final String redirectUrl;
	
	/**
	 * Creates a mobile redirect handler interceptor.
	 * @param redirectUrl the url to redirect to if the device is a mobile device; redirection is handled by calling {@link HttpServletResponse#sendRedirect(String)}.
	 */
	public MobileRedirectHandlerInterceptor(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Device device = (Device) request.getAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE);
		if (device.isMobile()) {
			response.sendRedirect(response.encodeRedirectURL(redirectUrl));
			return false;
		} else {
			return true;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {	
	}
	
}