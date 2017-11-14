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

package org.springframework.mobile.device.site.annotation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.annotation.DeviceResolverConfiguration;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Roy Clarkson
 * @since 2.0
 */
@Configuration
public class SitePreferenceConfiguration {

	private static final Log logger = LogFactory.getLog(DeviceResolverConfiguration.class);

	private List<SitePreferenceConfigurer> sitePreferenceConfigurers = new ArrayList<>();

	@Autowired(required = false)
	public void setSitePreferenceConfigurers(
			List<SitePreferenceConfigurer> sitePreferenceConfigurers) {
		this.sitePreferenceConfigurers.addAll(sitePreferenceConfigurers);
	}

	@Bean
	public SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor() {
		for (SitePreferenceConfigurer configurer : sitePreferenceConfigurers) {
			SitePreferenceHandler sitePreferenceHandler = configurer.getSitePreferenceHandler();
			if (sitePreferenceHandler != null) {
				logger.info("Using custom SitePreferenceHandler");
				return new SitePreferenceHandlerInterceptor(sitePreferenceHandler);
			}
		}
		return new SitePreferenceHandlerInterceptor();
	}

	@Bean
	public SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver() {
		return new SitePreferenceHandlerMethodArgumentResolver();
	}

	@Configuration
	protected static class SitePreferenceMvcConfiguration implements WebMvcConfigurer {

		private final SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor;

		private final SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver;

		protected SitePreferenceMvcConfiguration(
				SitePreferenceHandlerInterceptor sitePreferenceHandlerInterceptor,
				SitePreferenceHandlerMethodArgumentResolver sitePreferenceHandlerMethodArgumentResolver) {
			this.sitePreferenceHandlerInterceptor = sitePreferenceHandlerInterceptor;
			this.sitePreferenceHandlerMethodArgumentResolver = sitePreferenceHandlerMethodArgumentResolver;
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(this.sitePreferenceHandlerInterceptor);
		}

		@Override
		public void addArgumentResolvers(
				List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(this.sitePreferenceHandlerMethodArgumentResolver);
		}

	}

}
