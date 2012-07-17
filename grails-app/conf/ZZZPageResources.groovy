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
import org.slf4j.LoggerFactory

def log = LoggerFactory.getLogger('org.grails.plugin.resource.page.PageResources')
def application = ApplicationHolder.application

modules = {
    // This file should be named such that it comes alphabetically after any *Resources files that contain explicit
    // page module definitions.  Otherwise, it will overrride the other definition.

    def rootDir = application.parentContext.getResource("/").getFile()
    def pagesDir = new File(rootDir, 'pages')
    int prefixLen = rootDir.path.length()
    def dependModuleName = 'defaultPageDependencies'
    def dependModule = delegate._modules.find { it.name == dependModuleName }
    if (pagesDir.isDirectory()) {
        pagesDir.eachDirRecurse { File moduleDir ->
            String moduleName = moduleDir.path.substring(prefixLen + 1).replaceAll(/[\/\\]/, '_')
            def module = delegate._modules.find { it.name == moduleName }
            if (module) {
                if (log.isDebugEnabled()) {
                    log.debug("Page module ${moduleName} already defined; using pre-existing definition")
                }
            } else {
                File[] files = moduleDir.listFiles(
                    [accept: { File f -> f.isFile() }] as FileFilter
                ).sort()
                if (files.length > 0) {
                    if (log.isInfoEnabled()) {
                        log.info("Defining page module: ${moduleName}")
                    }
                    delegate.invokeMethod(moduleName, {
                        if (dependModule) {
                            dependsOn(dependModuleName)
                        }
                        defaultBundle(false)
                        files.each { file ->
                            def url = file.path.substring(prefixLen)
                            resource(url: url)
                        }
                    })
                }
            }
        }
    }
}
