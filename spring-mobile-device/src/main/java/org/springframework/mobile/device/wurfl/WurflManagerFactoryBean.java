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
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Factory that constructs the central {@link WURFLManager} and exports it as a Spring bean that can be injected into other beans.
 * The specifics of this factory class are hidden when working with the wurfl-device-resolver tag in the spring-mobile-device XML namespace.
 * @author Keith Donald
 */
public class WurflManagerFactoryBean implements FactoryBean<WURFLManager>, InitializingBean {

	private final Resource rootLocation;
	
	private List<? extends Resource> patchLocations;
	
	private WURFLManager manager;

	/**
	 * Constructs a WurflManagerFactoryBean that loads the root Device model from the XML file at the specified resource path.
	 * The specified resource must be resolvable to a file on the filesystem.
	 * @param rootLocation the path to the root device model XML file
	 */
	public WurflManagerFactoryBean(Resource rootLocation) {
		Assert.notNull(rootLocation, "The rootResource property cannot be null");
		this.rootLocation = rootLocation;
	}

	/**
	 * Set additional resource paths for patches that should be applied atop the root model.
	 * If not set, no patches will be applied.
	 * The specified resources must be resolvable to files on the filesystem.
	 * @param patchLocations the XML-based patch resources to apply
	 */
	public void setPatchLocations(List<? extends Resource> patchLocations) {
		this.patchLocations = patchLocations;
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
		return new SpringXMLResource(rootLocation);
	}
	
	private WURFLResources getPatches() {
		if (patchLocations == null) {
			return null;
		}
		WURFLResources patches = new WURFLResources();
		for (Resource patch : patchLocations) {
			patches.add(new SpringXMLResource(patch));
		}
		return patches;
	}
	
}