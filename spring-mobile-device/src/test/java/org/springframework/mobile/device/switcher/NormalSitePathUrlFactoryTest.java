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
		this.rootFactory = new NormalSitePathUrlFactory("/mob", "/tab", null);
		this.pathFactory = new NormalSitePathUrlFactory("/mob", "/tab", "/showcase");
	}

	@Test
	public void rootFullMobilePath() {
		assertEquals("/mob/", rootFactory.getFullMobilePath());
	}

	@Test
	public void rootFullTabletPath() {
		assertEquals("/tab/", rootFactory.getFullTabletPath());
	}

	@Test
	public void rootCleanMobilePath() {
		assertEquals("/mob", rootFactory.getCleanMobilePath());
	}

	@Test
	public void rootCleanTabletPath() {
		assertEquals("/tab", rootFactory.getCleanTabletPath());
	}

	@Test
	public void pathFullMobilePath() {
		assertEquals("/showcase/mob/", pathFactory.getFullMobilePath());
	}

	@Test
	public void pathFullTabletPath() {
		assertEquals("/showcase/tab/", pathFactory.getFullTabletPath());
	}
	
	@Test
	public void pathCleanMobilePath() {
		assertEquals("/showcase/mob", pathFactory.getCleanMobilePath());
	}
	
	@Test
	public void pathCleanTabletPath() {
		assertEquals("/showcase/tab", pathFactory.getCleanTabletPath());
	}

	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/");
		assertTrue(rootFactory.isRequestForSite(request));
	}

	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/showcase/");
		assertTrue(pathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSite() {
		request.setRequestURI("/mob/");
		assertFalse(rootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSite() {
		request.setRequestURI("/showcase/mob/");
		assertFalse(pathFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForRootSiteWithPath() {
		request.setRequestURI("/mob/marvelous/");
		assertFalse(rootFactory.isRequestForSite(request));
	}

	@Test
	public void notRequestForPathSiteWithPath() {
		request.setRequestURI("/showcase/mob/marvelous/");
		assertFalse(pathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/mob/foo");
		assertEquals("http://www.app.com/foo", rootFactory.createSiteUrl(request));
	}

	@Test
	public void createRootSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/mob/foo");
		assertEquals("http://www.app.com:8080/foo", rootFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/mob/foo");
		assertEquals("http://www.app.com/showcase/foo", pathFactory.createSiteUrl(request));
	}

	@Test
	public void createPathSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/mob/foo");
		assertEquals("http://www.app.com:8080/showcase/foo", pathFactory.createSiteUrl(request));
	}
}
