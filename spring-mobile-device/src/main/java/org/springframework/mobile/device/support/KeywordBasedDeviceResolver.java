package org.springframework.mobile.device.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.resolver.DeviceRequest;
import org.springframework.mobile.device.resolver.DeviceResolver;

/**
 * Simple device resolver implementation that resolves a mobile device by looking for keywords contained within the user agent string.
 * @author Keith Donald
 */
public abstract class KeywordBasedDeviceResolver implements DeviceResolver {

	private final List<String> keywords = new ArrayList<String>();
	
	/**
	 * Adds a single keyword to look for.
	 */
	public void addKeyword(String keyword) {
		this.keywords.add(keyword);
	}

	/**
	 * Adds a variable list of keywords to look for.
	 */
	public void addKeywords(String... keywords) {
		for (String keyword : keywords) {
			this.keywords.add(keyword);			
		}
	}
	
	public Device resolveDevice(DeviceRequest request) {
		if (containsKeyword(request.getUserAgent())) {
			return createMobileDevice(request);
		} else {
			return null;
		}
	}
	
	/**
	 * Creates the model for the matched device.
	 * This implementation simply returns a GenericDevice implementation with the mobile property set to true.
	 * Subclasses may override to return a custom implementation.
	 * @param request the matched device request
	 * @return the device model
	 */
	protected Device createMobileDevice(DeviceRequest request) {
		return new GenericDevice(request.getUserAgent(), true);
	}

	// internal helpers
	
	private boolean containsKeyword(String userAgent) {
		for (String keyword : keywords) {
			if (userAgent.contains(keyword)) {
				return true;
			}
		}
		return false;
	}
}
