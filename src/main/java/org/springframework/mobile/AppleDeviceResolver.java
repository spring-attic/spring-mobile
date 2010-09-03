package org.springframework.mobile;

import java.util.Arrays;
import java.util.Collection;

public class AppleDeviceResolver implements DeviceResolver {

	private Collection<String> appleKeywords = Arrays.asList(new String[] { "iPhone", "iPod", "iPad" });

	public Device resolveDeviceForUserAgent(String userAgent) {
		if (isApple(userAgent)) {
			GenericDevice device = new GenericDevice(userAgent);
			device.setMobileBrowser(true);
			device.setApple(true);
			return device;
		} else {
			return null;
		}
	}
	
	public boolean isApple(String userAgent) {
		for (String keyword : appleKeywords) {
			if (userAgent.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

}
