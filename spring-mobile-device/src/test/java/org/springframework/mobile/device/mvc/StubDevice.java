package org.springframework.mobile.device.mvc;

import org.springframework.mobile.device.Device;

public class StubDevice implements Device {

	private boolean mobile;
	
	public StubDevice() {
		this.mobile = true;
	}

	public StubDevice(boolean mobile) {
		this.mobile = mobile;
	}

	public boolean isMobile() {
		return mobile;
	}
	
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
}