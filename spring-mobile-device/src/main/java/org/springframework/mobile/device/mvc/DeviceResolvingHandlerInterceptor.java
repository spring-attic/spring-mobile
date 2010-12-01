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
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.lite.LiteDeviceResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A Spring MVC interceptor that resolves the Device that originated the web request <i>before</i> any request handler is invoked.
 * The resolved Device is exported as a request attribute under the well-known name of {@link #CURRENT_DEVICE_ATTRIBUTE}.
 * Request handlers such as @Controllers and views may then access the currentDevice to vary their control and rendering logic, respectively.
 * @author Keith Donald
 */
public class DeviceResolvingHandlerInterceptor extends HandlerInterceptorAdapter {

	/**
	 * The name of the request attribute the current Device is indexed by.
	 * The attribute name is 'currentDevice'.
	 */
	public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";

	private final DeviceResolver deviceResolver;

	/**
	 * Create a device resolving {@link HandlerInterceptor} that defaults to a {@link LiteDeviceResolver} implementation.
	 */
	public DeviceResolvingHandlerInterceptor() {
		this(new LiteDeviceResolver());
	}
	
	/**
	 * Create a device resolving {@link HandlerInterceptor}.
	 * @param deviceResolver the device resolver to delegate to in {@link #preHandle(HttpServletRequest, HttpServletResponse, Object)}.
	 */
	public DeviceResolvingHandlerInterceptor(DeviceResolver deviceResolver) {
		this.deviceResolver = deviceResolver;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Device device = deviceResolver.resolveDevice(request);
		request.setAttribute(CURRENT_DEVICE_ATTRIBUTE, device);
		return true;
	}

	/**
	 * Static utility method that extracts the current device from the web request.
	 * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
	 * @param request the servlet request
	 * @return the current device, or null if no device has been resolved for the request
	 */
	public static Device getCurrentDevice(HttpServletRequest request) {
		return (Device) request.getAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE);
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
			throw new IllegalStateException("No currenet device is set in this request and one is required - have you configured a DeviceResolvingHandlerInterceptor?");
		}
		return device;
	}

	/**
	 * Static utility method that extracts the current device from the request attributes map.
	 * Encapsulates the {@link HttpServletRequest#getAttribute(String)} lookup.
	 * @param request the request attributes
	 * @return the current device, or null if no device has been resolved for the request
	 */
	public static Device getCurrentDevice(RequestAttributes attributes) {
		return (Device) attributes.getAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
	}
	
}