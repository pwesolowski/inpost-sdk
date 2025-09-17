plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    testImplementation(project(":inpost-core"))
    testImplementation(libs.pact.consumer.junit)
    testImplementation(libs.wiremock)
    testImplementation(libs.junit.jupiter)
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

kotlin {
    jvmToolchain(providers.gradleProperty("java.toolchain.version").get().toInt())
}

// Placeholder task to indicate Postman collection usage
val postmanCollectionsDir = layout.projectDirectory.dir("postman")

tasks.register("copyPostmanCollections", Copy::class) {
    from(rootProject.layout.projectDirectory.dir("postman_collection")) {
        include("*.postman_collection.json")
        include("*environment*.postman_environment.json")
    }
    into(postmanCollectionsDir)
}

tasks.withType<Test>().configureEach {
    dependsOn("copyPostmanCollections")
    useJUnitPlatform()
}
