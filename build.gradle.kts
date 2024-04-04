import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val exposedVersion = "0.48.0"

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
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
    implementation("org.jetbrains.compose.material3:material3-desktop:1.6.1")  //material3
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.0")  //协程
    implementation("io.github.succlz123:compose-imageloader-desktop:0.0.2") //imageLoader
    implementation("moe.tlaster:precompose:1.5.11") //PreCompose导航栏
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-crypt:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
//    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")   //sqlite数据库
    implementation("org.slf4j:slf4j-api:2.0.12")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("com.github.tkuenneth:nativeparameterstoreaccess:0.1.2") //读取系统设置，动态响应颜色变化。
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
