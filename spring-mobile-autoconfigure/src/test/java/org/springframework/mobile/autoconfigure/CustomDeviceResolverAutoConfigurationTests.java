/*
 * Copyright 2012-2017 the original author or authors.
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

package org.springframework.mobile.autoconfigure;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DeviceResolverAutoConfiguration}.
 *
 * @author Roy Clarkson
 */
public class CustomDeviceResolverAutoConfigurationTests extends AbstractDeviceResolverAutoConfigurationTests {

	@Test
	public void resolveDevice() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		this.context.register(Config.class);
		this.context.refresh();
		DeviceResolverHandlerInterceptor interceptor = this.context.getBean(DeviceResolverHandlerInterceptor.class);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor.preHandle(request, response, null);
		Device device = DeviceUtils.getCurrentDevice(request);
		assertThat(device.isNormal()).isFalse();
		assertThat(device.isMobile()).isTrue();
		assertThat(device.isTablet()).isFalse();
		assertThat(device.getDevicePlatform()).isEqualByComparingTo(DevicePlatform.IOS);
	}

	@Configuration
	@ImportAutoConfiguration({ WebMvcAutoConfiguration.class,
			HttpMessageConvertersAutoConfiguration.class,
			DeviceResolverAutoConfiguration.class,
			PropertyPlaceholderAutoConfiguration.class,
			SpringDataWebAutoConfiguration.class,
			RepositoryRestMvcAutoConfiguration.class })
	protected static class Config {

		@Bean
		public DeviceResolver alwaysIphoneDeviceResolver() {
			return new DeviceResolver() {

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
			};
		}

	}

}
