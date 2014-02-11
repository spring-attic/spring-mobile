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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public final class TabletSitePathUrlFactoryTest extends AbstractSitePathUrlFactoryTest {

	@Before
	public void setUp() {
		super.setUp();
		this.basicRootFactory = new TabletSitePathUrlFactory("/tablet", null);
		this.advRootFactory = new TabletSitePathUrlFactory("/tablet", "/mobile");
		this.basicPathFactory = new TabletSitePathUrlFactory("/tablet", null, "/showcase");
		this.advPathFactory = new TabletSitePathUrlFactory("/tablet", "/mobile", "/showcase");
	}

	@Test
	public void rootFullTabletPath() {
		assertEquals("/tablet/", basicRootFactory.getFullTabletPath());
	}

	@Test
	public void rootFullMobilePath() {
		assertNull(basicRootFactory.getFullMobilePath());
	}

	@Test
	public void rootCleanTabletPath() {
		assertEquals("/tablet", basicRootFactory.getCleanTabletPath());
	}

	@Test
	public void rootCleanMobilePath() {
		assertNull(basicRootFactory.getCleanMobilePath());
	}

	@Test
	public void pathFullTabletPath() {
		assertEquals("/showcase/tablet/", basicPathFactory.getFullTabletPath());
	}

	@Test
	public void pathFullMobilePath() {
		assertNull(basicPathFactory.getFullMobilePath());
	}

	@Test
	public void pathCleanTabletPath() {
		assertEquals("/showcase/tablet", basicPathFactory.getCleanTabletPath());
	}

	@Test
	public void pathCleanMobilePath() {
		assertNull(basicPathFactory.getCleanMobilePath());
	}

	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/tablet/");
		assertTrue(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/showcase/tablet/");
		assertTrue(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSite() {
		request.setRequestURI("/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSite() {
		request.setRequestURI("/showcase/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteWithPath() {
		request.setRequestURI("/marvelous/");
		assertFalse(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteWithPath() {
		request.setRequestURI("/showcase/marvelous/");
		assertFalse(basicPathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/bar");
		assertEquals("http://www.app.com/tablet/bar", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/bar");
		assertEquals("http://www.app.com:8080/tablet/bar", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com/showcase/tablet/bar", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com:8080/showcase/tablet/bar", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlFromMobileSite() {
		request.setServerPort(80);
		request.setRequestURI("/mobile/about");
		assertEquals("http://www.app.com/tablet/about", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlFromMobileSitePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/mobile/about");
		assertEquals("http://www.app.com:8080/tablet/about", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createLongRootSiteUrlFromMobileSite() {
		request.setServerPort(80);
		request.setRequestURI("/mobile/about/x/y/z/contact");
		assertEquals("http://www.app.com/tablet/about/x/y/z/contact", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlFromMobileSite() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mobile/about");
		assertEquals("http://www.app.com/showcase/tablet/about", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlFromMobileSitePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/mobile/about");
		assertEquals("http://www.app.com:8080/showcase/tablet/about", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromMobileSite() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mobile/about/x/y/z/contact");
		assertEquals("http://www.app.com/showcase/tablet/about/x/y/z/contact", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromMobileSiteMultipleMobile() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mobile/about/x/mobile/y/z/contact");
		assertEquals("http://www.app.com/showcase/tablet/about/x/mobile/y/z/contact",
				advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromMobileSiteMultipleMobileAndSlashes() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mobile/about//x/mobile////y/z/contact///");
		assertEquals("http://www.app.com/showcase/tablet/about//x/mobile////y/z/contact///",
				advPathFactory.createSiteUrl(request));
	}

}
