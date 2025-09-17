plugins {
    `java-platform`
    `maven-publish`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        api(project(":inpost-core"))
        api(project(":inpost-spring-starter"))
        api("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}")
        api("org.slf4j:slf4j-api:${libs.versions.slf4j.get()}")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["javaPlatform"])
            pom {
                name.set("${project.group}:inpost-bom")
                description.set("BOM for InPost JVM SDK modules")
                url.set(providers.gradleProperty("POM_URL"))
            }
        }
    }
}
