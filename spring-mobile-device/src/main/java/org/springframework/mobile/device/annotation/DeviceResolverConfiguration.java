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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Roy Clarkson
 * @since 2.0
 */
@Configuration
public class DeviceResolverConfiguration {

	private static final Log logger = LogFactory.getLog(DeviceResolverConfiguration.class);

	private List<DeviceResolverConfigurer> deviceResolverConfigurers = new ArrayList<>();

	@Autowired(required = false)
	public void setDeviceResolverConfigurers(
			List<DeviceResolverConfigurer> deviceResolverConfigurers) {
		this.deviceResolverConfigurers.addAll(deviceResolverConfigurers);
	}

	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		for (DeviceResolverConfigurer configurer : deviceResolverConfigurers) {
			DeviceResolver deviceResolver = configurer.getDeviceResolver();
			if (deviceResolver != null) {
				logger.info("Using custom DeviceResolver");
				return new DeviceResolverHandlerInterceptor(deviceResolver);
			}
		}
		return new DeviceResolverHandlerInterceptor();
	}

	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}

	@Configuration
	@Order(0)
	protected static class DeviceResolverMvcConfiguration implements WebMvcConfigurer {

		private DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor;

		private DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver;

		protected DeviceResolverMvcConfiguration(
				DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor,
				DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver) {
			this.deviceResolverHandlerInterceptor = deviceResolverHandlerInterceptor;
			this.deviceHandlerMethodArgumentResolver = deviceHandlerMethodArgumentResolver;
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(this.deviceResolverHandlerInterceptor);
		}

		@Override
		public void addArgumentResolvers(
				List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(this.deviceHandlerMethodArgumentResolver);
		}

	}

}
