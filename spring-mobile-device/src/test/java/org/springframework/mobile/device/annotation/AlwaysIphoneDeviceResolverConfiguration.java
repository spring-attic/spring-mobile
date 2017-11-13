/*
 * Copyright 2010-2017 the original author or authors.
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

package org.springframework.mobile.device.annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceResolver;

@Configuration
@EnableDeviceResolver
public class AlwaysIphoneDeviceResolverConfiguration implements DeviceResolverConfigurer {

	@Nullable
	@Override
	public DeviceResolver getDeviceResolver() {
		return new AlwaysIphoneDeviceResolver();
	}

	private static class AlwaysIphoneDeviceResolver implements DeviceResolver {

		@Override
		public Device resolveDevice(HttpServletRequest request) {
			return new Device() {
				@Override
				public boolean isNormal() {
					return false;
				}

				@Override
				public boolean isMobile() {
					return true;
				}

				@Override
				public boolean isTablet() {
					return false;
				}

				@Override
				public DevicePlatform getDevicePlatform() {
					return DevicePlatform.IOS;
				}
			};
		}
	}

}
