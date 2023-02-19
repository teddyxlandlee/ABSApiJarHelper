plugins {
    java
    `maven-publish`
}

group = "xland.gradle"
version = ext["app_version"]!!

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    //implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.ow2.asm:asm:9.4")
    compileOnly("org.jetbrains:annotations:24.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

publishing {
    publications {
        create("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
