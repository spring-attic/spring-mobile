package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public final class NormalSitePathUrlFactoryTest {
	
	private NormalSitePathUrlFactory factory = new NormalSitePathUrlFactory("/m");

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void isRequestForSite() {
		request.setServerName("www.app.com");
		request.setRequestURI("/");
		assertTrue(factory.isRequestForSite(request));
	}
	
	@Test
	public void isRequestForSiteWithPath() {
		request.setServerName("www.app.com");
		request.setRequestURI("/marvelous/");
		assertTrue(factory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForSite() {
		request.setServerName("www.app.com");
		request.setRequestURI("/m/");
		assertFalse(factory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForSiteWithPath() {
		request.setServerName("www.app.com");
		request.setRequestURI("/m/marvelous/");
		assertFalse(factory.isRequestForSite(request));
	}

	@Test
	public void createSiteUrl() {
		request.setServerName("www.app.com");
		request.setServerPort(80);
		request.setRequestURI("/m/foo");
		assertEquals("http://www.app.com/foo", factory.createSiteUrl(request));
	}
}
