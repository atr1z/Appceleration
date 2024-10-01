import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.atriz.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.publish)
}

android {
    namespace = "mx.com.atriz.widgets"
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":core"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinator)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime.runtime)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material)
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true
        )
    )
    coordinates("mx.com.atriz", "widgets", libs.versions.atriz.widgets.get())
    pom {
        name.set("Widgets")
        description.set("Widgets easy to use ready for development")
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
