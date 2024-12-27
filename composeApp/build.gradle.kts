import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.material3.desktop)

            //paging分页
            implementation(libs.paging.compose.common)

            //协程
            implementation(libs.kotlinx.coroutines.core)

            //Coil
            implementation(libs.coil3.coil.compose)

            //PreCompose导航栏
            implementation(libs.precompose)

            //ViewModel
            implementation(libs.lifecycle.viewmodel.compose)

            //Exposed数据库
            implementation(libs.exposed.core)
            implementation(libs.exposed.crypt)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.kotlin.datetime)
            implementation(libs.org.jetbrains.exposed.exposed.json3)
            implementation(libs.org.jetbrains.exposed.exposed.money3)

            //sqlite数据库
            implementation(libs.org.xerial.sqlite.jdbc)
            implementation(libs.org.slf4j.slf4j.api)
            implementation(libs.log4j.core)

            //读取系统设置，动态响应颜色变化。
            implementation(libs.com.github.tkuenneth.nativeparameterstoreaccess)

            //数据序列化
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

android {
    namespace = "cn.merlin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "cn.merlin"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "cn.merlin.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "cn.merlin"
            packageVersion = "1.0.0"
        }
    }
}
