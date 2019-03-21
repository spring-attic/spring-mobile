/*
 * Copyright 2010-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.mobile.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
import org.springframework.web.servlet.ViewResolver;

import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Mobile's
 * {@link LiteDeviceDelegatingViewResolver}. If available, each of the following
 * {@link ViewResolver} implementations will be configured:
 * <ul>
 *     <li>{@link ThymeleafViewResolver}</li>
 *     <li>{@link GroovyMarkupViewResolver}</li>
 *     <li>{@link FreeMarkerViewResolver}</li>
 *     <li>{@link MustacheViewResolver}</li>
 *     <li>{@link InternalResourceViewResolver}</li>
 * </ul>
 * @author Roy Clarkson
 * @author Stephane Nicoll
 * @since 2.0
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(LiteDeviceDelegatingViewResolver.class)
@ConditionalOnProperty(prefix = "spring.mobile.devicedelegatingviewresolver", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DeviceDelegatingViewResolverProperties.class)
@AutoConfigureAfter({ WebMvcAutoConfiguration.class, FreeMarkerAutoConfiguration.class,
		GroovyTemplateAutoConfiguration.class, MustacheAutoConfiguration.class,
		ThymeleafAutoConfiguration.class })
public class DeviceDelegatingViewResolverAutoConfiguration {

	@Configuration
	protected static class LiteDeviceDelegatingViewResolverFactoryConfiguration {

		@Bean
		public DeviceDelegatingViewResolverFactory deviceDelegatingViewResolverFactory(
				DeviceDelegatingViewResolverProperties properties) {
			return new DeviceDelegatingViewResolverFactory(properties);
		}

	}

	@Configuration
	@ConditionalOnClass(FreeMarkerViewResolver.class)
	protected static class DeviceDelegatingFreeMarkerViewResolverConfiguration {

		@Bean
		@ConditionalOnBean(FreeMarkerViewResolver.class)
		public LiteDeviceDelegatingViewResolver deviceDelegatingFreeMarkerViewResolver(
				DeviceDelegatingViewResolverFactory factory,
				FreeMarkerViewResolver viewResolver) {
			return factory.createViewResolver(viewResolver);
		}

	}

	@Configuration
	@ConditionalOnClass(GroovyMarkupViewResolver.class)
	protected static class DeviceDelegatingGroovyMarkupViewResolverConfiguration {

		@Bean
		@ConditionalOnBean(GroovyMarkupViewResolver.class)
		public LiteDeviceDelegatingViewResolver deviceDelegatingGroovyMarkupViewResolver(
				DeviceDelegatingViewResolverFactory factory,
				GroovyMarkupViewResolver viewResolver) {
			return factory.createViewResolver(viewResolver);
		}

	}

	@Configuration
	@ConditionalOnClass(InternalResourceViewResolver.class)
	protected static class DeviceDelegatingJspViewResolverConfiguration {

		@Bean
		@ConditionalOnBean(InternalResourceViewResolver.class)
		public LiteDeviceDelegatingViewResolver deviceDelegatingJspViewResolver(
				DeviceDelegatingViewResolverFactory factory,
				InternalResourceViewResolver viewResolver) {
			return factory.createViewResolver(viewResolver);
		}

	}

	@Configuration
	@ConditionalOnClass(MustacheViewResolver.class)
	protected static class DeviceDelegatingMustacheViewResolverConfiguration {

		@Bean
		@ConditionalOnBean(MustacheViewResolver.class)
		public LiteDeviceDelegatingViewResolver deviceDelegatingMustacheViewResolver(
				DeviceDelegatingViewResolverFactory factory,
				MustacheViewResolver viewResolver) {
			return factory.createViewResolver(viewResolver);
		}

	}

	@Configuration
	@ConditionalOnClass(ThymeleafViewResolver.class)
	protected static class DeviceDelegatingThymeleafViewResolverConfiguration {

		@Bean
		@ConditionalOnBean(ThymeleafViewResolver.class)
		public LiteDeviceDelegatingViewResolver deviceDelegatingThymeleafViewResolver(
				DeviceDelegatingViewResolverFactory factory,
				ThymeleafViewResolver viewResolver) {
			return factory.createViewResolver(viewResolver);
		}

	}

}
