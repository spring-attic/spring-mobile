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
	
	public boolean isNormal() {
		return this.deviceType == DeviceType.NORMAL;
	}

	public boolean isMobile() {
		return this.deviceType == DeviceType.MOBILE;
	}
	
	public boolean isTablet() {
		return this.deviceType == DeviceType.TABLET;
	}
	
	public void setMobile(boolean mobile) {
		deviceType = mobile ? DeviceType.MOBILE : DeviceType.NORMAL;
	}
	
}