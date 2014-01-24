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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class DeviceResolverRequestFilterTest {
	
	private Device device = new StubDevice();

	private DeviceResolverRequestFilter filter = new DeviceResolverRequestFilter(new DeviceResolver() {
		public Device resolveDevice(HttpServletRequest request) {
			return device;
		}
	});

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private MockFilterChain filterChain = new MockFilterChain();

	@Test
	public void resolve() throws Exception {
		filter.doFilter(request, response, filterChain);
		assertSame(device, DeviceUtils.getCurrentDevice(request));
	}

	@Test
	public void resolveDefaultResolver() throws Exception {
		filter = new DeviceResolverRequestFilter();
		request.addHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7");
		filter.doFilterInternal(request, response, filterChain);
		Device device = DeviceUtils.getCurrentDevice(request);
		assertTrue(device.isMobile());
	}

}
