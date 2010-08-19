package org.springframework.mobile;

import net.sourceforge.wurfl.core.DefaultDeviceProvider;
import net.sourceforge.wurfl.core.DefaultWURFLManager;
import net.sourceforge.wurfl.core.DefaultWURFLService;
import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.handlers.matchers.MatcherManager;
import net.sourceforge.wurfl.core.resource.DefaultWURFLModel;
import net.sourceforge.wurfl.core.resource.SpringXMLResource;
import net.sourceforge.wurfl.core.resource.WURFLResources;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.InputStreamResource;

public class WURFLManagerFactoryBean implements FactoryBean<WURFLManager>, InitializingBean {

	private WURFLManager manager;
	
	public void afterPropertiesSet() throws Exception {
		this.manager = initWURFLManager();
	}

	// implementing FactoryBean
	
	public Class<?> getObjectType() {
		return WURFLManager.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public WURFLManager getObject() throws Exception {
		return manager;
	}
	
	protected WURFLManager initWURFLManager() {
		WURFLResources resources = new WURFLResources();
		resources.add(new SpringXMLResource(new InputStreamResource(getClass().getClassLoader().getResourceAsStream(
		        "org/springframework/mobile/web_browsers_patch.xml"))));

		DefaultWURFLModel model = new DefaultWURFLModel(new SpringXMLResource(new InputStreamResource(getClass()
		        .getClassLoader().getResourceAsStream("org/springframework/mobile/wurfl.xml"))), resources);
		MatcherManager matcherManager = new MatcherManager(model);
		DefaultDeviceProvider deviceProvider = new DefaultDeviceProvider(model);
		DefaultWURFLService service = new DefaultWURFLService(matcherManager, deviceProvider);
		return new DefaultWURFLManager(service);		
	}

}