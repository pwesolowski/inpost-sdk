plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
    alias(libs.plugins.dokka)
}

dependencies {
    api(libs.slf4j.api)

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
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
