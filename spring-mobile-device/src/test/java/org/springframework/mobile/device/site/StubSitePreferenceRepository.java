package org.springframework.mobile.device.site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceRepository;

public class StubSitePreferenceRepository implements SitePreferenceRepository {

	private SitePreference sitePreference;

	public void saveSitePreference(SitePreference preference, HttpServletRequest request, HttpServletResponse response) {
		sitePreference = preference;
	}

	public SitePreference loadSitePreference(HttpServletRequest request) {
		return sitePreference;
	}
	
	public SitePreference getSitePreference() {
		return sitePreference;
	}
	
	public void setSitePreference(SitePreference sitePreference) {
		this.sitePreference = sitePreference;
	}
}