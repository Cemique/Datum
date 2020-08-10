# Datum
Android SharedPreferences helper

To use [SharedPreferences api](https://developer.android.com/reference/android/content/SharedPreferences) in a better way.

## Features
* **Generic objects**: Different types of SharedPreferences can be used in a generic way. SharedPreferences' supported data types include Integer, Long, Float, Boolean, String & StringSet.
* **Synchronous vs Asynchronous**: Both supported. The most ui-blocking events happen in SharedPreferences' inner file io but some other processes have also been taken care of.
* **Security**: One can use encryption in order to provide higher levels of data safety. It's possible to implement a custom cipher to protect the saved data.

## Download (Gradle)

```gradle
repositories {
  jcenter()
}

dependencies {
  implementation 'com.cemique.datum:datum-lib:1.0'
}
```
## Usage

### Synchronous
```java
SyncProvider syncProvider = new SyncProvider() {
    @Override
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(...);
    }
};
SyncDatum<String> stringDatum = syncProvider.new StringDatum("key", "default");
stringDatum.setValue("new value");
String value = stringDatum.getValue();
stringDatum.clear();
```

### Asynchronous
```java
AsyncProvider asyncProvider = new AsyncProvider() {
    @Override
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(...); //For better performance try to call this function only here. More info: https://stackoverflow.com/a/4371883/6094503
    }
};
AsyncDatum<Float> floatDatum = asyncProvider.new FloatDatum("key", 3.141592f);
floatDatum.setValue(8203f, ()->{
    //value is saved
});
floatDatum.getValue(value->{
    //value is restored
});
floatDatum.clear(()->{
    //datum is removed
});
```

### Secure
Secure datums use async datums behind the scenes. To save datums they are converted to string and then encrypted using cipher functions, the same in reverse direction occurs when being restored.
```java
DeterministicCipher keyCipher = new DeterministicCipher(){...};
Cipher valueCipher = new Cipher(){...};
SecureProvider secureProvider = new SecureProvider(keyCipher, valueCipher) {
    @Override
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(...);
    }
};
```
You can also make use of DefaultCipher which transforms with 'AES/CBC/PKCS5Padding':
```java
DeterministicCipher defaultCipher = new DefaultCipher.Builder().setPassword(...).setIv(...).build();
...
```
Or you may use the default constructor of SecureProvider, which is not recommended:
```java
SecureProvider secureProvider = new SecureProvider() {
    @Override
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(...);
    }
};
```
Then use it:
```java
SecureDatum<Set<String>> stringSetDatum = secureProvider.new StringSetDatum("key", null);
Set<String> newStringSet = new HashSet<>();
newStringSet.add("new value");
stringSetDatum.setValue(newStringSet, () -> {
    //value is saved
});
stringSetDatum.getValue(value -> {
    //value is restored
});
stringSetDatum.clear(()->{
    //datum is removed
});
```
## Contribution
All kinds of contributions are welcomed.

## License
Copyright 2020 Cemique

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
