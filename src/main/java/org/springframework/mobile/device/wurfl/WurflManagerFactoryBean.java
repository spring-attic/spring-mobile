/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.mobile.device.wurfl;

import java.util.List;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Factory that constructs the central {@link WURFLManager} and exports it as a Spring bean that can be injected into other beans.
 * @author Keith Donald
 */
public class WurflManagerFactoryBean implements FactoryBean<WURFLManager>, InitializingBean {

	private Resource rootResource;
	
	private List<? extends Resource> patchResources;
	
	private WURFLManager manager;
	
	/**
	 * Sets the resource path to the XML-based root device model.
	 * If not set, defaults to a ClassPathResource pointing to org/springframework/mobile/device/wurfl/wurfl-${version}.xml.
	 * @param rootResource the root resource path
	 */
	public void setRootResource(Resource rootResource) {
		this.rootResource = rootResource;
	}
	
	/**
	 * Set the resource paths to patches that should be applied atop the root model.
	 * If not set, defaults to a single ClasspathResource pointing to org/springframework/mobiel/device/wurfl/web_browsers_patch.xml.
	 * @param patchResources the patch resources to apply
	 */
	public void setPatchResources(List<? extends Resource> patchResources) {
		this.patchResources = patchResources;
	}
	
	// implementing InitializingBean
	
	public void afterPropertiesSet() throws Exception {
		this.manager = createWURFLManager();
	}

	// implementing FactoryBean
	
	public Class<?> getObjectType() {
		return WURFLManager.class;
	}

	public WURFLManager getObject() throws Exception {
		return manager;
	}

	public boolean isSingleton() {
		return true;
	}

	// internal helpers
	
	private WURFLManager createWURFLManager() {
		DefaultWURFLModel model = new DefaultWURFLModel(getRoot(), getPatches());
		MatcherManager matcherManager = new MatcherManager(model);
		DefaultDeviceProvider deviceProvider = new DefaultDeviceProvider(model);
		DefaultWURFLService service = new DefaultWURFLService(matcherManager, deviceProvider);
		return new DefaultWURFLManager(service);		
	}

	private SpringXMLResource getRoot() {
		if (rootResource != null) {
			return new SpringXMLResource(rootResource);
		} else {
			return new SpringXMLResource(new ClassPathResource(DEFAULT_WURFL_ROOT, getClass()));
		}
	}
	
	private WURFLResources getPatches() {
		WURFLResources patches = new WURFLResources();
		if (patchResources != null) {
			for (Resource patch : patchResources) {
				patches.add(new SpringXMLResource(patch));
			}
		} else {
			patches.add(new SpringXMLResource(new ClassPathResource(DEFAULT_WEB_BROWSERS_PATCH, getClass())));
		}
		return patches;
	}
	
	private static final String DEFAULT_WURFL_ROOT = "wurfl-2.0.25.xml";

	private static final String DEFAULT_WEB_BROWSERS_PATCH = "web_browsers_patch.xml";

}