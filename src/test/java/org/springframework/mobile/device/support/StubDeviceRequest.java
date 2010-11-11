package org.springframework.mobile.device.support;

import org.springframework.mobile.device.resolver.DeviceRequest;

class StubDeviceRequest implements DeviceRequest {

	private String userAgent;
	
	public StubDeviceRequest() {
		this.userAgent = "test";
	}
	
	public StubDeviceRequest(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getUserAgent() {
		return userAgent;
	}

}
