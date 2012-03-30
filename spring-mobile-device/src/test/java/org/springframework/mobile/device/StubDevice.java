package org.springframework.mobile.device;

import org.springframework.mobile.device.Device;

public class StubDevice implements Device {

	private boolean mobile;
	private boolean tablet;
	
	public StubDevice() {
		this.mobile = true;
		this.tablet = false;
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

	public boolean isTablet() {
		return tablet;
	}

	public void setTablet(boolean tablet) {
		this.tablet = tablet;
	}
	
}