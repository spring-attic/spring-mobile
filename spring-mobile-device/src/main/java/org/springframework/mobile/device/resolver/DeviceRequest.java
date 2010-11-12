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

/**
 * Models a request made by a yet-to-be resolved Device.
 * Forms the input into device resolution using a {@link DeviceResolver}.
 * @author Keith Donald
 */
public interface DeviceRequest {
	
	/**
	 * The user agent of the device.
	 * Typically the value of the User-Agent request header, though not necessarily (proxies can cause the device header arrangement to vary).
	 * The raw header value may have been normalized when this request was constructed to help simplify the device resolution algorithm.
	 */
	String getUserAgent();
	
}