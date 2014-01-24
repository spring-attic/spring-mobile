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

import org.junit.Before;
import org.junit.Test;

public final class NormalSitePathUrlFactoryTest extends AbstractSitePathUrlFactoryTest {

	@Before
	public void setUp() {
		super.setUp();
		this.basicRootFactory = new NormalSitePathUrlFactory("/mob", "/tab", null);
		this.basicPathFactory = new NormalSitePathUrlFactory("/mob", "/tab", "/showcase");
	}

	@Test
	public void rootFullMobilePath() {
		assertEquals("/mob/", basicRootFactory.getFullMobilePath());
	}

	@Test
	public void rootFullTabletPath() {
		assertEquals("/tab/", basicRootFactory.getFullTabletPath());
	}

	@Test
	public void rootCleanMobilePath() {
		assertEquals("/mob", basicRootFactory.getCleanMobilePath());
	}

	@Test
	public void rootCleanTabletPath() {
		assertEquals("/tab", basicRootFactory.getCleanTabletPath());
	}

	@Test
	public void pathFullMobilePath() {
		assertEquals("/showcase/mob/", basicPathFactory.getFullMobilePath());
	}

	@Test
	public void pathFullTabletPath() {
		assertEquals("/showcase/tab/", basicPathFactory.getFullTabletPath());
	}

	@Test
	public void pathCleanMobilePath() {
		assertEquals("/showcase/mob", basicPathFactory.getCleanMobilePath());
	}

	@Test
	public void pathCleanTabletPath() {
		assertEquals("/showcase/tab", basicPathFactory.getCleanTabletPath());
	}

	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/");
		assertTrue(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/showcase/");
		assertTrue(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteMobile() {
		request.setRequestURI("/mob/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteTablet() {
		request.setRequestURI("/tab/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteMobile() {
		request.setRequestURI("/showcase/mob/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteTablet() {
		request.setRequestURI("/showcase/tab/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteWithPathMobile() {
		request.setRequestURI("/mob/marvelous/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteWithPathTablet() {
		request.setRequestURI("/tab/marvelous/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteWithPathMobile() {
		request.setRequestURI("/showcase/mob/marvelous/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteWithPathTablet() {
		request.setRequestURI("/showcase/tab/marvelous/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrlMobile() {
		request.setServerPort(80);
		request.setRequestURI("/mob/foo");
		assertEquals("http://www.app.com/foo", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlTablet() {
		request.setServerPort(80);
		request.setRequestURI("/tab/foo");
		assertEquals("http://www.app.com/foo", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlMobilePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/mob/foo");
		assertEquals("http://www.app.com:8080/foo", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlTabletPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/tab/foo");
		assertEquals("http://www.app.com:8080/foo", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlMobile() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mob/foo");
		assertEquals("http://www.app.com/showcase/foo", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlTablet() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/tab/foo");
		assertEquals("http://www.app.com/showcase/foo", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlMobilePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/mob/foo");
		assertEquals("http://www.app.com:8080/showcase/foo", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlTabletPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/tab/foo");
		assertEquals("http://www.app.com:8080/showcase/foo", basicPathFactory.createSiteUrl(request));
	}

}
