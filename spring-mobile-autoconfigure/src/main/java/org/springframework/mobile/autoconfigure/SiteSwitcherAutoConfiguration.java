/*
 * Copyright 2010-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mobile.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.site.annotation.EnableSitePreference;
import org.springframework.mobile.device.site.annotation.SitePreferenceConfigurer;
import org.springframework.mobile.device.switcher.SiteSwitcherHandler;
import org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor;
import org.springframework.mobile.device.switcher.annotation.EnableSiteSwitcher;
import org.springframework.mobile.device.switcher.annotation.SiteSwitcherConfigurer;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Mobile's
 * {@link SiteSwitcherHandler}. The site switcher feature depends on a
 * {@link DeviceResolver} first being registered.
 *
 * @author Roy Clarkson
 * @since 2.0
 */
@Configuration
@ConditionalOnClass(SiteSwitcherHandlerInterceptor.class)
@AutoConfigureAfter(DeviceResolverAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.mobile.siteswitcher", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.SERVLET)
public class SiteSwitcherAutoConfiguration {

	@Configuration
	@ConditionalOnBean(SiteSwitcherHandler.class)
	@ConditionalOnWebApplication(type = Type.SERVLET)
	@EnableSiteSwitcher
	protected static class SiteSwitcherConfigurationAdapter implements SiteSwitcherConfigurer {

		private SiteSwitcherHandler siteSwitcherHandler;

		public SiteSwitcherConfigurationAdapter(SiteSwitcherHandler siteSwitcherHandler) {
			this.siteSwitcherHandler = siteSwitcherHandler;
		}

		@Override
		public SiteSwitcherHandler getSiteSwitcherHandler() {
			return this.siteSwitcherHandler;
		}
	}

}
