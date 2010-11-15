package org.springframework.mobile.device.wurfl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import net.sourceforge.wurfl.core.WURFLManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpServletRequest;

public class WurflDeviceResolutionServiceTests {

	private WURFLManager manager;
	
	@Before
	public void setUp() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.afterPropertiesSet();
		manager = factory.getObject();
	}
	
	@Test
	public void resolveMobile() throws Exception {
		WurflDeviceResolutionService service = new WurflDeviceResolutionService(manager);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("User-Agent", "AUDIOVOX-CDM180");
		Device device = service.resolveDevice(request);
		assertTrue(device.isMobileBrowser());
	}

	@Test
	public void resolveWebBrowserNoPatch() throws Exception {
		WurflDeviceResolutionService service = new WurflDeviceResolutionService(manager);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("User-Agent", "Firefox");
		Device device = service.resolveDevice(request);
		assertFalse(device.isMobileBrowser());
	}

	@Test
	public void resolveWebBrowserPatch() throws Exception {
		WurflManagerFactoryBean factory = new WurflManagerFactoryBean(new ClassPathResource("test-wurfl.xml", getClass()));
		factory.setPatchLocations(Collections.singletonList(new ClassPathResource("test-wurfl-patch.xml", getClass())));
		factory.afterPropertiesSet();
		manager = factory.getObject();

		WurflDeviceResolutionService service = new WurflDeviceResolutionService(manager);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("User-Agent", "Firefox");
		Device device = service.resolveDevice(request);
		System.out.println(device);
		assertFalse(device.isMobileBrowser());
	}

}
