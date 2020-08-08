# Datum
Android SharedPreferences helper

To use [SharedPreferences api](https://developer.android.com/reference/android/content/SharedPreferences) in a better way.

Features
--------
* **Generic objects**: Different types of SharedPreferences can be used in a generic way. SharedPreferences' supported data types include Integer, Long, Float, Boolean, String & StringSet.
* **Synchronous vs Asynchronous**: Both supported. The most ui-blocking events happen in SharedPreferences' inner file io but some other processes have also been taken care of.
* **Security**: One can use secure datums in order to provide higher levels of data safety. It's possible to implement a custom cipher to protect the saved data.

Usage
-----
### Synchronous
```java
SyncProvider syncProvider = new SyncProvider() {
    @Override
    protected SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(...);
    }
};
StringDatum stringDatum = syncProvider.new StringDatum("key", "default");
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
FloatDatum floatDatum = asyncProvider.new FloatDatum("key", 3.141592f);
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
Secure datums use async datums behind the scenes.
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
Or you may use the default constructor, which is not recommended:
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
StringSetDatum stringSetDatum = secureProvider.new StringSetDatum("key", null);
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
