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
package org.springframework.mobile.device.wurfl;

import org.springframework.mobile.device.Device;

/**
 * Device implementation that delegates to the {@link net.sourceforge.wurfl.core.Device WURFL device} implementation.
 * The native WURFL device can also be accessed by calling {@link #getWurfl()}.
 * @author Keith Donald
 */
public class WurflDevice implements Device {

	private final net.sourceforge.wurfl.core.Device device;
	
	public WurflDevice(net.sourceforge.wurfl.core.Device device) {
		this.device = device;
	}
	
	public String getUserAgent() {
		return device.getUserAgent();
	}

	public boolean isMobile() {
		String capability = device.getCapability("is_wireless_device");
		return capability != null && capability.length() > 0 && Boolean.valueOf(capability);
	}

	/**
	 * The native WURFL device object, exposing the full capabilities of the WURFL API.
	 */
	public net.sourceforge.wurfl.core.Device getWurfl() {
		return device;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[WurflDevice ").append("userAgent").append("=").append(getUserAgent()).append(", ");
		builder.append("mobile").append("=").append(isMobile()).append(", ");
		builder.append("wurfl").append("=").append(getWurfl()).append("]");
		return builder.toString();
	}

}
