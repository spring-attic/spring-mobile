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
package org.springframework.mobile.device.resolver.lib;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.resolver.DeviceRequest;
import org.springframework.mobile.device.resolver.DeviceResolver;
import org.springframework.mobile.device.support.GenericDevice;

/**
 * Resolves Android-based devices.
 * @author Keith Donald
 */
public class AndroidDeviceResolver implements DeviceResolver {

	public Device resolveDevice(DeviceRequest request) {
		String userAgent = request.getUserAgent();
		if (isAndroid(userAgent)) {
			return new GenericDevice(userAgent, true, false);
		} else {
			return null;
		}
	}
	
	// internal helpers
	
	private boolean isAndroid(String userAgent) {
		for (String keyword : androidKeywords) {
			if (userAgent.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

	private Collection<String> androidKeywords = Arrays.asList(new String[] { "Android" });

}
