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

package org.springframework.mobile.device.switcher;

/**
 * Abstract {@link SiteUrlFactory} implementation that differentiates each site by the 
 * HTTP request path.  Provides functionality common to all path based site URL factories. 
 * @author Scott Rossillo
 * @author Roy Clarkson
 */
public abstract class AbstractSitePathUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {

	private final String mobilePath;

	private final String tabletPath;

	private final String rootPath;

	/**
	 * Creates a new abstract site path URL factory for the given mobile path
	 * and root application path.
	 */
	public AbstractSitePathUrlFactory(final String mobilePath, final String tabletPath, final String rootPath) {
		this.mobilePath = formatPath(mobilePath);
		this.tabletPath = formatPath(tabletPath);
		this.rootPath = formatPath(rootPath);
	}

	/**
	 * The mobile path with a trailing slash.
	 * <p>Examples:</p>
	 * <pre>
	 *  "/m/"
	 *  "/mobile/"
	 * </pre>
	 * @return The mobile path. May be null.
	 */
	public String getMobilePath() {
		return this.mobilePath;
	}

	/**
	 * The tablet path with a trailing slash.
	 * <p>Examples:</p>
	 * <pre>
	 *  "/t/"
	 *  "/tablet/"
	 * </pre>
	 * @return The mobile path. May be null.
	 */
	public String getTabletPath() {
		return this.tabletPath;
	}

	/**
	 * The root path of the application with a trailing slash.
	 * <p>Examples:</p>
	 * <pre>
	 *  "/showcase/"
	 *  "/demo/"
	 * </pre>
	 * @return The root path of the application. May be null.
	 */
	public String getRootPath() {
		return this.rootPath;
	}

	/**
	 * Whether there is a mobile path or not
	 * @return true if there is a mobile path
	 */
	public boolean hasMobilePath() {
		return this.mobilePath != null;
	}

	/**
	 * Whether there is a tablet path or not
	 * @return true if there is a tablet path
	 */
	public boolean hasTabletPath() {
		return this.tabletPath != null;
	}

	/**
	 * Whether there is a root path or not
	 * @return true if there is a root path
	 */
	public boolean hasRootPath() {
		return this.rootPath != null;
	}

	/**
	 * The full path of the normal site
	 * <p>Examples:</p>
	 * <pre>
	 *  "/"
	 *  "/showcase/"
	 *  "/demo/"
	 * </pre> 
	 * @see #getRootPath()
	 * @return the full path of the normal site
	 */
	public String getFullNormalPath() {
		return (this.rootPath == null ? "/" : getCleanPath(this.rootPath) + "/");
	}

	/**
	 * The full path of the mobile site. 
	 * <p>Examples:</p>
	 * <pre>
	 *  "/mob/"
	 *  "/showcase/m/"
	 *  "/demo/mobile/"
	 * </pre>
	 * @see #getCleanMobilePath()
	 * @return the full path of the mobile site
	 */
	public String getFullMobilePath() {
		String path = null;
		if (this.mobilePath != null) {
			path = (this.rootPath == null ? this.mobilePath : getCleanPath(this.rootPath) + this.mobilePath);
		}
		return path;
	}

	/**
	 * The full path of the tablet site. 
	 * <p>Examples:</p>
	 * <pre>
	 *  "/tab/"
	 *  "/showcase/t/"
	 *  "/demo/tablet/"
	 * </pre>
	 * @see #getCleanTabletPath()
	 * @return the full path of the tablet site 
	 */
	public String getFullTabletPath() {
		String path = null;
		if (this.tabletPath != null) {
			path = (this.rootPath == null ? this.tabletPath : getCleanPath(this.rootPath) + this.tabletPath);
		}
		return path;
	}

	/**
	 * Returns the full normal path without a trailing slash.
	 * @see #getFullNormalPath()
	 */
	protected String getCleanNormalPath() {
		return getCleanPath(getFullNormalPath());
	}

	/**
	 * Returns the full mobile path without a trailing slash.
	 * @see #getFullMobilePath()
	 */
	protected String getCleanMobilePath() {
		return getCleanPath(getFullMobilePath());
	}

	/**
	 * Returns the full tablet path without a trailing slash.
	 * @see #getFullTabletPath()
	 */
	protected String getCleanTabletPath() {
		return getCleanPath(getFullTabletPath());
	}

	/**
	 * Returns the root path without a trailing slash.
	 * @see #getRootPath()
	 */
	protected String getCleanRootPath() {
		return getCleanPath(getRootPath());
	}

	// helpers

	private String formatPath(String path) {
		String formattedPath = null;
		if (path != null) {
			formattedPath = (path.startsWith("/") ? path : "/" + path);
			formattedPath = (formattedPath.endsWith("/") ? formattedPath : formattedPath + "/");
		}
		return formattedPath;
	}

	private String getCleanPath(String path) {
		String cleanPath = null;
		if (path != null) {
			cleanPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
		}
		return cleanPath;
	}

}
