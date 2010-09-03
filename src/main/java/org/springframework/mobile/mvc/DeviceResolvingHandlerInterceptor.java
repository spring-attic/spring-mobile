package org.springframework.mobile.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.Device;
import org.springframework.mobile.DeviceResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class DeviceResolvingHandlerInterceptor implements HandlerInterceptor {

	public static final String CURRENT_DEVICE_ATTRIBUTE = "currentDevice";
	
	private DeviceResolver deviceResolver;

	public DeviceResolvingHandlerInterceptor(DeviceResolver deviceResolver) {
		this.deviceResolver = deviceResolver;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String userAgent = request.getHeader("User-Agent");
		Device device = deviceResolver.resolveDeviceForUserAgent(userAgent);
		request.setAttribute(CURRENT_DEVICE_ATTRIBUTE, device);
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {	
	}
}
