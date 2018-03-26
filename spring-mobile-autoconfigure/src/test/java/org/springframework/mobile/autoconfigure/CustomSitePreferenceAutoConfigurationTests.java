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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SitePreferenceAutoConfiguration}.
 *
 * @author Roy Clarkson
 */
public class CustomSitePreferenceAutoConfigurationTests
		extends AbstractSitePreferenceAutoConfigurationTests {

	@Test
	public void resolveCurrentSitePreference() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		TestPropertyValues.of("spring.mobile.sitepreference.enabled:true")
				.applyTo(this.context);
		this.context.register(Config.class);
		this.context.refresh();
		SitePreferenceHandlerInterceptor interceptor = (SitePreferenceHandlerInterceptor) this.context
				.getBean("sitePreferenceHandlerInterceptor");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		interceptor.preHandle(request, response, null);
		assertThat(SitePreferenceUtils.getCurrentSitePreference(request)).isEqualByComparingTo(SitePreference.MOBILE);
	}

	@Configuration
	@ImportAutoConfiguration(classes = SitePreferenceAutoConfiguration.class)
	protected static class Config {

		@Bean
		public SitePreferenceHandler alwaysMobileSitePreferenceHandler() {
			return new SitePreferenceHandler() {

				@Override
				public SitePreference handleSitePreference(HttpServletRequest request,
						HttpServletResponse response) {
					request.setAttribute("currentSitePreference", SitePreference.MOBILE);
					return SitePreference.MOBILE;
				}

			};
		}

	}

}
