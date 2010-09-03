package org.springframework.mobile.wurfl;

import net.sourceforge.wurfl.core.WURFLManager;

import org.springframework.mobile.Device;
import org.springframework.mobile.DeviceResolver;

public class WURFLDeviceResolver implements DeviceResolver {

	private WURFLManager wurflManager;

	public WURFLDeviceResolver(WURFLManager wurflManager) {
		this.wurflManager = wurflManager;
	}

	public Device resolveDeviceForUserAgent(String userAgent) {
		return new WURFLDevice(wurflManager.getDeviceForRequest(userAgent));
	}

}
