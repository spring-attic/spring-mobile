package org.springframework.mobile.device;

import org.springframework.mobile.device.Device;

public class StubDevice implements Device {

	private DeviceType deviceType;

	/**
	 * @return new 'normal' device
	 */
	public StubDevice() {
		this.deviceType = DeviceType.NORMAL;
	}

	/**
	 * @param deviceType
	 * @return new device based deviceType
	 */
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

	public DeviceType getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

}