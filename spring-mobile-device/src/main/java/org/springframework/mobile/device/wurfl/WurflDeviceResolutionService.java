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

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.request.DefaultWURFLRequestFactory;
import net.sourceforge.wurfl.core.request.WURFLRequestFactory;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolutionService;

/**
 * WURFL-based device resolution service.
 * WURL provides a comprehensive catalog of Devices and their capabilities.
 * See http://wurfl.sourceforge.net for more information.
 * @author Keith Donald
 */
public class WurflDeviceResolutionService implements DeviceResolutionService {

	private final WURFLManager wurflManager;

	private final WURFLRequestFactory requestFactory;

	/**
	 * Creates a new Wurfl-based device resolution service.
	 * @param wurflManager the central WURFL manager object to delegate to.
	 */
	public WurflDeviceResolutionService(WURFLManager wurflManager) {
		this(wurflManager, new DefaultWURFLRequestFactory());
	}

	/**
	 * Creates a new Wurfl-based device resolution service.
	 * @param wurflManager the central WURFL manager object to delegate to
	 * @param requestFactory a custom WurflRequest factory
	 */
	public WurflDeviceResolutionService(WURFLManager wurflManager, WURFLRequestFactory requestFactory) {
		this.wurflManager = wurflManager;
		this.requestFactory = requestFactory;
	}

	public Device resolveDevice(HttpServletRequest request) {
		return new WurflDevice(wurflManager.getDeviceForRequest(requestFactory.createRequest(request)));
	}

}