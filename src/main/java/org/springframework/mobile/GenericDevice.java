package org.springframework.mobile;

public class GenericDevice implements Device {

	private String userAgent;

	private boolean mobileBrowser;
	
	private boolean apple;
	
	public GenericDevice(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getUserAgent() {
		return userAgent;
	}

	public boolean isMobileBrowser() {
		return mobileBrowser;
	}

	public void setMobileBrowser(boolean mobileBrowser) {
		this.mobileBrowser = mobileBrowser;
	}

	public boolean isApple() {
		return apple;
	}

	public void setApple(boolean apple) {
		this.apple = apple;
	}
	
}
