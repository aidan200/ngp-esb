plugins {
    id ("java")
    id ("idea")
    id ("maven-publish")
    id("io.spring.dependency-management") version libs.versions.springDependencyManagement.get()
    id("org.springframework.boot") version libs.versions.springBoot.get()
}

group = "com.cdg.ngp.esb"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

allprojects{
    repositories {
        mavenLocal()
//        maven {
//            name = "fusesource.m2"
//            url = uri("https://repo.fusesource.com/nexus/content/groups/public")
//            isAllowInsecureProtocol = true
//        }
//        maven {
//            name = "fusesource.ea"
//            url = uri("https://repo.fusesource.com/nexus/content/groups/ea")
//            isAllowInsecureProtocol = true
//        }
        mavenCentral()
    }
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "maven-publish")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    dependencies {
        // Check if the current sub project is a common project to avoid circular dependencies
        if (project.name != "esb-common") {
            implementation(project(":esb-common"))
        }
        implementation(platform(rootProject.libs.springBootCamelBom))
        implementation("org.apache.camel.springboot:camel-spring-boot-starter")
        //implementation("org.apache.camel:camel-core-xml:4.3.0")
        implementation("org.apache.camel:camel-jms:4.3.0")
        implementation("org.apache.camel:camel-activemq:4.3.0")
        implementation("javax.jms:javax.jms-api:2.0.1")
        implementation("org.aspectj:aspectjrt:1.9.21")
        //implementation(rootProject.libs.activemqBroker)
        //implementation(rootProject.libs.activemqPool)
        implementation(rootProject.libs.activemqSpring)
        implementation(rootProject.libs.springJms)
        implementation(rootProject.libs.lombok)
        implementation(rootProject.libs.mapstruct)
        annotationProcessor(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.mapstruct)

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.apache.camel:camel-test-spring-junit5:4.3.0")
    }

    publishing {
        repositories {
            mavenLocal()
        }
    }

    tasks.register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
}

tasks.test {
    useJUnitPlatform()
}