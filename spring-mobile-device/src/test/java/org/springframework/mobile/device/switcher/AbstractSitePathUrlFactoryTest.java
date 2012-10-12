package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractSitePathUrlFactoryTest {
		
	protected AbstractSitePathUrlFactory rootFactory;
	
	protected AbstractSitePathUrlFactory pathFactory;

	protected MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Before
	public void setUp() {
		request.setServerName("www.app.com");
	}
	
	@Test
	public void rootFullNormalPath() {
		assertEquals("/", rootFactory.getFullNormalPath());
	}
	
	@Test
	abstract void rootFullTabletPath();
	
	@Test
	abstract void rootFullMobilePath();
	
	@Test
	public void rootCleanNormalPath() {
		assertEquals("", rootFactory.getCleanNormalPath());
	}
	
	@Test
	abstract void rootCleanTabletPath();
	
	@Test
	abstract void rootCleanMobilePath();
	
	@Test
	public void pathFullNormalPath() {
		assertEquals("/showcase/", pathFactory.getFullNormalPath());
	}
	
	@Test
	abstract void pathFullTabletPath();
	
	@Test
	abstract void pathFullMobilePath();
	
	@Test
	public void pathCleanNormalPath() {
		assertEquals("/showcase", pathFactory.getCleanNormalPath());
	}
	
	@Test
	abstract void pathCleanTabletPath();
	
	@Test
	abstract void pathCleanMobilePath();
	
	@Test
	abstract void isRequestForRootSite();
	
	@Test
	abstract void isRequestForPathSite();
	
	@Test
	abstract void notRequestForRootSite();
	
	@Test
	abstract void notRequestForPathSite();
	
	@Test
	abstract void notRequestForRootSiteWithPath();
	
	@Test
	abstract void notRequestForPathSiteWithPath();

	@Test
	abstract void createRootSiteUrl();
	
	@Test
	abstract void createRootSiteUrlPort8080();
	
	@Test
	abstract void createPathSiteUrl();
	
	@Test
	abstract void createPathSiteUrlPort8080();
}
