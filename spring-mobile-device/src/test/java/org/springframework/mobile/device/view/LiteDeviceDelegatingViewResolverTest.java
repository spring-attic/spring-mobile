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

package org.springframework.mobile.device.view;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.Ordered;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.StubDevice;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceHandler;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * @author Roy Clarkson
 */
public final class LiteDeviceDelegatingViewResolverTest {

	private LiteDeviceDelegatingViewResolver viewResolver;

	private ViewResolver delegateViewResolver;

	private View view;

	private MockHttpServletRequest request = new MockHttpServletRequest();

	private StubDevice device = new StubDevice();

	private String viewName = "home";

	private final Locale locale = Locale.ENGLISH;

	@Before
	public void setUp() {
		StaticWebApplicationContext context = new StaticWebApplicationContext();
		context.setServletContext(new MockServletContext());
		context.refresh();
		this.delegateViewResolver = createMock(ViewResolver.class);
		this.viewResolver = new LiteDeviceDelegatingViewResolver(delegateViewResolver);
		viewResolver.setApplicationContext(context);
		request.setRequestURI("/home");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		this.view = createMock("view", View.class);
	}

	@After
	public void tearDown() throws Exception {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullDelegate() throws Exception {
		this.viewResolver = new LiteDeviceDelegatingViewResolver(null);
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceRedirect() throws Exception {
		this.viewName = "redirect:about";
		replayMocks("redirect:about");
	}
	
	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceRedirectEmpty() throws Exception {
		this.viewName = "redirect:";
		replayMocks("redirect:");
	}
	
	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceRedirectAbsoluteUrl() throws Exception {
		this.viewName = "redirect:http://spring.io";
		replayMocks("redirect:http://spring.io");
	}
	
	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceRedirectAbsoluteUrlSSL() throws Exception {
		this.viewName = "redirect:https://spring.io";
		replayMocks("redirect:https://spring.io");
	}
	
	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceRedirectAbsoluteUrlFTP() throws Exception {
		this.viewName = "redirect:ftp://spring.io";
		replayMocks("redirect:ftp://spring.io");
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceForward() throws Exception {
		this.viewName = "forward:about";
		replayMocks("forward:about");
	}
	
	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceForwardAbsoluteUrl() throws Exception {
		this.viewName = "forward:http://spring.io";
		replayMocks("forward:http://spring.io");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixRedirectToRoot() throws Exception {
		this.viewName = "redirect:/";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("redirect:/");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixRedirectToEmpty() throws Exception {
		this.viewName = "redirect:";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("redirect:");
	}
	
	@Test
	public void resolveViewNameNormalDeviceNormalPrefixRedirectToAbsoluteUrl() throws Exception {
		this.viewName = "redirect:http://spring.io";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("redirect:http://spring.io");
	}
	
	@Test
	public void resolveViewNameNormalDeviceNormalPrefixRedirectToAbsoluteUrlFTP() throws Exception {
		this.viewName = "redirect:ftp://spring.io";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("redirect:ftp://spring.io");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalSuffixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalSuffix(".nor");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixForward() throws Exception {
		this.viewName = "forward:about";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("forward:about");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixForwardToRoot() throws Exception {
		this.viewName = "forward:/";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("forward:/");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalPrefixForwardToEmpty() throws Exception {
		this.viewName = "forward:";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("forward:");
	}
	
	@Test
	public void resolveViewNameNormalDeviceNormalPrefixForwardToAbsoluteUrl() throws Exception {
		this.viewName = "forward:http://spring.io";
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("forward:http://spring.io");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixRedirectToRoot() throws Exception {
		this.viewName = "redirect:/";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("redirect:/");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixRedirectToEmpty() throws Exception {
		this.viewName = "redirect:";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("redirect:");
	}
	
	@Test
	public void resolveViewNameMobileDeviceNormalPrefixRedirectToAbsoluteUrl() throws Exception {
		this.viewName = "redirect:http://spring.io";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("redirect:http://spring.io");
	}
	
	@Test
	public void resolveViewNameMobileDeviceNormalPrefixRedirectToAbsoluteUrlSSL() throws Exception {
		this.viewName = "redirect:https://spring.io";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("redirect:https://spring.io");
	}

	@Test
	public void resolveViewNameMobileDeviceMobileSuffixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobileSuffix(".mob");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixForward() throws Exception {
		this.viewName = "forward:about";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("forward:about");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixForwardToRoot() throws Exception {
		this.viewName = "forward:/";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("forward:/");
	}

	@Test
	public void resolveViewNameMobileDeviceMobilePrefixForwardToEmpty() throws Exception {
		this.viewName = "forward:";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("forward:");
	}
	
	@Test
	public void resolveViewNameMobileDeviceNormalPrefixForwardToAbsoluteUrl() throws Exception {
		this.viewName = "forward:HTTP://spring.io";
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("forward:HTTP://spring.io");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixRedirectToRoot() throws Exception {
		this.viewName = "redirect:/";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("redirect:/");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixRedirectToEmpty() throws Exception {
		this.viewName = "redirect:";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("redirect:");
	}
	
	@Test
	public void resolveViewNameTabletDeviceNormalPrefixRedirectToAbsoluteUrl() throws Exception {
		this.viewName = "redirect:http://spring.io";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("redirect:http://spring.io");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletSuffixRedirect() throws Exception {
		this.viewName = "redirect:about";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletSuffix(".tab");
		replayMocks("redirect:about");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixForward() throws Exception {
		this.viewName = "forward:about";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("forward:about");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixForwardToRoot() throws Exception {
		this.viewName = "forward:/";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("forward:/");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletPrefixForwardToEmpty() throws Exception {
		this.viewName = "forward:";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("forward:");
	}
	
	@Test
	public void resolveViewNameTabletDeviceNormalPrefixForwardToAbsoluteUrl() throws Exception {
		this.viewName = "forward:http://spring.io";
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("forward:http://spring.io");
	}

	@Test
	public void resolveViewNameNormalDevicePrefixFallback() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName("normal/" + viewName, locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameNormalDeviceSuffixFallback() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalSuffix(".nor");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName(viewName + ".nor", locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameMobileDevicePrefixFallback() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName("mobile/" + viewName, locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameMobileDeviceSuffixFallback() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobileSuffix(".mob");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName(viewName + ".mob", locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameTabletDevicePrefixFallback() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName("tablet/" + viewName, locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameTabletDeviceSuffixFallback() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletSuffix(".tab");
		viewResolver.setEnableFallback(true);
		expect(delegateViewResolver.resolveViewName(viewName + ".tab", locale)).andReturn(null);
		expect(delegateViewResolver.resolveViewName(viewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreference() throws Exception {
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceNormalPrefix() throws Exception {
		viewResolver.setNormalPrefix("normal/");
		replayMocks("normal/" + viewName);
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceNormalSuffix() throws Exception {
		viewResolver.setNormalSuffix(".norm");
		replayMocks(viewName + ".norm");
	}

	@Test
	public void resolveViewNameNoDeviceNoSitePreferenceNormalPrefixAndSuffix() throws Exception {
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setNormalSuffix(".nor");
		replayMocks("normal/" + viewName + ".nor");
	}

	@Test
	public void resolveViewNameNormalDeviceNoSitePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceNoSitePreferenceNormalPrefix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("normal/" + viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceNoSitePreferenceNormalSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalSuffix(".norm");
		replayMocks(viewName + ".norm");
	}

	@Test
	public void resolveViewNameNormalDeviceNoSitePreferenceNormalPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setNormalSuffix(".nor");
		replayMocks("normal/" + viewName + ".nor");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalSitePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceNormalSitePreferenceNormalPrefix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("normal/" + viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceNormalSitePreferenceNormalSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalSuffix(".nor");
		replayMocks(viewName + ".nor");
	}

	@Test
	public void resolveViewNameNormalDeviceNormalSitePreferenceNormalPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setNormalSuffix(".nor");
		replayMocks("normal/" + viewName + ".nor");
	}

	@Test
	public void resolveViewNameNormalDeviceMobileSitePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceMobileSitePreferenceMobilePrefix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("mobile/" + viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceMobileSitePreferenceMobileSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobileSuffix(".mob");
		replayMocks(viewName + ".mob");
	}

	@Test
	public void resolveViewNameNormalDeviceMobileSitePreferenceMobilePrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setMobileSuffix(".mob");
		replayMocks("mobile/" + viewName + ".mob");
	}

	@Test
	public void resolveViewNameNormalDeviceTabletSitePreference() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceTabletSitePreferenceTabletPrefix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("tablet/" + viewName);
	}

	@Test
	public void resolveViewNameNormalDeviceTabletSitePreferenceTabletSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletSuffix(".tab");
		replayMocks(viewName + ".tab");
	}

	@Test
	public void resolveViewNameNormalDeviceTabletSitePreferenceTabletPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.NORMAL);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setTabletSuffix(".tab");
		replayMocks("tablet/" + viewName + ".tab");
	}

	@Test
	public void resolveViewNameMobileDeviceNoSitePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceNoSitePreferenceMobilePrefix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("mobile/" + viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceNoSitePreferenceMobileSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobileSuffix(".mob");
		replayMocks(viewName + ".mob");
	}

	@Test
	public void resolveViewNameMobileDeviceNoSitePreferenceMobilePrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setMobileSuffix(".mob");
		replayMocks("mobile/" + viewName + ".mob");
	}

	@Test
	public void resolveViewNameMobileDeviceNormalSitePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceNormalSitePreferenceNormalPrefix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("normal/" + viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceNormalSitePreferenceNormalSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalSuffix(".nor");
		replayMocks(viewName + ".nor");
	}

	@Test
	public void resolveViewNameMobileDeviceNormalSitePreferenceNormalPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setNormalSuffix(".nor");
		replayMocks("normal/" + viewName + ".nor");
	}

	@Test
	public void resolveViewNameMobileDeviceMobileSitePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceMobileSitePreferenceMobilePrefix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("mobile/" + viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceMobileSitePreferenceMobileSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobileSuffix(".mob");
		replayMocks(viewName + ".mob");
	}

	@Test
	public void resolveViewNameMobileDeviceMobileSitePreferenceMobilePrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setMobileSuffix(".mob");
		replayMocks("mobile/" + viewName + ".mob");
	}

	@Test
	public void resolveViewNameMobileDeviceTabletSitePreference() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
	}

	@Test
	public void resolveViewNameMobileDeviceTabletSitePreferenceTabletPrefix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("tablet/" + viewName);
	}

	@Test
	public void resolveViewNameMobileDeviceTabletSitePreferenceTabletSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletSuffix(".tab");
		replayMocks(viewName + ".tab");
	}

	@Test
	public void resolveViewNameMobileDeviceTabletSitePreferenceTabletPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.MOBILE);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setTabletSuffix(".tab");
		replayMocks("tablet/" + viewName + ".tab");
	}

	@Test
	public void resolveViewNameTabletDeviceNoSitePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceNoSitePreferenceTabletPrefix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("tablet/" + viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceNoSitePreferenceTabletSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletSuffix(".tab");
		replayMocks(viewName + ".tab");
	}

	@Test
	public void resolveViewNameTabletDeviceNoSitePreferenceTabletPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setTabletSuffix(".tab");
		replayMocks("tablet/" + viewName + ".tab");
	}

	@Test
	public void resolveViewNameTabletDeviceNormalSitePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceNormalSitePreferenceNormalPrefix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		replayMocks("normal/" + viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceNormalSitePreferenceNormalSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalSuffix(".nor");
		replayMocks(viewName + ".nor");
	}

	@Test
	public void resolveViewNameTabletDeviceNormalSitePreferenceNormalPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.NORMAL);
		viewResolver.setNormalPrefix("normal/");
		viewResolver.setNormalSuffix(".nor");
		replayMocks("normal/" + viewName + ".nor");
	}

	@Test
	public void resolveViewNameTabletDeviceMobileSitePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceMobileSitePreferenceMobilePrefix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		replayMocks("mobile/" + viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceMobileSitePreferenceMobileSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobileSuffix(".mob");
		replayMocks(viewName + ".mob");
	}

	@Test
	public void resolveViewNameTabletDeviceMobileSitePreferenceMobilePrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE);
		viewResolver.setMobilePrefix("mobile/");
		viewResolver.setMobileSuffix(".mob");
		replayMocks("mobile/" + viewName + ".mob");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletSitePreference() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		replayMocks(viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceTabletSitePreferenceTabletPrefix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		replayMocks("tablet/" + viewName);
	}

	@Test
	public void resolveViewNameTabletDeviceTabletSitePreferenceTabletSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletSuffix(".tab");
		replayMocks(viewName + ".tab");
	}

	@Test
	public void resolveViewNameTabletDeviceTabletSitePreferenceTabletPrefixAndSuffix() throws Exception {
		device.setDeviceType(DeviceType.TABLET);
		request.setAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE, device);
		request.setAttribute(SitePreferenceHandler.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.TABLET);
		viewResolver.setTabletPrefix("tablet/");
		viewResolver.setTabletSuffix(".tab");
		replayMocks("tablet/" + viewName + ".tab");
	}

	@Test
	public void resolveViewNameComplexPath() throws Exception {
		viewResolver.setNormalPrefix("/vi/e/w/s/");
		replayMocks("/vi/e/w/s/" + viewName);
	}

	@Test
	public void implementsOrdered() {
		assertTrue(viewResolver instanceof Ordered);
	}

	@Test
	public void defaultOrder() {
		assertEquals(Ordered.LOWEST_PRECEDENCE, viewResolver.getOrder());
	}
	
	@Test
	public void modifiedOrdered() {
		viewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
		assertEquals(Ordered.HIGHEST_PRECEDENCE + 10, viewResolver.getOrder());
	}

	// helpers

	private void replayMocks(String expectedViewName) throws Exception {
		expect(delegateViewResolver.resolveViewName(expectedViewName, locale)).andReturn(view);
		replay(delegateViewResolver, view);
		View result = viewResolver.resolveViewName(viewName, locale);
		assertSame("Invalid view", view, result);
		verify(delegateViewResolver, view);
	}

}
