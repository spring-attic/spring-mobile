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
import org.springframework.mobile.device.DeviceResolutionService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC interceptor that resolves the Device originating the web request before any handler is invoked.
 * The resolved Device is exported as a request attribute under a well-known name of {@link #CURRENT_DEVICE_ATTRIBUTE}.
 * @author Keith Donald
 */
public class DeviceResolvingHandlerInterceptor implements HandlerInterceptor {

	/**
	 * The name of the request attribute the current Device is indexed by.
	 * The attribute name is 'currentDevice'.
	 */
	public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";
	
	private final DeviceResolutionService deviceResolutionService;

	/**
	 * Create a device resolving {@link HandlerInterceptor}.
	 * @param deviceResolutionService the device resolution system to delegate to in {@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}.
	 */
	public DeviceResolvingHandlerInterceptor(DeviceResolutionService deviceResolutionService) {
		this.deviceResolutionService = deviceResolutionService;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Device device = deviceResolutionService.resolveDevice(request);
		request.setAttribute(CURRENT_DEVICE_ATTRIBUTE, device);
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {	
	}
	
}
