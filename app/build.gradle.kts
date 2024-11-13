plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   id("com.google.devtools.ksp")
   id("com.google.dagger.hilt.android")
   id("androidx.navigation.safeargs.kotlin")
   alias(libs.plugins.google.gms.google.services)
}

android {
   namespace = "com.dermaseer.dermaseer"
   compileSdk = 35

   defaultConfig {
      applicationId = "com.dermaseer.dermaseer"
      minSdk = 24
      //noinspection EditedTargetSdkVersion
      targetSdk = 35
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }
   kotlinOptions {
      jvmTarget = "1.8"
   }
   buildFeatures {
      viewBinding = true
      buildConfig = true
   }
}

dependencies {

   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.appcompat)
   implementation(libs.material)
   implementation(libs.androidx.activity)
   implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
   implementation(libs.androidx.navigation.ui.ktx)
   testImplementation(libs.junit)
   androidTestImplementation(libs.androidx.junit)
   androidTestImplementation(libs.androidx.espresso.core)

   //Dagger Hilt
   implementation(libs.hilt.android)
   ksp(libs.dagger.compiler) // Dagger compiler
   ksp(libs.hilt.compiler)   // Hilt compiler

   // Retrofit
   implementation(libs.retrofit)
   implementation(libs.converter.gson)

   // OkHttp and Chucker
   implementation(libs.okhttp)
   debugImplementation(libs.library)
   releaseImplementation(libs.library.no.op)

   // Lifecycle
   implementation(libs.androidx.lifecycle.viewmodel.ktx)
   implementation(libs.androidx.lifecycle.livedata.ktx)
   implementation(libs.androidx.lifecycle.runtime.ktx)

   // Coroutines
   implementation (libs.kotlinx.coroutines.android)

   // Glide
   implementation(libs.glide)
   annotationProcessor(libs.compiler)

   // CameraX
   implementation(libs.androidx.camera.camera2)
   implementation(libs.androidx.camera.lifecycle)
   implementation(libs.androidx.camera.view)

   // Jetpack Navigation
   implementation(libs.androidx.navigation.fragment)
   implementation(libs.androidx.navigation.ui)

   // DataStore
   implementation(libs.androidx.datastore.preferences)

   // Firebase Auth
   implementation(libs.firebase.auth)
}