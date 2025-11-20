# Quick Reference: Swift ↔ Kotlin Interoperability

## Summary of All Approaches

### 1. **Calling Kotlin from Swift** ✅ (Easy)
Your iOS app imports the shared framework and calls Kotlin directly.

```swift
import Shared

let user = User(id: 1, name: "John")
let viewModel = SharedViewModel()
```

### 2. **Executing Swift Code from Kotlin** ✅ (Interface Injection)
Define interfaces in Kotlin, implement in Swift, pass to Kotlin.

```kotlin
// Kotlin (commonMain)
interface PlatformLogger {
    fun log(message: String)
}

// Swift (iOS App)
class SwiftLogger: PlatformLogger {
    func log(message: String) {
        print("📱 \(message)")
    }
}

// Usage
let logger = SwiftLogger()
let viewModel = SharedViewModel(logger: logger)
```

### 3. **Using iOS Native APIs in Kotlin** ✅ (expect/actual)
Write platform-specific Kotlin code using iOS frameworks.

```kotlin
// commonMain
expect class FileManager() {
    fun saveFile(path: String, content: String): Boolean
}

// iosMain
import platform.Foundation.*

actual class FileManager {
    actual fun saveFile(path: String, content: String): Boolean {
        // Use iOS NSFileManager API
        return (content as NSString).writeToFile(path, ...)
    }
}
```

### 4. **Calling Swift Code from iosMain Kotlin** ✅ (This Guide!)
Create Swift helpers with @objc, call from Kotlin.

```swift
// Swift (in iOS app)
@objc public class SwiftKeychainManager: NSObject {
    @objc public func save(key: String, value: String) -> Bool {
        // Keychain implementation
        return true
    }
}
```

```kotlin
// Kotlin (iosMain)
class KeychainService {
    private val swift = SwiftKeychainManager()
    
    fun saveToken(token: String): Boolean {
        return swift.saveWithKey("token", value = token)
    }
}
```

## When to Use Each Approach

| Scenario | Best Approach | Example |
|----------|---------------|---------|
| iOS app needs shared business logic | Call Kotlin from Swift | ViewModels, repositories |
| Kotlin needs UI/platform features | Interface injection | Camera, permissions |
| Kotlin needs iOS APIs | expect/actual | UserDefaults, file I/O |
| Kotlin needs complex Swift features | Swift @objc helpers | Keychain, biometrics |

## Files in This Project

1. **`IOS_INTEROP_GUIDE.md`** - Calling Kotlin from Swift + Flow handling
2. **`USING_IOS_CODE_IN_KOTLIN.md`** - Using iOS APIs via expect/actual
3. **`SWIFT_TO_KOTLIN_GUIDE.md`** - Calling Swift code from Kotlin (this scenario)
4. **`SwiftHelpers.swift`** - Example Swift code with @objc
5. **`SwiftInterop.kt`** - Example Kotlin code using Swift helpers

## Quick Start

### To call Swift from Kotlin (iosMain):

1. **Create Swift class with @objc:**
   ```swift
   @objc public class MyHelper: NSObject {
       @objc public func doSomething() -> String {
           return "Hello from Swift!"
       }
   }
   ```

2. **Add to iOS app target** (not shared framework)

3. **Use in Kotlin (iosMain):**
   ```kotlin
   class MyService {
       private val helper = MyHelper()
       
       fun callSwift(): String {
           return helper.doSomething()
       }
   }
   ```

## Common Patterns

### Pattern 1: Simple Helper
```swift
@objc class Helper: NSObject {
    @objc func process() -> String { "result" }
}
```

### Pattern 2: Callback/Closure
```swift
@objc class Async: NSObject {
    @objc func fetch(completion: @escaping (String) -> Void) {
        completion("data")
    }
}
```

### Pattern 3: Delegate
```swift
@objc protocol MyDelegate {
    func onEvent(data: String)
}

@objc class Manager: NSObject {
    @objc weak var delegate: MyDelegate?
}
```

## All Together Now!

```
┌─────────────────────────────────────────────┐
│          iOS App (Swift/SwiftUI)            │
│                                             │
│  1. Calls Kotlin directly                   │
│  2. Implements Kotlin interfaces            │
│  3. Provides @objc Swift helpers            │
└──────────────┬──────────────────────────────┘
               │ imports
               ↓
┌─────────────────────────────────────────────┐
│       Shared Framework (Kotlin)             │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │  commonMain                         │   │
│  │  - Business logic                   │   │
│  │  - expect declarations              │   │
│  │  - Interface definitions            │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │  iosMain                            │   │
│  │  - actual implementations           │   │
│  │  - Uses iOS frameworks              │   │
│  │  - Calls Swift @objc classes        │   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │  androidMain                        │   │
│  │  - actual implementations           │   │
│  │  - Uses Android APIs                │   │
│  └─────────────────────────────────────┘   │
└─────────────────────────────────────────────┘
```

## Tips & Tricks

✅ **Do:**
- Use @objc for all Swift classes/methods you want to expose
- Inherit from NSObject
- Use simple types (String, Int, Bool)
- Comment out unused imports to avoid build errors

❌ **Don't:**
- Use Swift generics, structs, or tuples
- Use pure Swift classes without @objc
- Put Swift files in the shared module (put in iOS app)

## Build Status

✅ Project builds successfully
✅ All examples compile
✅ Ready to use!

Run `./gradlew build` to verify.
