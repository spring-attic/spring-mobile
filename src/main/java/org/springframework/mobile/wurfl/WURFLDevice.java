package org.springframework.mobile.wurfl;

import net.sourceforge.wurfl.core.handlers.AppleHandler;

import org.springframework.mobile.Device;

public class WURFLDevice implements Device {

	private net.sourceforge.wurfl.core.Device device;
	
	public WURFLDevice(net.sourceforge.wurfl.core.Device device) {
		this.device = device;
	}
	
	public String getUserAgent() {
		return device.getUserAgent();
	}

	public boolean isMobileBrowser() {
		String mobileBrowserCapability = device.getCapability("mobile_browser");
		return mobileBrowserCapability != null && mobileBrowserCapability.length() > 0;
	}

	public boolean isApple() {
		return new AppleHandler().canHandle(device.getUserAgent());
	}
	
	public net.sourceforge.wurfl.core.Device getDevice() {
		return device;
	}

}
