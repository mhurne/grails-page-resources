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
                    [accept: { File f -> f.isFile() && f.name ==~ /.*\.(js|less|css)/}] as FileFilter
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
