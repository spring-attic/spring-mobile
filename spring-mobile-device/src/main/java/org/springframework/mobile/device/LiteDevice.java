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

/**
 * A lightweight Device implementation suitable for use as support code.
 * Typically used to hold the output of a device resolution invocation.
 * @author Keith Donald
 * @author Roy Clarkson
 * @author Scott Rossillo
 */
class LiteDevice implements Device {
	
	public static final LiteDevice NORMAL_INSTANCE = new LiteDevice(DeviceType.NORMAL);

	public static final LiteDevice MOBILE_INSTANCE = new LiteDevice(DeviceType.MOBILE);

	public static final LiteDevice TABLET_INSTANCE = new LiteDevice(DeviceType.TABLET);
	
	public boolean isNormal() {
		return this.deviceType == DeviceType.NORMAL;
	}

	public boolean isMobile() {
		return this.deviceType == DeviceType.MOBILE;
	}

	public boolean isTablet() {
		return this.deviceType == DeviceType.TABLET;
	}

	public DeviceType getDeviceType() {
		return this.deviceType;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[LiteDevice ");
		builder.append("type").append("=").append(this.deviceType);
		builder.append("]");
		return builder.toString();
	}

	private final DeviceType deviceType;

	/**
	 * Creates a LiteDevice.
	 */
	private LiteDevice(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

}
