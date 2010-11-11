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
package org.springframework.mobile.device.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.mobile.device.DeviceResolutionService;
import org.springframework.mobile.device.resolver.lib.AndroidDeviceResolver;
import org.springframework.mobile.device.resolver.lib.AppleDeviceResolver;
import org.springframework.mobile.device.support.GenericDeviceResolutionService;

/**
 * Factory for a standard {@link DeviceResolutionService} implementation that exports the service as a spring bean that can be injected into other beans.
 * Constructs a device resolution service implementation by calling {@link #createDeviceResolutionService()}.
 * @author Keith Donald
 */
public class DefaultDeviceResolutionServiceFactoryBean implements FactoryBean<DeviceResolutionService> {

	public Class<?> getObjectType() {
		return DeviceResolutionService.class;
	}

	public DeviceResolutionService getObject() throws Exception {
		return createDeviceResolutionService();
	}
	
	public boolean isSingleton() {
		return true;
	}

	// subclassing hooks
	
	/**
	 * Create the device resolution service implementation to use in the case where WURFL is not installed.
	 * This implementation registers a Apple and Android device resolver.
	 * Subclasses may override to register their own custom resolvers based on their needs.
	 * @see AppleDeviceResolver
	 * @see AndroidDeviceResolver
	 */
	protected DeviceResolutionService createDeviceResolutionService() {
		GenericDeviceResolutionService service = new GenericDeviceResolutionService();
		service.addDeviceResolver(new AppleDeviceResolver());
		service.addDeviceResolver(new AndroidDeviceResolver());
		return service;
	}

}