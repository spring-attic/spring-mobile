/*
 * Copyright 2010-2014 the original author or authors.
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

package org.springframework.mobile.device;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class DeviceHandlerMethodArgumentResolverTest {

	private DeviceHandlerMethodArgumentResolver resolver = new DeviceHandlerMethodArgumentResolver();

	private ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

	private Device device = new StubDevice();

	@Test
	public void resolve() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", Device.class), 0);
		assertTrue(resolver.supportsParameter(parameter));
		Object resolved = resolver.resolveArgument(parameter, null, request, null);
		assertSame(device, resolved);
	}

	@Test
	public void unresolved() throws Exception {
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		assertFalse(resolver.supportsParameter(parameter));
	}

	@RequestMapping
	public void handlerMethod(Device device) {

	}

	@RequestMapping
	public void handlerMethodUnresolved(String foo) {

	}

}
