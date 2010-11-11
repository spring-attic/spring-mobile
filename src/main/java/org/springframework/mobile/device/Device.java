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
package org.springframework.mobile.device;

/**
 * A model for the user agent or device that submitted the current request.
 * @author Keith Donald
 */
public interface Device {

	/**
	 * The user agent string for this device.
	 * Generally obtained by reading the User-Agent header of the HTTP request.
	 * The DeviceResolver may have normalized this value when mapping it to a Device model in its library.
	 */
	String getUserAgent();

	/**
	 * True if this device is a mobile browser such as the Safari for the iPhone or Chrome Lite for the Android.
	 */
	boolean isMobileBrowser();

	/**
	 * True if this device is an Apple device such as the Apple iPhone, iPod Touch, or iPad.
	 */
	boolean isApple();
	
}