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
android {
  ...
  dataBinding {
      enabled = true
  }
  compileOptions {
      targetCompatibility JavaVersion.VERSION_1_8
  }

  kotlinOptions {
      jvmTarget = '1.8'
  }
}
```

### Binding Supporter
* This base have some extension for binding, add this file to main module for showing suggestion in xml layout:
[BindingUtils](https://github.com/ngtien137/BaseWithMe/blob/master/baseme/src/main/java/com/base/baselibrary/utils/BindingUtils.kt)

### App Resources Extension
* Some extension function for resources such as string, drawable, dimension,...
* If you want to use this extension, you need call function initBaseApplication in onCreate() of your class Application:

```kotlin
class App : Application() {
  override fun onCreate() {
      super.onCreate()
      initBaseApplication()
  }
}
```

### View Model Support
* If you've used view model in your project, you must probably know that initializing a viewmodel with some parameters in constructor is very complexible. So, this base has some extension for initializing viewmodel easier.
* Example:
<br>First, you have a class call DataRepository where you contain, get and process data with retrofit, database,... and your view model has a instance of DataRepository in its constructor:
```kotlin
class DataRepository {
  val data = MutableLiveData(ArrayList<Account>())
  
  fun getDataFromServer(){
    //TODO something here
  }
  
  fun getDataFromLocal(){
    //TODO something here
  }
}

class HomeViewModel constructor(private val dataRepository: DataRepository) : ViewModel(){

}
```
<br>In a simple project, you need create a singleton of DataRepository, then you need custom a viewmodel factory to pass a parameter to your view model and kotlin supports you with inline function by viewmodels to create your viewmodels
```kotlin
private val viewModel by viewModels<HomeViewModel> {
      HomeViewModelFactory(DataRepository.getSingleton())
}
```
<br>Then think about it, if you have two or more viewmodels, you need create more factory and for each repository, you need write a static function to create singleton for it. Costing too much time!
<br>So, this base help you create singleton and viewmodel factory easier. If you need a view model factory for multiple parameters viewmodel, you can use my MultiParamsFactory class:
```kotlin
private val homeViewModel:HomeViewModel by viewModels {
    MultiParamsFactory(1,2,3,4,DataRepository.getSingleton())
}
```
<br>If you don't need any parameters, you just need call MultiParamsFactory() with no parameters
<br>For easy creating singleton, you can do as below:
```kotlin
MultiParamsFactory(1,2,3,4,DataRepository::class.java.getSingleton())
```
<br>If you think all above it still complexible, you can use my extension with annotation @Auto:
```kotlin
class HomeViewModel @Auto constructor(private val dataRepository: DataRepository) : ViewModel(){

}

//Then in your fragment or activity, you can create viewModel with:
private val viewModel: HomeViewModel by autoViewModels()
```
<br>With this annotation, it will auto create a factory with singleton instance of repository (if exists a singleton of repository which was created with base, it will get it, not create new)
<br>Note: If you use annotation @Auto to create viewModel, make sure your parameters has empty constructors (or initialize constructors with annotation @Auto - Future update)

