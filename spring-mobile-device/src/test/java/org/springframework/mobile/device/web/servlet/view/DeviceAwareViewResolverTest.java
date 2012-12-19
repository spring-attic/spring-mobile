/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.mobile.device.web.servlet.view;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.StubDevice;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.web.servlet.view.DeviceAwareViewResolver;

/**
 * Device aware view resolver tests.
 * 
 * @author Scott Rossillo
 *
 */
public final class DeviceAwareViewResolverTest {
	
	private final DeviceAwareViewResolver viewResolver = new DeviceAwareViewResolver();
	
	private final String viewName = "home";
	
	@Before
	public void setUp() {
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setNormalPrefix("/normal/");
		viewResolver.setMobilePrefix("/mobile/");
	}

	@Test
	public void testMobileViewResolution() throws Exception {
		
		final Device device = new StubDevice(DeviceType.MOBILE);
		final String resolvedViewName;
		
		resolvedViewName = viewResolver.resolveDeviceAwareViewName(viewName, device, null);
		
		Assert.assertEquals("mobile/home", resolvedViewName);
	}
	
	@Test
	public void testMobileViewResolutionWithMobileSitePreference() throws Exception {
		
		final Device device = new StubDevice(DeviceType.MOBILE);
		final SitePreference sitePreference = SitePreference.MOBILE;
		final String resolvedViewName;
		
		resolvedViewName = viewResolver.resolveDeviceAwareViewName(viewName, device, sitePreference);
		
		Assert.assertEquals("mobile/home", resolvedViewName);
	}
	
	@Test
	public void testMobileViewResolutionWithNormalSitePreference() throws Exception {
		
		final Device device = new StubDevice(DeviceType.MOBILE);
		final SitePreference sitePreference = SitePreference.NORMAL;
		final String resolvedViewName;
		
		resolvedViewName = viewResolver.resolveDeviceAwareViewName(viewName, device, sitePreference);
		
		Assert.assertEquals("normal/home", resolvedViewName);
	}
	
	@Test
	public void testNormalViewResolution() throws Exception {
		
		final Device device = new StubDevice(DeviceType.NORMAL);
		final String resolvedViewName;
		
		resolvedViewName = viewResolver.resolveDeviceAwareViewName(viewName, device, null);
		
		Assert.assertEquals("normal/home", resolvedViewName);
	}
}
