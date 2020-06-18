# BaseWithMe
Base Application
## Getting Started
### Configure build.gradle (Project)
* Add these lines:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
and:
```
dependencies {
  def nav_version = "2.1.0"
  classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
}
```
* If you want define some variable for implement, put something like this at the end of this file:
```
ext{
    def ktx_version = "1.1.0"
    core_ktx = "androidx.core:core-ktx:$ktx_version"

    def appcompat_version = "1.1.0"
    appcompat = "androidx.appcompat:appcompat:$appcompat_version"

    kotlin_core = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    constraint_layout = "androidx.constraintlayout:constraintlayout:1.1.3"

    def bumptech_glide = '4.10.0'
    glide = "com.github.bumptech.glide:glide:$bumptech_glide"
    glide_http = "com.github.bumptech.glide:okhttp3-integration:$bumptech_glide"
    design = 'com.android.support:design:29.0.2'

    def lifecycle_version = "2.1.0"
    lifecycle_ext = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"

    sdp = 'com.intuit.sdp:sdp-android:1.0.6'
    ssp = 'com.intuit.ssp:ssp-android:1.0.6'

    material = 'com.google.android.material:material:1.2.0-alpha04'

    def nav_version = "2.1.0"
    nav_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:$nav_version"
    nav_ui_ktx = "androidx.navigation:navigation-ui-ktx:$nav_version"

    def retrofit_version = "2.6.2"
    retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    gson = "com.squareup.retrofit2:converter-gson:$retrofit_version"

    def coroutine_version = "1.3.3"
    coroutine_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version"
    coroutine_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version"

    def room_version = "2.1.0"
    room_runtime = "androidx.room:room-runtime:$room_version"
    kapt_room = "androidx.room:room-compiler:$room_version"
    room_ktx = "androidx.room:room-ktx:$room_version"

    ultra_ptr = "in.srain.cube:ultra-ptr:1.0.11"

    loading_spinkit = 'com.github.ybq:Android-SpinKit:1.4.0'

    def npicker_version = "2.4.8"
    number_picker = "com.shawnlin:number-picker:$npicker_version"
    waveheaderalpha = 'com.scwang.wave:MultiWaveHeader:1.0.0-alpha-1'
    //androidx
    waveheaderandx = 'com.scwang.wave:MultiWaveHeader:1.0.0-andx-1'

    def okhttp_version = '3.9.0'
    okhttp_logging = "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    motionlayout = 'androidx.constraintlayout:constraintlayout:2.0.0-beta1'

}
```
* Then in your build gradle (module), you just need add an example line like this:
```
dependencies {
  implementation rootProject.ext.appcompat
}
```

### Configure build gradle (Module):
* Import module base:
```
dependencies {
  implementation 'com.github.ngtien137:BaseWithMe:TAG'
}
```
* You can get version of this module [here](https://jitpack.io/#ngtien137/BaseWithMe)
* Apply plugin for data binding and navigation component:
```
apply plugin: 'kotlin-kapt'
apply plugin: "androidx.navigation.safeargs"
```
* Add these tags into android tag:
```
dataBinding {
    enabled = true
}
compileOptions {
    targetCompatibility JavaVersion.VERSION_1_8
}

kotlinOptions {
    jvmTarget = '1.8'
}
```

