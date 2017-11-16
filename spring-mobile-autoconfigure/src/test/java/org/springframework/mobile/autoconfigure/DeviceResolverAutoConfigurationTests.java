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

import org.junit.Test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link DeviceResolverAutoConfiguration}.
 *
 * @author Roy Clarkson
 * @author Andy Wilkinson
 */
public class DeviceResolverAutoConfigurationTests extends AbstractDeviceResolverAutoConfigurationTests {

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
		assertThat(device.isNormal()).isTrue();
		assertThat(device.isMobile()).isFalse();
		assertThat(device.isTablet()).isFalse();
		assertThat(device.getDevicePlatform()).isEqualByComparingTo(DevicePlatform.UNKNOWN);
	}

	@Test
	public void deviceResolverHandlerInterceptorRegistered() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		this.context.register(Config.class);
		this.context.refresh();
		RequestMappingHandlerMapping mapping = this.context
				.getBean(RequestMappingHandlerMapping.class);
		HandlerInterceptor[] interceptors = mapping
				.getHandler(new MockHttpServletRequest()).getInterceptors();
		assertThat(interceptors)
				.hasAtLeastOneElementOfType(DeviceResolverHandlerInterceptor.class);
	}

	@Test
	public void deviceHandlerMethodArgumentWorksWithSpringData() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.register(Config.class);
		this.context.setServletContext(new MockServletContext());
		this.context.refresh();
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		mockMvc.perform(get("/")).andExpect(status().isOk());
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
		public MyController controller() {
			return new MyController();
		}

	}

	@Controller
	protected static class MyController {

		@RequestMapping("/")
		public ResponseEntity<Void> test(Device device) {
			if (device.getDevicePlatform() != null) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
