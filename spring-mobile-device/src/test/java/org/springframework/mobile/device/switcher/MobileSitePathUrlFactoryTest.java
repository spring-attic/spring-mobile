package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public final class MobileSitePathUrlFactoryTest {
	
	private MobileSitePathUrlFactory factory = new MobileSitePathUrlFactory("/m");

	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void isRequestForSite() {
		request.setServerName("www.app.com");
		request.setRequestURI("/m/");
		assertTrue(factory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForSite() {
		request.setServerName("www.app.com");
		request.setRequestURI("/");
		assertFalse(factory.isRequestForSite(request));
	}
	
	@Test
	public void notRequestForSiteWithPath() {
		request.setServerName("www.app.com");
		request.setRequestURI("/marvelous/");
		assertFalse(factory.isRequestForSite(request));
	}

	@Test
	public void createSiteUrl() {
		request.setServerName("www.app.com");
		request.setServerPort(80);
		request.setRequestURI("/foo");
		assertEquals("http://www.app.com/m/foo", factory.createSiteUrl(request));
	}
}
