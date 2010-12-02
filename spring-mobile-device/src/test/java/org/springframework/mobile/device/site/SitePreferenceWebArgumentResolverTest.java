package org.springframework.mobile.device.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class SitePreferenceWebArgumentResolverTest {
	
	private SitePreferenceWebArgumentResolver resolver = new SitePreferenceWebArgumentResolver();

	private ServletWebRequest request = new ServletWebRequest(new MockHttpServletRequest());

	@Test
	public void resolve() throws Exception {
		request.setAttribute(SitePreferenceResolver.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethod", SitePreference.class), 0);
		Object resolved = resolver.resolveArgument(parameter, request);
		assertEquals(SitePreference.MOBILE, resolved);
	}

	@Test
	public void unresolved() throws Exception {
		request.setAttribute(SitePreferenceResolver.CURRENT_SITE_PREFERENCE_ATTRIBUTE, SitePreference.MOBILE, WebRequest.SCOPE_REQUEST);
		MethodParameter parameter = new MethodParameter(getClass().getMethod("handlerMethodUnresolved", String.class), 0);
		Object resolved = resolver.resolveArgument(parameter, request);
		assertSame(WebArgumentResolver.UNRESOLVED, resolved);
	}

	@RequestMapping
	public void handlerMethod(SitePreference sitePreference) {
		
	}

	@RequestMapping
	public void handlerMethodUnresolved(String foo) {
		
	}

}
