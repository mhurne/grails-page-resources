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

import org.grails.plugin.resources.page.PageResourcesInterceptor
import org.springframework.core.io.FileSystemResource

class PageResourcesGrailsPlugin {
    def version = "0.2.0-SNAPSHOT"
    def grailsVersion = "1.3 > *"
    def dependsOn = [resources: "1.2-RC1"]
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/i18n/messages.properties",
        "web-app/**",
        "src/docs/**"
    ]

    def title = "Page Resources Plugin"
    def author = "David M. Carr"
    def authorEmail = "david@carrclan.us"
    def description = '''\
Enhances the resources plugin by allowing for creation of "page" resource modules using convention over configuration.
'''

    def documentation = "http://davidmc24.github.com/grails-page-resources/"

    def license = "APACHE"

    def issueManagement = [ url: "https://github.com/davidmc24/grails-page-resources/issues" ]

    def scm = [ url: "https://github.com/davidmc24/grails-page-resources/" ]

    def developers = [
        [name: "Álvaro Sánchez-Mariscal", email: "alvaro.sanchez@salenda.es"]
    ]

    def watchedResources = [
        "file:./web-app/pages/**/*.*" // Watch for page resource changes
    ]

    def doWithSpring = {
        pageResourcesInterceptor(PageResourcesInterceptor)
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
