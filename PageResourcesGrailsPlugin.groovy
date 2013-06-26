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

import org.grails.plugin.resources.page.ListFlowExecutionListenerLoader
import org.grails.plugin.resources.page.PageResourcesFlowExecutionListener
import org.grails.plugin.resources.page.PageResourcesInterceptor
import org.grails.plugin.resources.page.PageResourcesModuleRequester
import org.springframework.beans.PropertyValue
import org.springframework.beans.factory.config.RuntimeBeanReference
import org.springframework.core.io.FileSystemResource

class PageResourcesGrailsPlugin {
    def version = "0.2.4-SNAPSHOT"
    def grailsVersion = "2.0 > *"
    def dependsOn = [resources: "1.2.RC2"]
    def loadAfter = ['webflow']
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/i18n/messages.properties",
        "web-app/**",
        "src/docs/**"
    ]

    def title = "Page Resources Plugin"
    def author = "David M. Carr"
    def authorEmail = "david@carrclan.us"
    def description = 'Enhances the resources plugin by allowing for creation of "page" resource modules using convention over configuration.'

    def documentation = "http://commercehub-oss.github.io/grails-page-resources/docs"

    def license = "APACHE"

    def issueManagement = [ url: "https://github.com/commercehub-oss/grails-page-resources/issues" ]

    def scm = [ url: "https://github.com/commercehub-oss/grails-page-resources" ]

    def developers = [
        [name: "Álvaro Sánchez-Mariscal", email: "alvaro.sanchez@salenda.es"],
        [name: "Matt Hurne", email: "matt@thehurnes.com"]
    ]

    def watchedResources = [
        "file:./web-app/pages/**/*.*" // Watch for page resource changes
    ]

    def doWithSpring = {
        pageResourcesModuleRequester(PageResourcesModuleRequester)
        log.debug('Registering interceptor')
        pageResourcesInterceptor(PageResourcesInterceptor)
        if (manager?.hasGrailsPlugin('webflow')) {
            log.debug('Registering flow execution listener based on detection of webflow plugin')
            pageResourcesFlowExecutionListener(PageResourcesFlowExecutionListener)
            def flowListeners = [ref('pageResourcesFlowExecutionListener')]
            def oldExecutionListenerLoaderBeanDef = delegate.getBeanDefinition('executionListenerLoader')
            if (oldExecutionListenerLoaderBeanDef) {
                log.debug('Adding flow execution listener to existing execution listeners')
                oldExecutionListenerLoaderBeanDef.constructorArgumentValues.getGenericArgumentValues().each {
                    flowListeners.add(it.value)
                }
            }
            executionListenerLoader(ListFlowExecutionListenerLoader) {
                listeners = flowListeners
            }
            def flowExecutionFactoryBeanDef = delegate.getBeanDefinition('flowExecutionFactory')
            def executionListenerLoaderPropertyValue = flowExecutionFactoryBeanDef.propertyValues.getPropertyValue('executionListenerLoader')
            if (executionListenerLoaderPropertyValue == null) {
                log.debug("Registering executionListenerLoader with flowExecutionFactory")
                flowExecutionFactoryBeanDef.propertyValues.addPropertyValue(new PropertyValue('executionListenerLoader', ref("executionListenerLoader")))
            } else if (executionListenerLoaderPropertyValue.value instanceof RuntimeBeanReference && executionListenerLoaderPropertyValue.value.beanName == 'executionListenerLoader') {
                log.debug("executionListenerLoader already registered with flowExecutionFactory") // No changes needed
            } else {
                log.warn("Unknown executionListenerLoader registered for flowExecutionFactory; cannot automatically register executionListenerLoader")
            }
        }
    }

    def onChange = { event ->
        def manager = event.manager
        def resourcesPlugin = manager.getGrailsPlugin('resources').@plugin
        if (event.source instanceof FileSystemResource) {
            log.info("Scheduling reload of modules due to change of file $event.source.file")
            resourcesPlugin.triggerReload {
                event.application.mainContext.grailsResourceProcessor.reloadModules()
            }
        } else {
            log.info("Scheduling reload of modules due to change of $event.source.name")
            resourcesPlugin.triggerReload {
                event.application.mainContext.grailsResourceProcessor.reloadModules()
            }
        }
    }
}
