/*
 * Copyright 2010-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mobile.device;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LiteDeviceTest {

	@Test
	public void defaultDevice() {
		LiteDevice liteDevice = new LiteDevice();
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.NORMAL);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.UNKNOWN);
		assertThat(liteDevice.isNormal()).isTrue();
		assertThat(liteDevice.isMobile()).isFalse();
		assertThat(liteDevice.isTablet()).isFalse();
	}

	@Test
	public void normalDevice() {
		LiteDevice liteDevice = new LiteDevice(DeviceType.NORMAL);
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.NORMAL);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.UNKNOWN);
		assertThat(liteDevice.isNormal()).isTrue();
		assertThat(liteDevice.isMobile()).isFalse();
		assertThat(liteDevice.isTablet()).isFalse();
	}

	@Test
	public void mobileDevice() {
		LiteDevice liteDevice = new LiteDevice(DeviceType.MOBILE, DevicePlatform.IOS);
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.MOBILE);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.IOS);
		assertThat(liteDevice.isNormal()).isFalse();
		assertThat(liteDevice.isMobile()).isTrue();
		assertThat(liteDevice.isTablet()).isFalse();
	}

	@Test
	public void tabletDevice() {
		LiteDevice liteDevice = new LiteDevice(DeviceType.TABLET, DevicePlatform.ANDROID);
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.TABLET);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.ANDROID);
		assertThat(liteDevice.isNormal()).isFalse();
		assertThat(liteDevice.isMobile()).isFalse();
		assertThat(liteDevice.isTablet()).isTrue();
	}

	@Test
	public void normalInstance() {
		LiteDevice liteDevice = LiteDevice.NORMAL_INSTANCE;
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.NORMAL);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.UNKNOWN);
		assertThat(liteDevice.isNormal()).isTrue();
		assertThat(liteDevice.isMobile()).isFalse();
		assertThat(liteDevice.isTablet()).isFalse();
	}

	@Test
	public void mobileInstance() {
		LiteDevice liteDevice = LiteDevice.MOBILE_INSTANCE;
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.MOBILE);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.UNKNOWN);
		assertThat(liteDevice.isNormal()).isFalse();
		assertThat(liteDevice.isMobile()).isTrue();
		assertThat(liteDevice.isTablet()).isFalse();
	}

	@Test
	public void tabletInstance() {
		LiteDevice liteDevice = LiteDevice.TABLET_INSTANCE;
		assertThat(liteDevice.getDeviceType()).isEqualTo(DeviceType.TABLET);
		assertThat(liteDevice.getDevicePlatform()).isEqualTo(DevicePlatform.UNKNOWN);
		assertThat(liteDevice.isNormal()).isFalse();
		assertThat(liteDevice.isMobile()).isFalse();
		assertThat(liteDevice.isTablet()).isTrue();
	}

}
