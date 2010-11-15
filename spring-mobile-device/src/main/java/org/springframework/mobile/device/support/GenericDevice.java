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

import org.springframework.mobile.device.Device;

/**
 * A general purpose Device implementation suitable for use as support code.
 * Typically used to hold the output of a device resolution invocation.
 * @author Keith Donald
 */
public class GenericDevice implements Device {

	private final String userAgent;

	private final boolean mobileBrowser;
	
	private final boolean apple;
	
	public GenericDevice(String userAgent, boolean mobileBrowser, boolean apple) {
		this.userAgent = userAgent;
		this.mobileBrowser = mobileBrowser;
		this.apple = apple;
	}
	
	public String getUserAgent() {
		return userAgent;
	}

	public boolean isMobileBrowser() {
		return mobileBrowser;
	}

	public boolean isApple() {
		return apple;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[Device ").append("userAgent").append("=").append(getUserAgent()).append(", ");
		builder.append("mobileBrowser").append("=").append(isMobileBrowser()).append(", ");
		builder.append("isApple").append("=").append(isApple()).append("]");
		return builder.toString();
	}

}