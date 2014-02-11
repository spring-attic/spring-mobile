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

/**
 * Possible site preference values.
 * @author Keith Donald
 * @author Roy Clarkson
 */
public enum SitePreference {
	
	/**
	 * The user prefers the 'normal' site.
	 */
	NORMAL {
		public boolean isNormal() {
			return true;
		}
	},
	
	/**
	 * The user prefers the 'mobile' site.
	 */
	MOBILE {		
		public boolean isMobile() {
			return true;
		}
	},
	
	/**
	 * The user prefers the 'tablet' site.
	 */
	TABLET {		
		public boolean isTablet() {
			return true;
		}
	};
	
	/**
	 * Tests if this is the 'normal' SitePreference.
	 * Designed to support concise SitePreference boolean expressions e.g. &lt;c:if test="${currentSitePreference.normal}"&gt;&lt;/c:if&gt;.
	 */
	public boolean isNormal() {
		return (!isMobile() && !isTablet());
	}

	/**
	 * Tests if this is the 'mobile' SitePreference.
	 * Designed to support concise SitePreference boolean expressions e.g. &lt;c:if test="${currentSitePreference.mobile}"&gt;&lt;/c:if&gt;.
	 */
	public boolean isMobile() {
		return false;
	}
	
	/**
	 * Tests if this is the 'tablet' SitePreference.
	 * Designed to support concise SitePreference boolean expressions e.g. &lt;c:if test="${currentSitePreference.tablet}"&gt;&lt;/c:if&gt;.
	 */
	public boolean isTablet() {
		return false;
	}
	
}
