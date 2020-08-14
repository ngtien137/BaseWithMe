# BaseWithMe
Base Application
## Getting Started
### Configure build.gradle (Project)
* Add these lines:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
and:
```gradle
dependencies {
  def nav_version = "2.1.0"
  classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
}
```

### Configure build gradle (Module):
* Import module base:
```gradle
dependencies {
  implementation 'com.github.ngtien137:BaseWithMe:TAG'
}
```
* You can get version of this module [here](https://jitpack.io/#ngtien137/BaseWithMe)
* Apply plugin for data binding and navigation component:
```gradle
apply plugin: 'kotlin-kapt'
apply plugin: "androidx.navigation.safeargs"
```
* Add these tags into android tag:
```gradle
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
[BindingUtils](https://github.com/ngtien137/BaseWithMe/blob/master/app/src/main/java/com/lhd/view/basewithme/utils/BindingUtils.kt)

### App Resources Extension & SharedPreferences support
* Some extension function for resources such as string, drawable, dimension,...
* If you want to use this extension, you need use my BaseApplication:

```kotlin
class App : BaseApplication()
```
* SharedPreferences support:
```kotlin
@BaseSharedPreferences(name = "BasePref") //configure name of Sharedpreferences
class App : BaseApplication()
```
<br> After use this annotation for your application class, you can use extension function save data in SharedPreferences:
```kotlin

//Example Long::class.java.getPrefData() or LongClass.getPrefData()
fun <T> Class<T>.getPrefData(key: String): T = basePreference!!.get(key, this)
fun <T> Class<T>.getPrefData(key: String, defaultValue: T): T =
    basePreference!!.get(key, defaultValue, this)

fun <T> putPrefData(key: String, value: T) = basePreference!!.put(key, value)

//Example
//Get Long data:
LongClass.getPrefData(STRING_KEY_DATA,defaultValue)
//Put data, you can call this everywhere
putPrefData(STRING_KEY_DATA, value) //data type which is saved in sharedpreferences is depends on value's data type
```
### Recycler View Adapter Support (Open Beta)
#### Preview
![alt text](https://github.com/ngtien137/BaseWithMe/blob/master/git_resources/super_adapter.gif) 
#### Feature
- Support select item with a Stack<T> list: Unselectable, Multiple Selection or Single selection
- Drag vertical to move position
- Swipe Menu
#### Work with SuperAdapter
- To use this supporting, you can use my base adapter call SuperAdapter. It uses some annotation to configure it's behavior
- First create a adapter extends SuperAdapter. Example, I use a object call Account:
```kotlin
@SuperActionMenu
@SuperDragVertical
@SuperSelect(viewHandleSelectId = R.id.imgAccount,handleByLongClick = false,enableUnSelect = true,enableMultiSelect = false)
class AccountAdapter : SuperAdapter<Account>(R.layout.item_account) {

}
```
#### Annotation
* SuperSelect 
```kotlin
annotation class SuperSelect(
    @IdRes val viewHandleSelectId: Int = -1, //This is id of view which handle select event
    val handleByLongClick: Boolean = false,  //Set select by longclick or normal onclick
    val enableUnSelect: Boolean = true,      //set able to uncheck
    val enableMultiSelect: Boolean = false   //set able to multiple selection
)
```

* SuperDragVertical : Add this annotation for enable drag function
```kotlin
annotation class SuperDragVertical
```

* SuperActionMenu : Add this annotation for enable swipe menu
```kotlin
annotation class SuperActionMenu(
    @IdRes val menuId: Int = -1,
    @IdRes val menuMainContent: Int = -1
)
```
<br> This annotation has two properties: menuId and menuMainContent, when you use this annotation, you must declare two as below:
```xml
<androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="@dimen/item_account_height"
        android:layout_margin="@dimen/_4sdp"
        android:background="?selectableItemBackground"
        android:onClick="@{()->listener.onAccountClick(item)}">

        <LinearLayout
            android:id="@id/layout_item_menu_action"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            app:layout_constraintEnd_toEndOf="parent">
            <!-- layout menu write here  -->
       </LinearLayout>
  
  
      <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/layout_item_main_content"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/white"
            app:layout_constraintEnd_toStartOf="@id/layout_item_menu_action">
        
            <!-- Main content for item write here  -->
        
      </androidx.constraintlayout.widget.ConstraintLayout>
  </androidx.constraintlayout.widget.ConstraintLayout>
```
<br>You must use constraint layout is the big parent and inside you must have two view group with two id: @id/layout_item_menu_action and @id/layout_item_main_content. Those are ids of menu and mainContent you declare in annotation SuperActionMenu. If you don't declare any id for it. You must set @id/layout_item_menu_action and @id/layout_item_main_content for your menu and main content layout.

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

