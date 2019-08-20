# foxcore
Android base classes for quick starting projects


MVP pattern by moxy https://github.com/moxy-community/Moxy


Rx pattern by RxJava2 https://github.com/ReactiveX/RxJava


Network by Retrofit2 https://github.com/square/retrofit


# Integration
Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

 Add the dependency
 
 ```
dependencies {
  implementation  "com.github.a-f0x:foxcore:Tag"
  implementation  "io.reactivex.rxjava2:rxjava:$rxJavaVersion"
  implementation  "io.reactivex.rxjava2:rxandroid:$rxAndroidVersion"
  implementation  "androidx.appcompat:appcompat:$appCompatXVersion"
  implementation  "com.google.android.material:material:$materialXVersion"
  implementation  "androidx.core:core-ktx:$ktxVersion"
  implementation  "com.shawnlin:number-picker:2.4.4"
  implementation  "com.redmadrobot:inputmask:3.4.4"
  implementation  "com.github.moxy-community:moxy-app-compat:$moxyVersion"
  kapt            "com.github.moxy-community:moxy-compiler:$moxyVersion"
  implementation  "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines"
  implementation  "com.squareup.retrofit2:retrofit:$retrofitVersion"
  implementation  "com.squareup.retrofit2:converter-gson:$retrofitVersion"
  
}

 ```
