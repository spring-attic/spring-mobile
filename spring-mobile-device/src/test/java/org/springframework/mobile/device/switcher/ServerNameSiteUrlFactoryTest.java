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

package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class ServerNameSiteUrlFactoryTest {

	private StandardSiteUrlFactory factory = new StandardSiteUrlFactory("m.app.com");
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void isRequestForSite() {
		request.setServerName("m.app.com");
		assertTrue(factory.isRequestForSite(request));
	}

	@Test
	public void notRequestForSite() {
		request.setServerName("app.com");
		assertFalse(factory.isRequestForSite(request));
	}

	@Test
	public void createSiteUrl() {
		request.setServerName("m.app.com");
		request.setServerPort(80);
		request.setRequestURI("/foo");
		assertEquals("http://m.app.com/foo", factory.createSiteUrl(request));
	}

}
