package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public final class NormalSitePathUrlFactoryTest {
		
	private NormalSitePathUrlFactory rootFactory = new NormalSitePathUrlFactory("/m");
	
	private NormalSitePathUrlFactory pathFactory = new NormalSitePathUrlFactory("/m", "/showcase");

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp() {
		request.setServerName("www.app.com");
	}
	
	@Test
	public void rootMobilePath() {
		assertEquals("/m/", rootFactory.getFullMobilePath());
	}
	
	@Test
	public void pathMobilePath() {
		assertEquals("/showcase/m/", pathFactory.getFullMobilePath());
	}
	
	@Test
	public void isRequestForSite() {
		request.setRequestURI("/");
		assertTrue(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void isRequestForRootSiteWithPath() {
		request.setRequestURI("/marvelous/");
		assertTrue(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void isRequestForPathSiteWithPath() {
		request.setRequestURI("/showcase/marvelous/");
		assertTrue(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSite() {
		request.setRequestURI("/m/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSite() {
		request.setRequestURI("/showcase/m/");
		assertFalse(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSiteWithPath() {
		request.setRequestURI("/m/marvelous/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSiteWithPath() {
		request.setRequestURI("/showcase/m/marvelous/");
		assertFalse(pathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/m/foo");
		assertEquals("http://www.app.com/foo", rootFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/showcase/m/foo");
		assertEquals("http://www.app.com/showcase/foo", pathFactory.createSiteUrl(request));
	}
}
