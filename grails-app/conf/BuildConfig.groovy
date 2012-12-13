grails.project.work.dir = 'target'
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
    inherits("global")
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        //mavenLocal()
    }
    dependencies {
        build("org.springframework.webflow:spring-webflow:2.3.1.RELEASE")
    }
    plugins {
        compile(":resources:1.2.RC2")
        build(":release:2.2.0") {
            export = false
        }
    }
}
