plugins {
    id("java")
    id("org.springframework.boot") version libs.versions.springBoot.get()
}

group = "com.cdg.ngp.esb"
version = "1.0-SNAPSHOT"

configurations.all {
    resolutionStrategy {
        eachDependency {
            println("Checking dependency: ${requested.group}:${requested.name}:${requested.version}")
            if (requested.group == "org.apache.activemq") {
                useVersion(libs.versions.activeMq.get())
            }
            if (requested.group == "io.netty") {
                useVersion(libs.versions.netty.get())
            }
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation(rootProject.libs.netty)
    implementation(rootProject.libs.camelNetty)

    implementation("sg.com.cdgtaxi.comms:cdgCommsTlv:3.6.6.9.5")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}