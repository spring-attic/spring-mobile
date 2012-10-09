package org.springframework.mobile.device.site;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SitePreferenceTest {

	@Test
	public void normal() {
		assertTrue(SitePreference.NORMAL.isNormal());
		assertFalse(SitePreference.NORMAL.isMobile());
		assertFalse(SitePreference.NORMAL.isTablet());
	}

	@Test
	public void mobile() {
		assertFalse(SitePreference.MOBILE.isNormal());
		assertTrue(SitePreference.MOBILE.isMobile());
		assertFalse(SitePreference.MOBILE.isTablet());
	}
	
	@Test
	public void tablet() {
		assertFalse(SitePreference.TABLET.isNormal());
		assertFalse(SitePreference.TABLET.isMobile());
		assertTrue(SitePreference.TABLET.isTablet());
	}

}
