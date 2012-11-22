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

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.grails.plugin.resource.ResourceProcessor
import org.slf4j.LoggerFactory

def log = LoggerFactory.getLogger('org.grails.plugin.resources.page.PageResources')
def application = ApplicationHolder.application
def config = application.config
ResourceProcessor grailsResourceProcessor = application.mainContext.getBean('grailsResourceProcessor')
def mainContext = application.mainContext

modules = {
    // This file should be named such that it comes alphabetically after any *Resources files that contain explicit
    // page module definitions.  Otherwise, it will overrride the other definition.

    def defaultBundleVal = config.flatten().get('grails.plugins.pageResources.defaultBundle')
    log.debug('defaultBundle configured as: {}', defaultBundleVal)
    def dependModuleName = 'defaultPageDependencies'
    def dependModule = delegate._modules.find { it.name == dependModuleName }
    def pageResources = mainContext.getResources('/pages/**')
    def moduleResources = pageResources.findAll{it.filename.empty}
    moduleResources.each { moduleResource ->
        String modulePath = moduleResource.path
        String moduleName = modulePath[1..-2].replaceAll(/[\/\\]/, '_')
        def module = delegate._modules.find { it.name == moduleName }
        if (module) {
            log.debug("Page module {} already defined; using pre-existing definition", moduleName)
        } else {
            def fileResources = pageResources.findAll{ pageResource ->
                !pageResource.filename.empty && pageResource.path.startsWith(modulePath) && pageResource.path.indexOf('/', modulePath.length()) == -1
            }.findAll{ pageResource ->
                boolean supported = grailsResourceProcessor.getDefaultSettingsForURI(pageResource.path)
                if (!supported) {
                    log.info("Ignoring file of unsupported type: {}", pageResource.path)
                }
                return supported
            }.sort()
            if (!fileResources.isEmpty()) {
                log.info("Defining page module: {}", moduleName)
                delegate.invokeMethod(moduleName) {
                    if (dependModule) {
                        dependsOn(dependModuleName)
                    }
                    if (defaultBundleVal instanceof CharSequence) {
                        defaultBundle(defaultBundleVal)
                    } else if (!defaultBundleVal) {
                        defaultBundle(false)
                    }
                    fileResources.each { fileResource ->
                        resource(url: fileResource.path)
                    }
                }
            }
        }
    }
}
