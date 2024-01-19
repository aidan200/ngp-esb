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
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation(rootProject.libs.ojdbc8)

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}