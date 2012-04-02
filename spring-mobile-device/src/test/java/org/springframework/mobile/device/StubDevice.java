package org.springframework.mobile.device;

import org.springframework.mobile.device.Device;

public class StubDevice implements Device {

	private DeviceType deviceType;
	
	public StubDevice() {
		this.deviceType = DeviceType.MOBILE;
	}

	public StubDevice(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isMobile() {
		return deviceType == DeviceType.MOBILE;
	}
	
	public boolean isTablet() {
		return deviceType == DeviceType.TABLET;
	}
	
	public void setMobile(boolean mobile) {
		deviceType = mobile ? DeviceType.MOBILE : DeviceType.NORMAL;
	}
	
}