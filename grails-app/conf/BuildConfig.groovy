grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6

grails.project.dependency.resolution = {
    inherits("global")
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsCentral()
        //mavenLocal()
    }
    dependencies {
        build("org.springframework.webflow:spring-webflow:2.3.1.RELEASE") {
            export = false
        }
    }
    plugins {
        compile(":resources:1.2.RC2")
        build(":release:2.2.0") {
            export = false
        }
    }
}
