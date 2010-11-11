package org.springframework.mobile.device.wurfl;

import java.util.Collections;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class WurflManagerFactoryBeanTests {
	
	@Test
	public void defaultWurflManager() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean();
		factory.afterPropertiesSet();
		factory.getObject();
	}

	@Test
	public void customRoot() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean();
		factory.setRootResource(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.afterPropertiesSet();
		factory.getObject();
	}

	@Test
	public void customPatches() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean();
		factory.setRootResource(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.setPatchResources(Collections.singletonList(new ClassPathResource("test-wurfl-patch.xml", getClass())));
		factory.afterPropertiesSet();
		factory.getObject();
	}

}
