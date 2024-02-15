import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import kotlinx.benchmark.gradle.NativeBenchmarkTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform") version "1.9.22"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

fun KotlinNativeCompilation.configureCInterop() {
    cinterops {
        val nativeCalls by creating {
            defFile(project.rootDir.resolve("src/appleMain/kotlin/nativeCalls.def"))
            packageName("org.example.native")
        }
    }
}

kotlin {
    jvmToolchain(21)

    macosArm64 {
        // Neither of these works:
        // compilations.named("main") {configureCInterop() }
        // compilations.named("macosArm64Benchmark") {configureCInterop() }
    }
    macosX64 {
        // compilations.named("main") {configureCInterop() }
        // compilations.named("macosX64Benchmark") { configureCInterop() }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.10")
            }
        }
    }
}

benchmark {
    targets {
        register("macosArm64") {
            this as NativeBenchmarkTarget
            compilation.configureCInterop()
        }
        register("macosX64") {
            this as NativeBenchmarkTarget
            compilation.configureCInterop()
        }
    }
}
