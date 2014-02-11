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

import org.springframework.mobile.device.Device;

public class StubDevice implements Device {

	private DeviceType deviceType;

	/**
	 * @return new 'normal' device
	 */
	public StubDevice() {
		this.deviceType = DeviceType.NORMAL;
	}

	/**
	 * @param deviceType
	 * @return new device based deviceType
	 */
	public StubDevice(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

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

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

}
