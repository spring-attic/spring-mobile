package org.springframework.mobile.device.resolver;

import org.springframework.mobile.device.resolver.DeviceRequest;

public class StubDeviceRequest implements DeviceRequest {

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
