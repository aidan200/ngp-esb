plugins {
    id("java")
}

group = "com.cdg.ngp.esb"
version = "1.0-SNAPSHOT"

configurations.all {
    resolutionStrategy {
        eachDependency {
            println("Checking dependency: ${requested.group}:${requested.name}:${requested.version}")
            if (requested.group == "org.apache.activemq") {
                useVersion("6.0.1")
            }
            if (requested.group == "io.netty") {
                useVersion("4.1.105.Final")
            }
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    implementation("io.netty:netty-all:4.1.105.Final")
    implementation("org.apache.camel:camel-netty:4.3.0")

    implementation("sg.com.cdgtaxi.comms:cdgCommsTlv:3.6.6.9.5")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}