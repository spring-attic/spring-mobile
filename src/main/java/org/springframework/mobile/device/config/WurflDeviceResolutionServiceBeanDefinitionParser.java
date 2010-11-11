package org.springframework.mobile.device.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.mobile.device.wurfl.WurflDeviceResolutionService;
import org.springframework.mobile.device.wurfl.WurflManagerFactoryBean;
import org.w3c.dom.Element;

class WurflDeviceResolutionServiceBeanDefinitionParser extends AbstractBeanDefinitionParser {

	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		Object source = parserContext.extractSource(element);
		RootBeanDefinition serviceDef = new RootBeanDefinition(WurflDeviceResolutionService.class);
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
