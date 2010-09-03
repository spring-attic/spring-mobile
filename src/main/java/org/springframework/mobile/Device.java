package org.springframework.mobile;

public interface Device {

	String getUserAgent();

	boolean isMobileBrowser();

	boolean isApple();

}