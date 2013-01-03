/*
 * Copyright 2010-2013 the original author or authors.
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

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractSitePathUrlFactoryTest {

	protected AbstractSitePathUrlFactory basicRootFactory;

	protected AbstractSitePathUrlFactory advRootFactory;

	protected AbstractSitePathUrlFactory basicPathFactory;

	protected AbstractSitePathUrlFactory advPathFactory;

	protected MockHttpServletRequest request = new MockHttpServletRequest();

	@Before
	public void setUp() {
		request.setServerName("www.app.com");
	}

	@Test
	public void rootFullNormalPath() {
		assertEquals("/", basicRootFactory.getFullNormalPath());
	}

	@Test
	abstract void rootFullTabletPath();

	@Test
	abstract void rootFullMobilePath();

	@Test
	public void rootCleanNormalPath() {
		assertEquals("", basicRootFactory.getCleanNormalPath());
	}

	@Test
	abstract void rootCleanTabletPath();

	@Test
	abstract void rootCleanMobilePath();

	@Test
	public void pathFullNormalPath() {
		assertEquals("/showcase/", basicPathFactory.getFullNormalPath());
	}

	@Test
	abstract void pathFullTabletPath();

	@Test
	abstract void pathFullMobilePath();

	@Test
	public void pathCleanNormalPath() {
		assertEquals("/showcase", basicPathFactory.getCleanNormalPath());
	}

}
