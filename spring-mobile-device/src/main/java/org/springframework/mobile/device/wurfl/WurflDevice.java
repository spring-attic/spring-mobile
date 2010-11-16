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

import java.util.Map;

import net.sourceforge.wurfl.core.CapabilityNotDefinedException;
import net.sourceforge.wurfl.core.MarkUp;

import org.springframework.mobile.device.Device;

/**
 * Device implementation that delegates to the {@link net.sourceforge.wurfl.core.Device WURFL device} implementation.
 * The native WURFL device can also be accessed by calling {@link #getWurfl()}.
 * @author Keith Donald
 */
public class WurflDevice implements Device, net.sourceforge.wurfl.core.Device {

	private final net.sourceforge.wurfl.core.Device device;
	
	public WurflDevice(net.sourceforge.wurfl.core.Device device) {
		this.device = device;
	}

	// implementing our Device interface
	
	public boolean isMobile() {
		String capability = device.getCapability("is_wireless_device");
		return capability != null && capability.length() > 0 && Boolean.valueOf(capability);
	}

	// implementing Wurfl Device
	
	public String getId() {
		return device.getId();
	}

	public String getUserAgent() {
		return device.getUserAgent();
	}

	public String getCapability(String name) throws CapabilityNotDefinedException {
		return device.getCapability(name);
	}

	@SuppressWarnings("rawtypes")
	public Map getCapabilities() {
		return device.getCapabilities();
	}

	public MarkUp getMarkUp() {
		return device.getMarkUp();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[WurflDevice ");
		builder.append("mobile").append("=").append(isMobile()).append(", ");
		builder.append("id").append("=").append(getId());
		builder.append("userAgent").append("=").append(getUserAgent());
		builder.append("capabilities").append("=").append(getCapabilities());
		builder.append("markup").append("=").append(getMarkUp());
		builder.append("]");
		return builder.toString();
	}

}