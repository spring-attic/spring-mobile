/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
