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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A Servlet 2.3 Filter that resolves the Device that originated the web request. The resolved Device is exported as a
 * request attribute under the well-known name of {@link DeviceUtils#CURRENT_DEVICE_ATTRIBUTE}. Request handlers such as @Controllers
 * and views may then access the currentDevice to vary their control and rendering logic, respectively.
 * 
 * @author Roy Clarkson
 */
public class DeviceResolverRequestFilter extends OncePerRequestFilter {

	private final DeviceResolver deviceResolver;

	/**
	 * Create a device resolving {@link Filter} that defaults to a {@link LiteDeviceResolver} implementation.
	 */
	public DeviceResolverRequestFilter() {
		this(new LiteDeviceResolver());
	}

	/**
	 * Create a device resolving {@link Filter}.
	 * @param deviceResolver the device resolver to delegate to.
	 */
	public DeviceResolverRequestFilter(DeviceResolver deviceResolver) {
		this.deviceResolver = deviceResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Device device = deviceResolver.resolveDevice(request);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		filterChain.doFilter(request, response);
	}

}
