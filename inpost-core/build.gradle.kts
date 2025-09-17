plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    alias(libs.plugins.dokka)
}

dependencies {
    api(libs.slf4j.api)
    api(libs.coroutines.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

kotlin {
    jvmToolchain(providers.gradleProperty("java.toolchain.version").get().toInt())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("${project.group}:inpost-core")
                description.set("Core client and models for InPost APIs")
                url.set(providers.gradleProperty("POM_URL"))
            }
        }
    }
}
