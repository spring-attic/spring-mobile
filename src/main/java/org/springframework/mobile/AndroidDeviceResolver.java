package org.springframework.mobile;

import java.util.Arrays;
import java.util.Collection;

public class AndroidDeviceResolver implements DeviceResolver {

	private Collection<String> androidKeywords = Arrays.asList(new String[] { "Android" });

	public Device resolveDeviceForUserAgent(String userAgent) {
		if (isAndroid(userAgent)) {
			GenericDevice device = new GenericDevice(userAgent);
			device.setMobileBrowser(true);
			device.setApple(false);
			return device;
		} else {
			return null;
		}
	}
	
	public boolean isAndroid(String userAgent) {
		for (String keyword : androidKeywords) {
			if (userAgent.contains(keyword)) {
				return true;
			}
		}
		return false;
	}

}
