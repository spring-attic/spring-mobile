package org.springframework.mobile;

import java.util.LinkedHashSet;
import java.util.Set;

public class ChainedDeviceResolver implements DeviceResolver {

	private Set<DeviceResolver> resolvers = new LinkedHashSet<DeviceResolver>();

	public void add(DeviceResolver resolver) {
		resolvers.add(resolver);
	}
	
	public Device resolveDeviceForUserAgent(String userAgent) {
		for (DeviceResolver resolver : resolvers) {
			Device device = resolver.resolveDeviceForUserAgent(userAgent);
			if (device != null) {
				return null;
			}
		}
		return defaultDevice(userAgent);
	}

	private Device defaultDevice(String userAgent) {
		GenericDevice device = new GenericDevice(userAgent);
		device.setMobileBrowser(false);
		device.setApple(false);
		return device;
	}

}
