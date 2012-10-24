package org.springframework.mobile.device.switcher;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public abstract class AbstractSitePathUrlFactoryTest {

	protected AbstractSitePathUrlFactory basicRootFactory;

	protected AbstractSitePathUrlFactory advRootFactory;

	protected AbstractSitePathUrlFactory basicPathFactory;

	protected AbstractSitePathUrlFactory advPathFactory;

	protected MockHttpServletRequest request = new MockHttpServletRequest();

	@Before
	public void setUp() {
		request.setServerName("www.app.com");
	}

	@Test
	public void rootFullNormalPath() {
		assertEquals("/", basicRootFactory.getFullNormalPath());
	}

	@Test
	abstract void rootFullTabletPath();

	@Test
	abstract void rootFullMobilePath();

	@Test
	public void rootCleanNormalPath() {
		assertEquals("", basicRootFactory.getCleanNormalPath());
	}

	@Test
	abstract void rootCleanTabletPath();

	@Test
	abstract void rootCleanMobilePath();

	@Test
	public void pathFullNormalPath() {
		assertEquals("/showcase/", basicPathFactory.getFullNormalPath());
	}

	@Test
	abstract void pathFullTabletPath();

	@Test
	abstract void pathFullMobilePath();

	@Test
	public void pathCleanNormalPath() {
		assertEquals("/showcase", basicPathFactory.getCleanNormalPath());
	}

}
