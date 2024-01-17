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
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    implementation("com.oracle.database.jdbc:ojdbc8:23.3.0.23.09")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}