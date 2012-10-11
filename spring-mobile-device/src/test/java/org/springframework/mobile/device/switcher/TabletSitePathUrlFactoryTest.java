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
		this.rootFactory = new TabletSitePathUrlFactory("/tablet");
		this.pathFactory = new TabletSitePathUrlFactory("/tablet", "/showcase");
	}
	
	@Test
	public void rootFullTabletPath() {
		assertEquals("/tablet/", rootFactory.getFullTabletPath());
	}
	
	@Test
	public void rootFullMobilePath() {
		assertNull(rootFactory.getFullMobilePath());
	}
		
	@Test
	public void rootCleanTabletPath() {
		assertEquals("/tablet", rootFactory.getCleanTabletPath());
	}
	
	@Test
	public void rootCleanMobilePath() {
		assertNull(rootFactory.getCleanMobilePath());
	}
	
	@Test
	public void pathFullTabletPath() {
		assertEquals("/showcase/tablet/", pathFactory.getFullTabletPath());
	}
	
	@Test
	public void pathFullMobilePath() {
		assertNull(pathFactory.getFullMobilePath());
	}
	
	@Test
	public void pathCleanTabletPath() {
		assertEquals("/showcase/tablet", pathFactory.getCleanTabletPath());
	}
	
	@Test
	public void pathCleanMobilePath() {
		assertNull(pathFactory.getCleanMobilePath());
	}
	
	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/tablet/");
		assertTrue(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/showcase/tablet/");
		assertTrue(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSite() {
		request.setRequestURI("/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSite() {
		request.setRequestURI("/showcase/");
		assertFalse(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSiteWithPath() {
		request.setRequestURI("/marvelous/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSiteWithPath() {
		request.setRequestURI("/showcase/marvelous/");
		assertFalse(pathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/bar");
		assertEquals("http://www.app.com/tablet/bar", rootFactory.createSiteUrl(request));
	}
	
	@Test
	public void createRootSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/bar");
		assertEquals("http://www.app.com:8080/tablet/bar", rootFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com/showcase/tablet/bar", pathFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrlPort8080() {
		request.setServerPort(8080);
		request.setRequestURI("/showcase/bar");
		assertEquals("http://www.app.com:8080/showcase/tablet/bar", pathFactory.createSiteUrl(request));
	}
}
