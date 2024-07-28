dependencies {
    implementation(project(":common"))
    implementation(project(":domain:core"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
