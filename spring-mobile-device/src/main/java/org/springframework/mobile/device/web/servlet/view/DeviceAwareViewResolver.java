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

import java.util.Locale;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * Provides a device aware extension of the 
 * {@link org.springframework.web.servlet.view.UrlBasedViewResolver} class, 
 * allowing for resolution of device specific view names without the need for
 * a dedicated mapping to be defined for each view.
 * 
 * <p>
 * Device aware view names can be augmented by a specified normal prefix 
 * and/or a mobile prefix and take into account a base prefix and/or suffix 
 * specified via {@link #setPrefix(String)} and/or {@link #setSuffix(String)}.
 * </p>
 * 
 * <p>
 * For example, prefix="/WEB-INF/jsp/", suffix=".jsp", normalPrefix="normal" mobilePrefix="mobile" viewname="test": 
 * </p>
 * 
 * <p>
 * When device is normal -&gt; "/WEB-INF/jsp/normal/test.jsp" <br />
 * When device is mobile -&gt; "/WEB-INF/jsp/mobile/test.jsp"
 * </p>
 * 
 * <p>
 * A prefix may be omitted for normal views, which may be useful if you
 * are adding mobile views to an existing project, e.g.,
 * prefix="/WEB-INF/jsp/", suffix=".jsp", mobilePrefix="mobile" viewname="test":
 * </p>
 * 
 * <p>
 * When device is normal -&gt; "/WEB-INF/jsp/test.jsp" <br />
 * When device is mobile -&gt; "/WEB-INF/jsp/mobile/test.jsp"
 * </p>
 * 
 * 
 * <p>
 * This implementation supports all the features of
 * {@link org.springframework.web.servlet.view.UrlBasedViewResolver}, such as
 * redirect URLs and forward URLs specified via the "redirect:" and "forward:"
 * prefixes. 
 * </p>
 * 
 * <p>
 * Note: This class does not support localized resolution, i.e. resolving a
 * symbolic view name to different resources depending on the current locale.
 * </p>
 * 
 * @author Scott Rossillo
 * 
 * @see #setNormalPrefix(String)
 * @see #setMobilePrefix(String)
 *
 * @see org.springframework.web.servlet.view.UrlBasedViewResolver
 */
public class DeviceAwareViewResolver extends UrlBasedViewResolver implements ViewResolver {

	private String mobilePrefix = null;
	private String normalPrefix = null;

	/**
	 * Creates a new device aware view resolver.
	 */
	public DeviceAwareViewResolver() {
		super();
	}

	/**
	 * Returns the device aware view name for the given device prefix and view name.
	 * 
	 * @param prefix the path prefix for the device requesting the view
	 * 
	 * @param viewName the name of the view before device resolution
	 * 
	 * @return the device aware view name for the given device prefix and view name
	 */
	protected String buildDeviceViewName(final String prefix, final String viewName) {

		if (prefix == null) {
			if (normalPrefix != null) {
				return this.buildDeviceViewName(normalPrefix, viewName);
			} else {
				return viewName;
			}
		}

		return (prefix + "/" + viewName);
	}

	/**
	 * This implementation returns onyl the device aware view name, 
	 * as this view resolver doesn't support localized resolution.
	 * 
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#getCacheKey(java.lang.String, java.util.Locale)
	 * @see #resolveDeviceAwareViewName(String)
	 */
	@Override
	protected Object getCacheKey(String viewName, Locale locale) {
		return this.resolveDeviceAwareViewName(viewName);
	}

	/**
	 * Returns the given prefix without a trailing slash.
	 * 
	 * @param prefix the prefix to normalize
	 * 
	 * @return the given <code>prefix</code> without a trailing slash
	 */
	private String normalizePrefix(final String prefix) {

		String normalizedPrefix = prefix;
		
		if (prefix == null) {
			return null;
		}
		
		if(normalizedPrefix.startsWith("/")) {
			normalizedPrefix = prefix.substring(1);
		}

		if (normalizedPrefix.endsWith("/") ) {
			normalizedPrefix = normalizedPrefix.substring(0, normalizedPrefix.length() - 1);
		}

		return normalizedPrefix;
	}

	/**
	 * Overridden to support device aware view resolution.
	 * Delegates to {@link #createDeviceAwareView(String, Locale)} 
	 * unless the view name contains special redirect URLs or forward URLs. 
	 * If a redirect URL or forward URL is detected, 
	 * {@link org.springframework.web.servlet.view.UrlBasedViewResolver#createView(java.lang.String, java.util.Locale)}
	 * is called instead.
	 * 
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#createView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View createView(final String viewName, final Locale locale) throws Exception {

		// If this resolver is not supposed to handle the given view,
		// return null to pass on to the next resolver in the chain.
		if (!canHandle(viewName, locale)) {
			return null;
		}

		// Check for special "redirect:" or "forward:" prefixes and delegate
		// handling to the superclass.
		if (viewName.startsWith(REDIRECT_URL_PREFIX) || viewName.startsWith(FORWARD_URL_PREFIX)) {
			return super.createView(viewName, locale);
		}

		return this.createDeviceAwareView(viewName, locale);
	}


	/**
	 * Creates the device aware view object.
	 * 
	 * <p>
	 * Resolves the device aware view name and then delegates to 
	 * {@link #loadView(String, Locale)} to actually load the view object.
	 * </p>
	 * 
	 * @param viewName the name of the view to retrieve
	 * @param locale the <code>Locale</code> to retrieve the view for
	 * 
	 * @return the <code>View</code> instance, or <code>null</code> if not found
	 * 
	 * @throws Exception if the view couldn't be resolved
	 */
	protected View createDeviceAwareView(final String viewName, final Locale locale) throws Exception {
		return super.loadView(resolveDeviceAwareViewName(viewName), locale);
	}

	/**
	 * Returns the device aware view name for the given view name.
	 * Resolves the device and site preference from the current request
	 * context and then delegates to 
	 * {@link #resolveDeviceAwareViewName(String, Device, SitePreference)}.
	 * 
	 * @param viewName the view name to resolve
	 * 
	 * @return the device aware view name for the given <code>viewName</code>
	 */
	protected String resolveDeviceAwareViewName(final String viewName) {

		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		final Device device = DeviceUtils.getCurrentDevice(requestAttributes);
		final SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(requestAttributes);

		return resolveDeviceAwareViewName(viewName, device, sitePreference);
	}

	/**
	 * Returns the device aware view name for the given view name, given device
	 * and given site preference.
	 * 
	 * @param viewName the view name to resolve (required)
	 * 
	 * @param device the <code>Device</code> for the current request (required)
	 * 
	 * @param sitePreference the <code>SitePreference</code> for the 
	 * current request or null if no site preference was specified
	 * 
	 * @return the device aware view name for the given <code>viewName</code>, 
	 * <code>device</code> and <code>sitePreference</code>
	 * 
	 */
	protected String resolveDeviceAwareViewName(
			final String viewName,
			final Device device,
			final SitePreference sitePreference) {

		String deviceAwareViewName;
		
		if (device.isMobile() && (sitePreference == null || sitePreference.isMobile())) {
			deviceAwareViewName = this.buildDeviceViewName(mobilePrefix, viewName);
		} else {
			deviceAwareViewName = this.buildDeviceViewName(normalPrefix, viewName);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format(
					"Resolved view with name [%s] for device [%s] preference [%s]%n", 
					deviceAwareViewName, device, sitePreference));
		}

		return deviceAwareViewName;
	}

	/**
	 * Sets the mobile prefix for this view resolver.
	 */
	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = normalizePrefix(mobilePrefix);
	}

	/**
	 * Sets the normal prefix for this view resolver.
	 */
	public void setNormalPrefix(String normalPrefix) {
		this.normalPrefix = normalizePrefix(normalPrefix);
	}

}
