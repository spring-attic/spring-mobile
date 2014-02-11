/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mobile.device.site;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Spring MVC {@link WebArgumentResolver} that resolves @Controller MethodParameters of type {@link SitePreference}
 * to the value of the web request's {@link SitePreferenceHandler#CURRENT_SITE_PREFERENCE_ATTRIBUTE current site preference attribute}.
 * @author Keith Donald
 */
public class SitePreferenceWebArgumentResolver implements WebArgumentResolver {
	
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		if (SitePreference.class.isAssignableFrom(param.getParameterType())) {
			return SitePreferenceUtils.getCurrentSitePreference(request);
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
	
}
