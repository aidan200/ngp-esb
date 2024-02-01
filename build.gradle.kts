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

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

allprojects{
    repositories {
        mavenLocal()
        maven {
            name = "hw.m2"
            url = uri("http://10.30.7.166:8081/repository/maven-public/")
            isAllowInsecureProtocol = true
        }
        maven {
            name = "cdg"
            url = uri("http://10.2.140.85:8081/nexus/content/groups/public/")
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "maven-publish")
    apply(plugin = "io.spring.dependency-management")
    //apply(plugin = "org.springframework.boot")

    dependencies {
        // Check if the current sub project is a common project to avoid circular dependencies
        if (project.name != "esb-common") {
            implementation(project(":esb-common"))
        }
        implementation(platform(rootProject.libs.springBootBom))
        implementation(platform(rootProject.libs.springBootCamelBom))
        implementation(rootProject.libs.camelJms)
        implementation(rootProject.libs.camelActivemq)
        implementation(rootProject.libs.javaxJmsApi)
        implementation(rootProject.libs.aspectjrt)
        implementation(rootProject.libs.activemqSpring)
        implementation(rootProject.libs.springJms)
        implementation(rootProject.libs.lombok)
        //implementation(rootProject.libs.mapstruct)
        annotationProcessor(rootProject.libs.lombok)
        //annotationProcessor(rootProject.libs.mapstruct)
        implementation(rootProject.libs.commonsLang3)
        implementation(rootProject.libs.gson)
        implementation(rootProject.libs.httpclient)

        implementation("org.apache.camel:camel-kafka:4.3.0")
        implementation ("org.springframework.kafka:spring-kafka:3.1.1")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")

        implementation("org.apache.camel.springboot:camel-spring-boot-starter")

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