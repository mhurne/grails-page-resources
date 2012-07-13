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
