package org.springframework.mobile.device.support;

import org.springframework.mobile.device.resolver.DeviceRequest;

class TestDeviceRequest implements DeviceRequest {

	private String userAgent;
	
	public TestDeviceRequest() {
		this.userAgent = "test";
	}
	
	public TestDeviceRequest(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

}
