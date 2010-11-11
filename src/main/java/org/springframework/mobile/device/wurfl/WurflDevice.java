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

import net.sourceforge.wurfl.core.handlers.AndroidHandler;
import net.sourceforge.wurfl.core.handlers.AppleHandler;

import org.springframework.mobile.device.Device;

/**
 * Device implementation that delegates to the {@link net.sourceforge.wurfl.core.Device} implementation.
 * The native WURFL device can be obtained by calling {@link #getWurflDevice()}.
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

	public boolean isMobileBrowser() {
		String mobileBrowserCapability = device.getCapability("mobile_browser");
		return mobileBrowserCapability != null && mobileBrowserCapability.length() > 0;
	}

	public boolean isApple() {
		return new AppleHandler().canHandle(device.getUserAgent());
	}
	
	public boolean isAndroid() {
		return new AndroidHandler().canHandle(device.getUserAgent());
	}
	
	/**
	 * The native WURFL device object, exposing the full capabilities of the WURFL API.
	 */
	public net.sourceforge.wurfl.core.Device getWurflDevice() {
		return device;
	}

}
