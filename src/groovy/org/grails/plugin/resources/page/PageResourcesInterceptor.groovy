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

import org.grails.plugin.resource.ResourceProcessor
import org.grails.plugin.resource.ResourceTagLib
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class PageResourcesInterceptor extends HandlerInterceptorAdapter {
    def log = LoggerFactory.getLogger(PageResourcesInterceptor.class)

    @Resource
    ResourceProcessor resourceProcessor

    @Resource
    ResourceTagLib resourceTagLib

    @Override
    void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        def viewName = modelAndView?.viewName
        if (viewName) {
            String moduleName = 'pages' + viewName.replaceAll('/', '_')
            def module = resourceProcessor.getModule(moduleName)
            if (module) {
                if (log.isDebugEnabled()) {
                    log.debug("Automatically requiring page module ${moduleName} for view ${viewName}")
                }
                resourceTagLib.require(module: moduleName)
            }
        }
    }
}
