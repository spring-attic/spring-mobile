package org.springframework.mobile;

public interface DeviceResolver {

	Device resolveDeviceForUserAgent(String userAgent);

}
