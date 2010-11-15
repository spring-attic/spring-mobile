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
package org.springframework.mobile.device.support;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolutionService;
import org.springframework.mobile.device.resolver.DeviceRequest;
import org.springframework.mobile.device.resolver.DeviceResolver;

/**
 * General-purpose device resolution service implementation.
 * Allows one or more device resolvers to be registered that are applied in the order they are registered.
 * The first resolver that succeeds wins.
 * If no resolver succeeds, a {@link #createDefaultDevice(DeviceRequest)} implementation is returned.
 * @author Keith Donald
 * @see #addDeviceResolver(DeviceResolver)
 */
public class GenericDeviceResolutionService implements DeviceResolutionService {

	private final Set<DeviceResolver> deviceResolvers = new LinkedHashSet<DeviceResolver>();

	/**
	 * Add a resolver for a device family.
	 * @param resolver the device resolver to register
	 */
	public void addDeviceResolver(DeviceResolver resolver) {
		deviceResolvers.add(resolver);
	}
	
	public Device resolveDevice(HttpServletRequest request) {
		HttpServletDeviceRequest deviceRequest = new HttpServletDeviceRequest(request);
		for (DeviceResolver resolver : deviceResolvers) {
			Device device = resolver.resolveDevice(deviceRequest);
			if (device != null) {
				return device;
			}
		}
		return createDefaultDevice(deviceRequest);
	}

	/**
	 * Creates the default device implementation if no resolver matched.
	 * This implementation simply assumes the device is not a mobile browser and performs no further processing.
	 * Subclasses may override.
	 */
	protected Device createDefaultDevice(DeviceRequest request) {
		return new GenericDevice(request.getUserAgent(), false ,false);
	}

}