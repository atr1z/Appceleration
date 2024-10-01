import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.VersionCatalog

plugins {
    id("version-catalog")
    alias(libs.plugins.publish)
}

catalog {
    versionCatalog {
        from(files("../gradle/libs.versions.toml"))
    }
}

mavenPublishing {
    configure(VersionCatalog())
    coordinates("mx.com.atriz", "catalog", libs.versions.atriz.catalogs.get())
    pom {
        name.set("Catalog")
        description.set("Catalog ready to go")
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
                id.set("AtrizDeveloper")
                name.set("Jair M.")
                url.set("https://github.com/atr1z/")
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
