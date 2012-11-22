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
import org.grails.plugin.resource.ResourceProcessor
import org.grails.plugin.resource.ResourceTagLib
import org.slf4j.LoggerFactory

class PageResourcesModuleRequester {
    def log = LoggerFactory.getLogger(PageResourcesModuleRequester.class)

    @Resource
    ResourceProcessor resourceProcessor

    @Resource
    ResourceTagLib resourceTagLib

    void requestModuleForView(String viewName) {
        if (viewName) {
            String moduleName = 'pages' + viewName.replaceAll('/', '_')
            if (resourceProcessor.getModule(moduleName)) {
                log.debug('Automatically requiring page module {} for view {}', moduleName, viewName)
                resourceTagLib.require(module: moduleName)
            } else {
                log.debug('No page module {} defined for view {}', moduleName, viewName)
            }
        }
    }
}
