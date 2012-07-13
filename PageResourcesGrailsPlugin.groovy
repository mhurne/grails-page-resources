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

class PageResourcesGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "1.3 > *"
    def dependsOn = [resources: "1.2-RC1"]
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/i18n/messages.properties",
        "web-app/**"
    ]

    def title = "Page Resources Plugin"
    def author = "David M. Carr"
    def authorEmail = "david@carrclan.us"
    def description = '''\
Enhances the resources plugin by allowing for creation of "page" resource modules using convention over configuration.
'''

    def documentation = "http://grails.org/plugin/page-resources"

    def license = "APACHE"

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/davidmc24/grails-page-resources/" ]
//    def scm = [ url: "http://svn.grails-plugins.codehaus.org/browse/grails-plugins/" ]

    def doWithSpring = {
        pageResourcesInterceptor(PageResourcesInterceptor)
    }
}
