repositories {
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain:core"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:1.0.0-SNAPSHOT")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
