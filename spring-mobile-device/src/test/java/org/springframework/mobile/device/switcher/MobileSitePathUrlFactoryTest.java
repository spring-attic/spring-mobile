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

public final class MobileSitePathUrlFactoryTest extends AbstractSitePathUrlFactoryTest {

	@Before
	public void setUp() {
		super.setUp();
		this.basicRootFactory = new MobileSitePathUrlFactory("/mobile", null);
		this.advRootFactory = new MobileSitePathUrlFactory("/mobile", "/tablet");
		this.basicPathFactory = new MobileSitePathUrlFactory("/mobile", null, "/showcase");
		this.advPathFactory = new MobileSitePathUrlFactory("/mobile", "/tablet", "/showcase");
	}

	@Test
	public void rootFullNormalPath() {
		assertEquals("/", basicRootFactory.getFullNormalPath());
	}

	@Test
	public void rootFullMobilePath() {
		assertEquals("/mobile/", basicRootFactory.getFullMobilePath());
	}

	@Test
	public void rootFullTabletPath() {
		assertNull(basicRootFactory.getFullTabletPath());
	}

	@Test
	public void rootCleanNormalPath() {
		assertEquals("", basicRootFactory.getCleanNormalPath());
	}

	@Test
	public void rootCleanMobilePath() {
		assertEquals("/mobile", basicRootFactory.getCleanMobilePath());
	}

	@Test
	public void rootCleanTabletPath() {
		assertNull(basicRootFactory.getCleanTabletPath());
	}

	@Test
	public void pathFullNormalPath() {
		assertEquals("/showcase/", basicPathFactory.getFullNormalPath());
	}

	@Test
	public void pathFullMobilePath() {
		assertEquals("/showcase/mobile/", basicPathFactory.getFullMobilePath());
	}

	@Test
	public void pathFullTabletPath() {
		assertNull(basicPathFactory.getFullTabletPath());
	}

	@Test
	public void pathCleanNormalPath() {
		assertEquals("/showcase", basicPathFactory.getCleanNormalPath());
	}

	@Test
	public void pathCleanMobilePath() {
		assertEquals("/showcase/mobile", basicPathFactory.getCleanMobilePath());
	}

	@Test
	public void pathCleanTabletPath() {
		assertNull(basicPathFactory.getCleanTabletPath());
	}

	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/mobile/");
		assertTrue(basicRootFactory.isRequestForSite(request));
	}

	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/showcase/mobile/");
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
		assertEquals("http://www.app.com/mobile/bar", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/bar");
		assertEquals("http://www.app.com:8080/mobile/bar", basicRootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com/showcase/mobile/bar", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com:8080/showcase/mobile/bar", basicPathFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrlPort8080withQueryString() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/bar?xval=123&yval=456");
		assertEquals("http://www.app.com:8080/showcase/mobile/bar?xval=123&yval=456", basicPathFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlFromTabletSite() {
		request.setServerPort(80);
		request.setRequestURI("/tablet/about");
		assertEquals("http://www.app.com/mobile/about", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlFromTabletSitePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/tablet/about");
		assertEquals("http://www.app.com:8080/mobile/about", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createLongRootSiteUrlFromTabletSite() {
		request.setServerPort(80);
		request.setRequestURI("/tablet/about/x/y/z/contact");
		assertEquals("http://www.app.com/mobile/about/x/y/z/contact", advRootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlFromTabletSite() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/tablet/about");
		assertEquals("http://www.app.com/showcase/mobile/about", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlFromTabletSitePort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/tablet/about");
		assertEquals("http://www.app.com:8080/showcase/mobile/about", advPathFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrlFromTabletSitePort8080withQueryString() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/tablet/about?x=123");
		assertEquals("http://www.app.com:8080/showcase/mobile/about?x=123", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromTabletSite() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/tablet/about/x/y/z/contact");
		assertEquals("http://www.app.com/showcase/mobile/about/x/y/z/contact", advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromTabletSiteMultipleTablet() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/tablet/about/x/y/tablet/z/contact");
		assertEquals("http://www.app.com/showcase/mobile/about/x/y/tablet/z/contact",
				advPathFactory.createSiteUrl(request));
	}

	@Test
	public void createLongPathSiteUrlFromTabletSiteMultipleTabletAndSlashes() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/tablet//about/x/y/tablet///z/contact//");
		assertEquals("http://www.app.com/showcase/mobile//about/x/y/tablet///z/contact//",
				advPathFactory.createSiteUrl(request));
	}

}
