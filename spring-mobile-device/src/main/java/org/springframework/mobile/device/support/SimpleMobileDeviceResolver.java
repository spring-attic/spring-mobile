package org.springframework.mobile.device.support;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.resolver.DeviceRequest;
import org.springframework.mobile.device.resolver.DeviceResolver;

public class SimpleMobileDeviceResolver implements DeviceResolver {

	public Device resolveDevice(DeviceRequest request) {
		String userAgent = request.getUserAgent().toLowerCase();
		if (userAgent.contains("iphone") || userAgent.contains("android") || userAgent.contains("blackberry") || userAgent.contains("mobile") ||
				userAgent.contains("windows ce") || userAgent.contains("opera mini") || userAgent.contains("palm")) {
			return new GenericDevice(request.getUserAgent(), true);
		} else {
			return null;
		}
	}

}
