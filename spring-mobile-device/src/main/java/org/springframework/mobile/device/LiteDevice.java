/*
 * Copyright 2010-2011 the original author or authors.
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
 */
class LiteDevice implements Device {
	
	private static enum DeviceType { MOBILE, TABLET, OTHER };

	public static final LiteDevice MOBILE_INSTANCE = new LiteDevice(DeviceType.MOBILE);
	
	public static final LiteDevice TABLET_INSTANCE = new LiteDevice(DeviceType.TABLET);

	public static final LiteDevice NOT_MOBILE_INSTANCE = new LiteDevice(DeviceType.OTHER);

	public boolean isMobile() {
		return mobile;
	}

	public boolean isTablet() {
		return tablet;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[LiteDevice ");
		builder.append("mobile").append("=").append(isMobile());
		builder.append("]");
		return builder.toString();
	}

	private final boolean mobile;
	private final boolean tablet;

	/**
	 * Creates a LiteDevice.
	 */
	private LiteDevice(DeviceType deviceType) {
		
		switch(deviceType) {
			case MOBILE:
				this.mobile = true;
				this.tablet = false;
				break;
			case TABLET:
				this.mobile = false;
				this.tablet = true;
				break;
			default:
				this.mobile = false;
				this.tablet = false;
		}
	}
	
}