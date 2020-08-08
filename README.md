# Datum
Android SharedPreferences helper

To use [SharedPreferences api](https://developer.android.com/reference/android/content/SharedPreferences) in a better way.

Features
--------
* **Generic objects**: Different types of SharedPreferences can be used in a generic way. SharedPreferences' supported data types include Integer, Float, Double, Boolean, String & StringSet.
* **Synchronous vs Asynchronous**: Both supported. The most ui-blocking events happen in SharedPreferences' inner file io but some other processes have also been taken care of.
* **Security**: One can use secure datums in order to provide higher levels of data safety. It's possible to implement a custom cipher to protect the saved data.
