package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public final class MobileSitePathUrlFactoryTest {
	
	private MobileSitePathUrlFactory rootFactory = new MobileSitePathUrlFactory("/mobile");
	
	private MobileSitePathUrlFactory pathFactory = new MobileSitePathUrlFactory("/mobile", "/demo");

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp() {
		request.setServerName("www.foo.com");
	}
	
	@Test
	public void rootMobilePath() {
		assertEquals("/mobile/", rootFactory.getFullMobilePath());
	}
	
	@Test
	public void pathMobilePath() {
		assertEquals("/demo/mobile/", pathFactory.getFullMobilePath());
	}
	
	@Test
	public void isRequestForRootSite() {
		request.setRequestURI("/mobile/");
		assertTrue(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void isRequestForPathSite() {
		request.setRequestURI("/demo/mobile/");
		assertTrue(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSite() {
		request.setRequestURI("/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSite() {
		request.setRequestURI("/demo/");
		assertFalse(pathFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForRootSiteWithPath() {
		request.setRequestURI("/marvelous/");
		assertFalse(rootFactory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForPathSiteWithPath() {
		request.setRequestURI("/demo/marvelous/");
		assertFalse(pathFactory.isRequestForSite(request));
	}

	@Test
	public void createRootSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/bar");
		assertEquals("http://www.foo.com/mobile/bar", rootFactory.createSiteUrl(request));
	}
	
	@Test
	public void createPathSiteUrl() {
		request.setServerPort(80);
		request.setRequestURI("/demo/bar");
		assertEquals("http://www.foo.com/demo/mobile/bar", pathFactory.createSiteUrl(request));
	}
}
