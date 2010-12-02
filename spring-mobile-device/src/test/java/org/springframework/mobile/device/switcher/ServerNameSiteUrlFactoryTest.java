package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class ServerNameSiteUrlFactoryTest {

	private StandardSiteUrlFactory factory = new StandardSiteUrlFactory("m.app.com");
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	
	@Test
	public void isRequestForSite() {
		request.setServerName("m.app.com");
		assertTrue(factory.isRequestForSite(request));
	}

	@Test
	public void notRequestForSite() {
		request.setServerName("app.com");
		assertFalse(factory.isRequestForSite(request));
	}

	@Test
	public void createSiteUrl() {
		request.setServerName("m.app.com");
		request.setServerPort(80);
		request.setRequestURI("/foo");
		assertEquals("http://m.app.com/foo", factory.createSiteUrl(request));
	}

}