/*
 * Copyright 2012 David M. Carr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.grails.plugin.resources.page

import javax.annotation.Resource
import org.springframework.webflow.core.collection.MutableAttributeMap
import org.springframework.webflow.definition.StateDefinition
import org.springframework.webflow.execution.FlowExecutionListenerAdapter
import org.springframework.webflow.execution.FlowSession
import org.springframework.webflow.execution.RequestContext
import org.springframework.webflow.execution.View

class PageResourcesFlowExecutionListener extends FlowExecutionListenerAdapter {
    @Resource
    PageResourcesModuleRequester pageResourcesModuleRequester

    void sessionEnding(RequestContext context, FlowSession session, String outcome, MutableAttributeMap output) {
        String viewName = "/${session.flow.id}/${session.state.id}"
        pageResourcesModuleRequester.requestModuleForView(viewName)
    }

    @Override
    void viewRendering(RequestContext context, View view, StateDefinition viewState) {
        String url = view.view.url[1..-1]
        String viewName = url.lastIndexOf('.').with {it != -1 ? url[0..<it] : url}
        pageResourcesModuleRequester.requestModuleForView(viewName)
    }
}
