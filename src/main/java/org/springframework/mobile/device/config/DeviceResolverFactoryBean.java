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

import net.sourceforge.wurfl.core.WURFLManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.mobile.device.DeviceResolutionService;
import org.springframework.mobile.device.support.AndroidDeviceResolver;
import org.springframework.mobile.device.support.AppleDeviceResolver;
import org.springframework.mobile.device.support.GenericDeviceResolutionService;
import org.springframework.mobile.device.wurfl.WurflDeviceResolutionService;
import org.springframework.util.ClassUtils;

/**
 * Factory for a {@link DeviceResolutionService} that exports the service as a spring bean that can be injected into other beans.
 * If WURFL is present in the classpath, assumes you wish to use WURFL for device resolution and automatically creates a {@link WurflDeviceResolutionService}.
 * This requires exactly one {@link WURFLManager} to exist in the {@link BeanFactory} hosting this factory bean.
 * If WURFL is not in the classpath, constructs a device resolution service implementation by calling {@link #createDeviceResolutionService()}.
 * @author Keith Donald
 */
public class DeviceResolverFactoryBean implements FactoryBean<DeviceResolutionService>, BeanFactoryAware {

	private static final boolean wurflPresent = ClassUtils.isPresent("net.sourceforge.wurfl.core.WURFLManager;", DeviceResolverFactoryBean.class.getClassLoader());
	
	private BeanFactory beanFactory;

	// implementing BeanFactoryAware
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	// implementing FactoryBean

	public Class<?> getObjectType() {
		return DeviceResolutionService.class;
	}

	public DeviceResolutionService getObject() throws Exception {
		if (wurflPresent) {
			return new WurflDeviceResolutionService(beanFactory.getBean(WURFLManager.class));			
		} else {
			return createDeviceResolutionService();
		}
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