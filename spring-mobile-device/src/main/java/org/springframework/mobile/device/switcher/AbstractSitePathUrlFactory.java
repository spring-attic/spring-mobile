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
package org.springframework.mobile.device.switcher;

import org.springframework.util.Assert;

/**
 * Abstract {@link SiteUrlFactory} implementation that differentiates each site by the 
 * HTTP request path.  Provides functionality common to all path based site URL factories. 
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public abstract class AbstractSitePathUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {

	private final String mobilePath;

	private final String rootPath;

	/**
	 * Creates a new abstract site path URL factory for the given mobile path.
	 */
	public AbstractSitePathUrlFactory(final String mobilePath) {
		this(mobilePath, null);
	}

	/**
	 * Creates a new abstract site path URL factory for the given mobile path
	 * and root application path.
	 */
	public AbstractSitePathUrlFactory(final String mobilePath, final String rootPath) {
		Assert.notNull(mobilePath, "mobilePath is required");
		this.mobilePath = formatPath(mobilePath);
		this.rootPath = formatPath(rootPath);
	}

	/**
	 * The mobile path with a trailing slash.
	 * <p>Examples:</p>
	 * <pre>
	 *  "/m/"
	 *  "/mobile/"
	 * </pre>
	 * @return the mobile path
	 */
	public String getMobilePath() {
		return this.mobilePath;
	}

	/**
	 * The root path of the application with a trailing slash.
	 * <p>Examples:</p>
	 * <pre>
	 *  "/showcase/"
	 *  "/demo/"
	 * </pre>
	 * @return the root path of the application
	 */
	public String getRootPath() {
		return this.rootPath;
	}

	/**
	 * The full path of the mobile site. 
	 * <p>Examples:</p>
	 * <pre>
	 *  "/showcase/m/"
	 *  "/demo/mobile/"
	 * </pre>
	 * @return the full path of the mobile site
	 */
	public String getFullMobilePath() {
		return (this.rootPath == null ? this.mobilePath : getCleanPath(getRootPath()) + this.mobilePath);
	}

	/**
	 * Returns the full mobile path without a trailing slash.
	 */
	protected String getCleanMobilePath() {
		return getCleanPath(getFullMobilePath());
	}

	// helpers

	private String formatPath(String path) {
		String formattedPath = null;
		if (path != null) {
			formattedPath = (path.startsWith("/") ? path : "/" + path);
			formattedPath = (path.endsWith("/") ? path : path + "/");
		}
		return formattedPath;
	}

	private String getCleanPath(String path) {
		return path.substring(0, path.length() - 1);
	}

}
