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

import org.junit.After;
import org.junit.Test;

import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DeviceResolverAutoConfiguration}.
 *
 * @author Roy Clarkson
 * @author Andy Wilkinson
 */
public abstract class AbstractDeviceResolverAutoConfigurationTests {

	protected AnnotationConfigWebApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void deviceResolverHandlerInterceptorCreated() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.register(DeviceResolverAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean(DeviceResolverHandlerInterceptor.class))
				.isNotNull();
	}

	@Test
	public void deviceHandlerMethodArgumentResolverCreated() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.register(DeviceResolverAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBean(DeviceHandlerMethodArgumentResolver.class))
				.isNotNull();
	}

}
