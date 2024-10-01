import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.atriz.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.publish)
}

android {
    namespace = "mx.com.atriz.core"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    // implementation(libs.androidx.activity.compose)
    // implementation(platform(libs.androidx.compose.bom))
    // implementation(libs.androidx.compose.runtime.runtime)
    implementation(libs.androidx.navigation)
    implementation(libs.google.material)
    implementation(libs.google.location)
    implementation(libs.gson)
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true
        )
    )
    coordinates("mx.com.atriz", "core", libs.versions.atriz.core.get())
    pom {
        name.set("Core")
        description.set("Tools for faster development")
        inceptionYear.set("2024")
        url.set("https://github.com/atr1z/Appceleration/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("atr1z")
                name.set("Atriz.")
                url.set("https://github.com/atri1z/")
            }
        }
        scm {
            url.set("https://github.com/atr1z/Appceleration/")
            connection.set("scm:git:git://github.com/atr1z/Appceleration.git")
            developerConnection.set("scm:git:ssh://git@github.com/atr1z/Appceleration.git")
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}
