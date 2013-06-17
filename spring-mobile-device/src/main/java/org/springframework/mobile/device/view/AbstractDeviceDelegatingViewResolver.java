/*
 * Copyright 2010-2013 the original author or authors.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * Abstract {@link ViewResolver} implementation, providing a device
 * aware {@link ViewResolver} wrapper that delegates to another view resolver
 * implementation, allowing for resolution of device specific view names without
 * the need for a dedicated mapping to be defined for each view.</p>
 * @author Roy Clarkson
 * @since 1.1
 * @see LiteDeviceDelegatingViewResolver
 */
public abstract class AbstractDeviceDelegatingViewResolver extends WebApplicationObjectSupport implements ViewResolver {

	/**
	 * Prefix for special view names that specify a redirect URL (usually
	 * to a controller after a form has been submitted and processed).
	 */
	public static final String REDIRECT_URL_PREFIX = "redirect:";

	/**
	 * Prefix for special view names that specify a forward URL (usually
	 * to a controller after a form has been submitted and processed).
	 */
	public static final String FORWARD_URL_PREFIX = "forward:";

	private final ViewResolver delegate;

	private boolean enableFallback = false;
	private boolean forwardToViews = true;
	private boolean redirectToViews = true;

	/**
	 * Creates a new AbstractDeviceDelegatingViewResolver
	 * @param delegate the ViewResolver in which to delegate
	 */
	protected AbstractDeviceDelegatingViewResolver(ViewResolver delegate) {
		Assert.notNull(delegate, "delegate is required");
		this.delegate = delegate;
	}

	/**
	 * Returns the delegate view resolver
	 */
	public ViewResolver getViewResolver() {
		return this.delegate;
	}

	/**
	 * Enables support for fallback resolution, meaning if the adjusted view
	 * name cannot be resolved, and attempt will be made to resolve the
	 * original view name. This may be helpful in situations where not all
	 * views within a web site have device specific implementations.
	 */
	public void setEnableFallback(boolean enableFallback) {
		this.enableFallback = enableFallback;
	}

	/**
	 * Return whether fallback view resolution is enabled
	 * @see #setEnableFallback(boolean)
	 */
	protected boolean getEnableFallback() {
		return this.enableFallback;
	}

	/**
	 * Return whether forwarding to device specific views is enabled.
	 * @see #setForwardToViews(boolean)
	 */
	public boolean isForwardToViews() {
		return forwardToViews;
	}

	/**
	 * Sets whether this view resolver forwards requests to device specific views when
	 * {@link #FORWARD_URL_PREFIX} prefixes the view name.
	 */
	public void setForwardToViews(boolean forwardToViews) {
		this.forwardToViews = forwardToViews;
	}

	/**
	 * Returns whether redirecting to device specific views is enabled.
	 * @see #setRedirectToViews(boolean)
	 */
	public boolean isRedirectToViews() {
		return redirectToViews;
	}

	/**
	 * Sets whether this view resolver redirects requests to device specific views when
	 * {@link #REDIRECT_URL_PREFIX} prefixes the view name.
	 */
	public void setRedirectToViews(boolean redirectToViews) {
		this.redirectToViews = redirectToViews;
	}

	public View resolveViewName(String viewName, Locale locale) throws Exception {
		String deviceViewName = getDeviceViewName(viewName);
		View view = delegate.resolveViewName(deviceViewName, locale);
		if (enableFallback && view == null) {
			view = delegate.resolveViewName(viewName, locale);
		}
		return view;
	}

	/**
	 * Returns the adjusted view name as determined by subclass implementation.
	 * In the case where a requested URL is prefixed with "redirect:" or
	 * "forward:", the view name will be adjusted to maintain those prefixes.
	 * A redirect or forward to an absolute URL, e.g. "http://springsource.org"
	 * will not be adjusted. Additionally, redirecting and forwarding to device
	 * specific views may be disabled.
	 * @param viewName the name of the view before device resolution
	 * @return the adjusted view name
	 * @see #getDeviceViewNameInternal(String)
	 * @see #setRedirectToViews(boolean)
	 * @see #setForwardToViews(boolean)
	 */
	protected String getDeviceViewName(String viewName) {
		// Check for special "redirect:" prefix.
		if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
			String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
			// do not adjust absolute URL's
			if (isAbsoluteUrl(redirectUrl) || !redirectToViews) {
				return viewName;
			}
			return REDIRECT_URL_PREFIX + getDeviceViewNameInternal(redirectUrl);
		}
		// Check for special "forward:" prefix.
		if (viewName.startsWith(FORWARD_URL_PREFIX)) {
			String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
			// do not adjust absolute URL's
			if (isAbsoluteUrl(forwardUrl) || !forwardToViews) {
				return viewName;
			}
			return FORWARD_URL_PREFIX + getDeviceViewNameInternal(forwardUrl);
		}
		return getDeviceViewNameInternal(viewName);
	}
	
	private boolean isAbsoluteUrl(String url) {
		try {
			URI uri = new URI(url);
			return uri.isAbsolute();
		} catch (URISyntaxException e) {
			return false;
		}
	}

	/**
	 * Subclasses must implement this method, adjusting the device view name
	 * based on device resolution used within the subclass.
	 * @param viewName the name of the view before device resolution
	 * @return the adjusted view name
	 * @see #getDeviceViewName(String)
	 */
	protected abstract String getDeviceViewNameInternal(String viewName);

	@Override
	protected void initServletContext(ServletContext servletContext) {
		String name = delegate.getClass().getName();
		getApplicationContext().getAutowireCapableBeanFactory().initializeBean(delegate, name);
	}

}
