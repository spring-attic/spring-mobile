package org.springframework.mobile.device.site;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SitePreferenceTest {

	@Test
	public void normal() {
		assertFalse(SitePreference.NORMAL.isMobile());		
	}

	@Test
	public void mobile() {
		assertTrue(SitePreference.MOBILE.isMobile());
	}

}
