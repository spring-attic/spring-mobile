package org.springframework.mobile.device.wurfl.wng;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * Wraps View instances returned by a target ViewResolver in {@link WngView} decorators in order build in WNG-awareness.
 * @author Keith Donald
 * @see WngView
 */
public class WngViewResolver implements ViewResolver {

	private final ViewResolver target;
	
	public WngViewResolver(ViewResolver target) {
		this.target = target;
	}

	public View resolveViewName(String viewName, Locale locale) throws Exception {
		View view = target.resolveViewName(viewName, locale);
		return view != null ? new WngView(view) : null;
	}
	
}
