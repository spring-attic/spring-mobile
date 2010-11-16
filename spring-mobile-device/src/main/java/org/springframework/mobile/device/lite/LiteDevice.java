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
package org.springframework.mobile.device.lite;

import org.springframework.mobile.device.Device;

/**
 * A general purpose Device implementation suitable for use as support code.
 * Typically used to hold the output of a device resolution invocation.
 * @author Keith Donald
 */
class LiteDevice implements Device {

	public static final LiteDevice MOBILE_INSTANCE = new LiteDevice(true);

	public static final LiteDevice NOT_MOBILE_INSTANCE = new LiteDevice(false);

	public boolean isMobile() {
		return mobile;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[LiteDevice ");
		builder.append("mobile").append("=").append(isMobile());
		builder.append("]");
		return builder.toString();
	}

	private final boolean mobile;

	/**
	 * Creates a GeneriDevice.
	 */
	private LiteDevice(boolean mobile) {
		this.mobile = mobile;
	}
	
}