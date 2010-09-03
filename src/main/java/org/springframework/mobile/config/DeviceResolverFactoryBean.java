package org.springframework.mobile.config;

import net.sourceforge.wurfl.core.WURFLManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.mobile.AppleDeviceResolver;
import org.springframework.mobile.ChainedDeviceResolver;
import org.springframework.mobile.DeviceResolver;
import org.springframework.mobile.wurfl.WURFLDeviceResolver;
import org.springframework.util.ClassUtils;

public class DeviceResolverFactoryBean implements FactoryBean<DeviceResolver>, BeanFactoryAware {

	private static final boolean wurflPresent = ClassUtils.isPresent(
			"net.sourceforge.wurfl.core.WURFLManager;", DeviceResolverFactoryBean.class.getClassLoader());
	
	private BeanFactory beanFactory;

	private String wurflManagerBeanName;
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public DeviceResolver getObject() throws Exception {
		if (wurflManagerBeanName != null) {
			if (!wurflPresent) {
				throw new IllegalStateException("WURFL is not present on the classpath");
			}
			return new WURFLDeviceResolver(beanFactory.getBean(wurflManagerBeanName, WURFLManager.class));
		}
		ChainedDeviceResolver deviceResolver = new ChainedDeviceResolver();
		deviceResolver.add(new AppleDeviceResolver());
		return deviceResolver;
	}

	public Class<?> getObjectType() {
		return DeviceResolver.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
