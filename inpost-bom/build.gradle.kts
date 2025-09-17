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
        api("com.squareup.okhttp3:okhttp:${libs.versions.okhttp.get()}")
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
