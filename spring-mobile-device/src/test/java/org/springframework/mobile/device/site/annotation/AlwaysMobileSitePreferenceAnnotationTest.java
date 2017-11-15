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

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Roy Clarkson
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AlwaysMobileSitePreferenceConfiguration.class)
public class AlwaysMobileSitePreferenceAnnotationTest {

	@Autowired
	ApplicationContext context;

	@Test
	public void sitePreferenceHandlerInterceptorCreated() {
		assertThat(this.context.getBean("sitePreferenceHandlerInterceptor")).isNotNull();
	}

	@Test
	public void sitePreferenceHandlerMethodArgumentResolverCreated() {
		assertThat(this.context.getBean("sitePreferenceHandlerMethodArgumentResolver"))
				.isNotNull();
	}

	@Test
	public void resolveSitePreference() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		SitePreferenceHandlerInterceptor interceptor = (SitePreferenceHandlerInterceptor) this.context
				.getBean("sitePreferenceHandlerInterceptor");
		interceptor.preHandle(request, response, null);
		assertThat(request.getAttribute("currentSitePreference")).isEqualTo(SitePreference.MOBILE);
	}

}
