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

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.switcher.SiteSwitcherHandlerInterceptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Roy Clarkson
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MdotSiteSwitcherConfiguration.class)
public class MdotSiteSwitcherAnnotationTest {

	@Autowired
	ApplicationContext context;

	@Test
	public void siteSwitcherHandlerInterceptorCreated() {
		assertThat(this.context.getBean("siteSwitcherHandlerInterceptor")).isNotNull();
	}

	@Test
	public void resolveSite() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		SiteSwitcherHandlerInterceptor interceptor = (SiteSwitcherHandlerInterceptor) this.context
				.getBean("siteSwitcherHandlerInterceptor");
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, new Device() {
			@Override
			public boolean isMobile() {
				return true;
			}
		});
		interceptor.preHandle(request, response, null);
		assertThat(response.getRedirectedUrl()).isEqualToIgnoringWhitespace("https://m.server.local");
	}

}
