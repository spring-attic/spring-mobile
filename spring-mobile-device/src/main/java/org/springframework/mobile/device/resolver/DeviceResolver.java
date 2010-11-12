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
package org.springframework.mobile.device.resolver;

import org.springframework.mobile.device.Device;

/**
 * A resolver, generally for a family of device types, capable of introspecting a request and figuring out which Device it originated from.  
 * @author Keith Donald
 */
public interface DeviceResolver {
	
	/**
	 * Resolve the Device that originated the request.
	 * @param request the request details
	 * @return the resolved Device, or null if the request signature did not match a Device known to this resolver
	 */
	public Device resolveDevice(DeviceRequest request);
	
}
