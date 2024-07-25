import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val exposedVersion = "0.48.0"

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.20"

}

group = "cn.merlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    //material3
    implementation("org.jetbrains.compose.material3:material3-desktop:1.6.11")

    //协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")

    //Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")

    //PreCompose导航栏
    implementation("moe.tlaster:precompose:1.5.11")

    //Exposed数据库
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")

    //sqlite数据库
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    //读取系统设置，动态响应颜色变化。
    implementation("com.github.tkuenneth:nativeparameterstoreaccess:0.1.2")

    //数据序列化
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,TargetFormat.Exe)
            packageName = "SimpleSender"
            packageVersion = "1.0.0"
            modules("java.compiler", "java.instrument" , "java.sql", "jdk.unsupported", "java.naming")
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}
