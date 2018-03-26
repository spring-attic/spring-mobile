/*
 * Copyright 2010-2018 the original author or authors.
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

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.switcher.SiteSwitcherHandler;
import org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor;
import org.springframework.mobile.device.switcher.StandardSiteSwitcherHandlerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SiteSwitcherAutoConfiguration}.
 *
 * @author Roy Clarkson
 */
public class SiteSwitcherAutoConfigurationTests {

	protected AnnotationConfigWebApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void siteSwitcherHandlerInterceptorCreated() {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.register(Config.class);
		this.context.refresh();
		assertThat(this.context.getBean(SiteSwitcherHandlerInterceptor.class))
				.isNotNull();
	}

	@Test
	public void siteSwitcherHandlerInterceptorEnabled() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of("spring.mobile.siteswitcher.enabled:true")
				.applyTo(this.context);
		this.context.register(Config.class);
		this.context.refresh();
		assertThat(this.context.getBean(SiteSwitcherHandlerInterceptor.class))
				.isNotNull();
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void siteSwitcherHandlerInterceptorDisabled() {
		this.context = new AnnotationConfigWebApplicationContext();
		TestPropertyValues.of("spring.mobile.siteswitcher.enabled:false")
				.applyTo(this.context);
		this.context.register(Config.class);
		this.context.refresh();
		this.context.getBean(SiteSwitcherHandlerInterceptor.class);
	}

	@Test
	public void siteSwitcherHandlerInterceptorRegistered() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		this.context.register(Config.class, WebMvcAutoConfiguration.class,
				HttpMessageConvertersAutoConfiguration.class,
				SiteSwitcherAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		RequestMappingHandlerMapping mapping = this.context
				.getBean(RequestMappingHandlerMapping.class);
		HandlerInterceptor[] interceptors = mapping
				.getHandler(new MockHttpServletRequest()).getInterceptors();
		assertThat(interceptors)
				.hasAtLeastOneElementOfType(SiteSwitcherHandlerInterceptor.class);
	}

	@Test
	public void resolveCurrentRedirectUrl() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		TestPropertyValues.of("spring.mobile.siteswitcher.enabled:true")
				.applyTo(this.context);
		this.context.register(Config.class);
		this.context.refresh();
			SiteSwitcherHandlerInterceptor interceptor = (SiteSwitcherHandlerInterceptor) this.context
				.getBean("siteSwitcherHandlerInterceptor");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new Device() {
			@Override
			public boolean isMobile() {
				return true;
			}
		});
		interceptor.preHandle(request, response, null);
		assertThat(response.getRedirectedUrl()).isEqualToIgnoringWhitespace("http://m.app.local");
	}

	@Configuration
	@ImportAutoConfiguration(classes = SiteSwitcherAutoConfiguration.class)
	protected static class Config {

		@Bean
		public SiteSwitcherHandler mDotSiteSwitcherHandler() {
			return StandardSiteSwitcherHandlerFactory.mDot("app.local");
		}

		@Bean
		public MyController controller() {
			return new MyController();
		}

	}

	@Controller
	protected static class MyController {

		@RequestMapping("/")
		public void test() {

		}

	}

}
