# Using iOS Code in Kotlin - Complete Guide

## Overview

To use iOS-specific code (like UIKit, Foundation, etc.) from Kotlin, you use the **expect/actual pattern**. This allows you to:
- Write platform-independent code in `commonMain`
- Provide iOS-specific implementations in `iosMain` using native iOS APIs
- Provide Android implementations in `androidMain`

## The expect/actual Pattern

### Step 1: Define `expect` in commonMain

```kotlin
// In: shared/src/commonMain/kotlin/...

expect class PlatformHelper() {
    fun getCurrentPlatformName(): String
    fun getDeviceModel(): String
}
```

### Step 2: Provide `actual` implementation in iosMain

```kotlin
// In: shared/src/iosMain/kotlin/...

import platform.UIKit.UIDevice

actual class PlatformHelper {
    actual fun getCurrentPlatformName(): String = "iOS"
    
    actual fun getDeviceModel(): String {
        return UIDevice.currentDevice.model  // Calling iOS API!
    }
}
```

### Step 3: Use in common code

```kotlin
// In: shared/src/commonMain/kotlin/...

class MyViewModel {
    private val helper = PlatformHelper()
    
    fun showDeviceInfo(): String {
        return "Running on ${helper.getCurrentPlatformName()}, " +
               "Device: ${helper.getDeviceModel()}"
    }
}
```

## Available iOS Frameworks in Kotlin/Native

You can access ALL iOS frameworks from Kotlin:

```kotlin
import platform.Foundation.*     // NSString, NSDate, NSFileManager, etc.
import platform.UIKit.*          // UIDevice, UIApplication, UIViewController, etc.
import platform.CoreData.*       // Core Data entities
import platform.CoreLocation.*   // Location services
import platform.Security.*       // Keychain
import platform.UserNotifications.* // Push notifications
import platform.AVFoundation.*   // Audio/Video
import platform.MapKit.*         // Maps
```

## Real-World Examples

### Example 1: File Operations with Foundation

```kotlin
actual class FileManager {
    private val fileManager = NSFileManager.defaultManager
    
    actual fun getDocumentsDirectory(): String {
        val urls = fileManager.URLsForDirectory(
            directory = NSDocumentDirectory,
            inDomains = NSUserDomainMask
        )
        return (urls.firstOrNull() as? NSURL)?.path ?: ""
    }
    
    actual fun writeFile(path: String, content: String): Boolean {
        return try {
            (content as NSString).writeToFile(
                path = path,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
```

### Example 2: Keychain (Secure Storage)

```kotlin
import platform.Security.*
import kotlinx.cinterop.*

actual class SecureStorage {
    actual fun saveSecure(key: String, value: String): Boolean {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrAccount to key,
            kSecValueData to (value as NSString).dataUsingEncoding(NSUTF8StringEncoding),
            kSecAttrAccessible to kSecAttrAccessibleWhenUnlocked
        )
        
        val status = SecItemAdd(query as CFDictionaryRef, null)
        return status == errSecSuccess
    }
    
    actual fun getSecure(key: String): String? {
        val query = mapOf<Any?, Any?>(
            kSecClass to kSecClassGenericPassword,
            kSecAttrAccount to key,
            kSecReturnData to kCFBooleanTrue,
            kSecMatchLimit to kSecMatchLimitOne
        )
        
        memScoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)
            
            if (status == errSecSuccess) {
                val data = result.value as? NSData
                return data?.let {
                    NSString.create(it, NSUTF8StringEncoding) as? String
                }
            }
        }
        return null
    }
}
```

### Example 3: UserDefaults

```kotlin
import platform.Foundation.NSUserDefaults

class UserDefaultsStorage {
    private val defaults = NSUserDefaults.standardUserDefaults
    
    fun saveString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
        defaults.synchronize()
    }
    
    fun getString(key: String): String? {
        return defaults.stringForKey(key)
    }
    
    fun saveInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), forKey = key)
    }
    
    fun getInt(key: String): Int {
        return defaults.integerForKey(key).toInt()
    }
}
```

### Example 4: Device Information

```kotlin
import platform.UIKit.*
import platform.Foundation.*

class DeviceInfo {
    fun getDeviceName(): String = UIDevice.currentDevice.name
    fun getSystemName(): String = UIDevice.currentDevice.systemName
    fun getSystemVersion(): String = UIDevice.currentDevice.systemVersion
    fun getModel(): String = UIDevice.currentDevice.model
    fun getBatteryLevel(): Float = UIDevice.currentDevice.batteryLevel
    
    fun getAppVersion(): String {
        val bundle = NSBundle.mainBundle
        return bundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: ""
    }
    
    fun getBundleId(): String {
        return NSBundle.mainBundle.bundleIdentifier ?: ""
    }
}
```

### Example 5: Opening URLs

```kotlin
import platform.UIKit.*
import platform.Foundation.*

class UrlOpener {
    fun openUrl(urlString: String) {
        val url = NSURL.URLWithString(urlString)
        url?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
    
    fun canOpenUrl(urlString: String): Boolean {
        val url = NSURL.URLWithString(urlString) ?: return false
        return UIApplication.sharedApplication.canOpenURL(url)
    }
}
```

### Example 6: Vibration/Haptics

```kotlin
import platform.AudioToolbox.*

class HapticFeedback {
    fun vibrate() {
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
    }
    
    fun playSystemSound(soundId: UInt) {
        AudioServicesPlaySystemSound(soundId)
    }
}
```

### Example 7: Locale and Language

```kotlin
import platform.Foundation.*

class LocaleHelper {
    fun getCurrentLanguage(): String {
        return NSLocale.currentLocale.languageCode
    }
    
    fun getCurrentCountry(): String {
        return NSLocale.currentLocale.countryCode ?: ""
    }
    
    fun getPreferredLanguages(): List<String> {
        return NSLocale.preferredLanguages.map { it.toString() }
    }
    
    fun getTimeZone(): String {
        return NSTimeZone.localTimeZone.name
    }
}
```

## When to Use Swift vs Kotlin/Native

### Use Kotlin/Native (expect/actual) when:
✅ Simple platform APIs (Foundation, UIKit basics)
✅ File operations, UserDefaults, Keychain
✅ Device information
✅ Business logic that needs platform-specific implementations

### Use Swift (interface implementation) when:
✅ Complex UI operations (SwiftUI, UIViewController)
✅ iOS 16+ specific features not yet in Kotlin/Native
✅ Third-party iOS SDKs not available in Kotlin
✅ Camera, Photo Library, permissions handling
✅ When Swift code is already written

## Complete Usage Pattern

```kotlin
// 1. Define in commonMain
expect class LocationService {
    fun getCurrentLocation(): Location?
}

data class Location(val lat: Double, val lon: Double)

// 2. Implement in iosMain using CoreLocation
import platform.CoreLocation.*
import kotlinx.cinterop.*

actual class LocationService {
    private val locationManager = CLLocationManager()
    
    actual fun getCurrentLocation(): Location? {
        val location = locationManager.location
        return location?.let {
            Location(
                lat = it.coordinate.latitude,
                lon = it.coordinate.longitude
            )
        }
    }
}

// 3. Use in common code
class MapViewModel {
    private val locationService = LocationService()
    
    fun showCurrentLocation() {
        val location = locationService.getCurrentLocation()
        println("Current location: ${location?.lat}, ${location?.lon}")
    }
}
```

## Important Notes

1. **Memory Management**: Kotlin/Native uses ARC (Automatic Reference Counting) on iOS, so memory is managed automatically

2. **Threading**: iOS APIs often require main thread. Use:
   ```kotlin
   dispatch_async(dispatch_get_main_queue()) {
       // UI operations
   }
   ```

3. **Nullability**: Objective-C nullability annotations are respected in Kotlin

4. **Collections**: Swift/Objective-C collections map to Kotlin:
   - `NSArray` → `List`
   - `NSDictionary` → `Map`
   - `NSSet` → `Set`

5. **Error Handling**: Objective-C errors become exceptions in Kotlin

## Summary

| Approach | When to Use | Example |
|----------|-------------|---------|
| **expect/actual** | Platform-specific Kotlin code using iOS frameworks | File operations, UserDefaults, Keychain |
| **Interface injection** | Complex Swift code, UI, third-party SDKs | Camera, permissions, SwiftUI integration |

Both approaches can be combined in the same project!
