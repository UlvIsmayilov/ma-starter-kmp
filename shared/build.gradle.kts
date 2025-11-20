import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidLibrary {
        namespace = "az.pashabank.starterkmp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(
                        JvmTarget.JVM_11
                    )
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.okhttp)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.darwin)
        }

        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)

            //Koin
            implementation(libs.koin.core)
            api(libs.koin.annotations)

            //Ktor
            api(libs.ktor.core)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.auth)


            //Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            //DataStore
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

room {
    schemaDirectory("$projectDir/schemas")
}

// In the build.gradle.kts file of the :shared module
afterEvaluate {
    tasks.configureEach {
        if (name == "extractAndroidMainAnnotations") {
            mustRunAfter(tasks.named("kspCommonMainKotlinMetadata"))
            mustRunAfter(tasks.named("kspAndroidMain"))
        }
        if (name.startsWith("ksp") && name != "kspCommonMainKotlinMetadata") {
            mustRunAfter(tasks.named("kspCommonMainKotlinMetadata"))
        }
    }
}


dependencies {
    ksp(libs.koin.ksp.compiler)
    ksp(libs.androidx.room.compiler)
}