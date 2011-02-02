package org.springframework.mobile.device.wurfl;

import java.util.Collections;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class WurflManagerFactoryBeanTest {
	
	@Test
	public void customRoot() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.afterPropertiesSet();
		factory.getObject();
	}

	@Test
	public void customRootAndPatches() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.setPatchLocations(Collections.singletonList(new ClassPathResource("test-wurfl-patch.xml", getClass())));
		factory.afterPropertiesSet();
		factory.getObject();
	}

}
