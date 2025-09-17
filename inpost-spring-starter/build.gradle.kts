plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    id("org.jetbrains.kotlin.kapt")
    `maven-publish`
}

dependencies {
    api(project(":inpost-core"))
    compileOnly(libs.spring.boot.autoconfigure)
    implementation(libs.spring.context)
    kapt(libs.spring.boot.configuration.processor)
}

kotlin {
    jvmToolchain(providers.gradleProperty("java.toolchain.version").get().toInt())
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("${project.group}:inpost-spring-starter")
                description.set("Spring Boot autoconfiguration for InPost JVM SDK")
                url.set(providers.gradleProperty("POM_URL"))
            }
        }
    }
}
