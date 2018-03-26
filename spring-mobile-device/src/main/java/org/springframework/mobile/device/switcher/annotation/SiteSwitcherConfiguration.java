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

package org.springframework.mobile.device.switcher.annotation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.switcher.SiteSwitcherHandler;
import org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Roy Clarkson
 * @since 2.0
 */
@Configuration
public class SiteSwitcherConfiguration {

	private List<SiteSwitcherConfigurer> siteSwitcherConfigurers = new ArrayList<>();

	@Autowired(required = false)
	public void setSiteSwitcherConfigurers(
			List<SiteSwitcherConfigurer> siteSwitcherConfigurers) {
		Assert.notNull(siteSwitcherConfigurers,
				"At least one configuration class must implement SiteSwitcherConfigurer");
		Assert.notEmpty(siteSwitcherConfigurers,
				"At least one configuration class must implement SiteSwitcherConfigurer");
		this.siteSwitcherConfigurers.addAll(siteSwitcherConfigurers);
	}

	@Bean
	public SiteSwitcherHandlerInterceptor siteSwitcherHandlerInterceptor() {
		for (SiteSwitcherConfigurer configurer : siteSwitcherConfigurers) {
			SiteSwitcherHandler siteSwitcherHandler = configurer.getSiteSwitcherHandler();
			if (siteSwitcherHandler != null) {
				return new SiteSwitcherHandlerInterceptor(siteSwitcherHandler);
			}
		}
		throw new IllegalStateException(
				"One configuration class must implement getSiteSwitcherHandler from SiteSwitcherConfigurer.");
	}

	@Configuration
	protected static class SiteSwitcherMvcConfiguration implements WebMvcConfigurer {

		private final SiteSwitcherHandlerInterceptor siteSwitcherHandlerInterceptor;

		protected SiteSwitcherMvcConfiguration(
				SiteSwitcherHandlerInterceptor siteSwitcherHandlerInterceptor) {
			this.siteSwitcherHandlerInterceptor = siteSwitcherHandlerInterceptor;
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(this.siteSwitcherHandlerInterceptor);
		}

	}

}
