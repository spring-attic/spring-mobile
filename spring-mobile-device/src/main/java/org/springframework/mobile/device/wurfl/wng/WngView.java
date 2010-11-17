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
package org.springframework.mobile.device.wurfl.wng;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.wng.Constants;
import net.sourceforge.wurfl.wng.WNGDevice;
import net.sourceforge.wurfl.wng.component.ComponentException;
import net.sourceforge.wurfl.wng.component.Document;
import net.sourceforge.wurfl.wng.component.StyleContainer;
import net.sourceforge.wurfl.wng.component.ValidatorVisitor;
import net.sourceforge.wurfl.wng.renderer.DefaultDocumentRenderer;
import net.sourceforge.wurfl.wng.renderer.DefaultRendererGroupResolver;
import net.sourceforge.wurfl.wng.renderer.DocumentRenderer;
import net.sourceforge.wurfl.wng.renderer.RenderedDocument;
import net.sourceforge.wurfl.wng.style.StyleOptimizerVisitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.springframework.mobile.device.mvc.DeviceResolvingHandlerInterceptor;
import org.springframework.web.servlet.View;

/**
 * A Spring MVC {@link View} that renders a WNG {@link Document}, if one has been set in the current request by a 'target' view this class delegates to.
 * WNG aims to allow the developer to control the rendering of markup by device type in a declarative manner without resorting to manual if/else logic in his or her JSP templates.
 * When a WNG-based JSP view renders itself, the view builds a component tree that contains a {@link Document} object as its root element--no response writing is performed at that time.
 * After view rendering completes, this decorator finishes WNG processing by rendering the assembled Document.
 * That action triggers the device markup to be generated and written to the response.
 * @author Keith Donald
 */
public class WngView implements View {

	private final View target;
	
	private final DocumentRenderer documentRenderer;

	public WngView(View target) {
		this(target, new DefaultDocumentRenderer(new DefaultRendererGroupResolver()));
	}

	public WngView(View target, DocumentRenderer documentRenderer) {
		this.target = target;
		this.documentRenderer = documentRenderer;;
	}

	// implementing View
	
	public String getContentType() {
		return target.getContentType();
	}
	
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BufferedHttpServletResponse buffered = new BufferedHttpServletResponse(response);
		target.render(model, request, buffered);
		// logic adapted from WNGContextFilter which has the same responsibility
		if (isWngDocumentCreated(request)) {
			WNGDevice device = new WNGDevice((Device) request.getAttribute(DeviceResolvingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE));
			Document document = resolveDocument(request);
			StyleContainer styleContainer = (StyleContainer)CollectionUtils.find(document.getHead().getChildren(), new InstanceofPredicate(StyleContainer.class));
			if (styleContainer == null) {
				styleContainer = new StyleContainer();
				document.addToHead(styleContainer);
			}
			StyleOptimizerVisitor visitor = new StyleOptimizerVisitor(device, styleContainer);
			document.accept(visitor);
			RenderedDocument renderedDocument = documentRenderer.renderDocument(document, device);
			writeDocument(renderedDocument, response);			
		} else {
			buffered.writeTo(response.getOutputStream());
		}
	}

	private boolean isWngDocumentCreated(ServletRequest request) {
		return request.getAttribute(Constants.ATT_DOCUMENT) != null;
	}
	
	private Document resolveDocument(ServletRequest request) throws ComponentException {
		Document document = (Document) request.getAttribute(Constants.ATT_DOCUMENT);
		ValidatorVisitor validatorVisitor = new ValidatorVisitor();
		document.accept(validatorVisitor);
		return document;
	}
	
	private void writeDocument(RenderedDocument renderedDocument, HttpServletResponse response) throws IOException {
		response.setContentType(renderedDocument.getContentType());
		response.getWriter().print(renderedDocument.getMarkup());
		response.getWriter().flush();
	}

}