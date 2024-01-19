plugins {
    id("java")
}

group = "com.cdg.ngp.esb"
version = "1.0-SNAPSHOT"

//tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//    enabled = false
//}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}