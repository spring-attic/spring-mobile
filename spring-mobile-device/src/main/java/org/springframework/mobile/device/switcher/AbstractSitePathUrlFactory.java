package org.springframework.mobile.device.switcher;


/**
 * Abstract {@link SiteUrlFactory} implementation that differentiates each site by the 
 * HTTP request path.  Provides functionality common to all path based site URL factories. 
 * 
 * @author Scott Rossillo
 *
 */
public abstract class AbstractSitePathUrlFactory extends AbstractSiteUrlFactory implements SiteUrlFactory {
	
	private final String mobilePath;
	
	/**
	 * Creates a new abstract site path URL factory for the given mobile path.
	 */
	public AbstractSitePathUrlFactory(final String mobilePath) {
		this.mobilePath = (mobilePath.endsWith("/") ? mobilePath : mobilePath + "/");
	}
	
	/**
	 * Returns the mobile path with a trailing slash character.
	 */
	public String getMobilePath() {
		return mobilePath;
	}
	
	/**
	 * Returns the mobile path without a trailing slash.
	 */
	protected String getCleanMobilePath() {
		return mobilePath.substring(0, mobilePath.length() - 1);
	}
}
