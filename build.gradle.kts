import com.diffplug.spotless.LineEnding
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.spotless)
    alias(libs.plugins.nexusPublish)
}

allprojects {
    group = providers.gradleProperty("group").get()
    version = providers.gradleProperty("version").get()
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.jvm") {
        extensions.configure<org.gradle.api.plugins.JavaPluginExtension>("java") {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(providers.gradleProperty("java.toolchain.version").get().toInt()))
            }
            withSourcesJar()
            withJavadocJar()
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }

        tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = providers.gradleProperty("kotlin.jvm.target").get()
                freeCompilerArgs = freeCompilerArgs +
                    listOf(
                        "-Xjsr305=strict",
                        "-Xjvm-default=all",
                    )
            }
        }

        dependencies {
            "testImplementation"(platform(libs.junit.bom))
            "testImplementation"(libs.junit.jupiter)
            "testRuntimeOnly"(libs.junit.platform.launcher)
            "testImplementation"(libs.mockk)
            "testImplementation"(libs.kotest.assertions)
        }

        pluginManager.withPlugin("maven-publish") {
            pluginManager.apply("signing")
            extensions.configure<PublishingExtension>("publishing") {
                publications {
                    withType<MavenPublication>().configureEach {
                        pom {
                            name.set(providers.gradleProperty("POM_NAME"))
                            description.set(providers.gradleProperty("POM_DESCRIPTION"))
                            url.set(providers.gradleProperty("POM_URL"))
                            licenses {
                                license {
                                    name.set(providers.gradleProperty("POM_LICENSE_NAME"))
                                    url.set(providers.gradleProperty("POM_LICENSE_URL"))
                                    distribution.set(providers.gradleProperty("POM_LICENSE_DIST"))
                                }
                            }
                            scm {
                                url.set(providers.gradleProperty("POM_SCM_URL"))
                                connection.set(providers.gradleProperty("POM_SCM_CONNECTION"))
                                developerConnection.set(providers.gradleProperty("POM_SCM_DEV_CONNECTION"))
                            }
                            developers {
                                developer {
                                    id.set(providers.gradleProperty("POM_DEVELOPER_ID"))
                                    name.set(providers.gradleProperty("POM_DEVELOPER_NAME"))
                                    email.set(providers.gradleProperty("POM_DEVELOPER_EMAIL"))
                                }
                            }
                        }
                    }
                }
            }

            extensions.configure<SigningExtension>("signing") {
                val signingKey: String? = providers.gradleProperty("signing.key").orNull
                val signingPassword: String? = providers.gradleProperty("signing.password").orNull
                if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(extensions.getByType(PublishingExtension::class.java).publications)
                }
            }
        }
    }
}

spotless {
    lineEndings = LineEnding.UNIX
    kotlin {
        ktlint(libs.versions.ktlint.get())
        // Only format Kotlin source files, not templates under gradle/
        target("**/src/**/*.kt")
        targetExclude("**/build/**", "gradle/**")
        licenseHeaderFile("gradle/spotless.license.kt", "package ")
    }
    kotlinGradle {
        ktlint(libs.versions.ktlint.get())
        target("**/*.gradle.kts")
        targetExclude("**/build/**", "inpost-contract-tests/postman/**")
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(providers.gradleProperty("sonatypeUsername").orNull)
            password.set(providers.gradleProperty("sonatypePassword").orNull)
        }
    }
}
