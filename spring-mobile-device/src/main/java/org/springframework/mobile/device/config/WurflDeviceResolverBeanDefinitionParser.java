/*
 * Copyright 2010-2011 the original author or authors.
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
package org.springframework.mobile.device.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.mobile.device.wurfl.WurflDeviceResolver;
import org.springframework.mobile.device.wurfl.WurflManagerFactoryBean;
import org.w3c.dom.Element;

class WurflDeviceResolverBeanDefinitionParser extends AbstractBeanDefinitionParser {

	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		Object source = parserContext.extractSource(element);
		RootBeanDefinition serviceDef = new RootBeanDefinition(WurflDeviceResolver.class);
		serviceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		serviceDef.setSource(source);
		
		RootBeanDefinition managerDef = new RootBeanDefinition(WurflManagerFactoryBean.class);
		managerDef.getConstructorArgumentValues().addIndexedArgumentValue(0, element.getAttribute("root-location"));
		if (element.hasAttribute("patch-locations")) {
			managerDef.getPropertyValues().add("patchLocations", element.getAttribute("patch-locations"));
		}
		managerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		managerDef.setSource(source);

		serviceDef.getConstructorArgumentValues().addIndexedArgumentValue(0, managerDef);
		return serviceDef;
	}
	
}
