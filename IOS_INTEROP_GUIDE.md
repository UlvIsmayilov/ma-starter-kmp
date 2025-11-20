# Kotlin Multiplatform iOS Interoperability Guide

This guide explains how to call Kotlin code from Swift/iOS and vice versa.

## 📱 Part 1: Calling Kotlin Code from Swift

### Basic Function Calls

```swift
import Shared

// Create Kotlin objects
let user = User(id: 1, name: "John")
print(user.name) // Access properties

// Call Kotlin functions
let flowExample = FlowExample()
```

### Calling Suspend Functions

Kotlin suspend functions are exposed to Swift as async functions (requires iOS 13+):

```swift
Task {
    do {
        let users = try await flowExample.getUserUpdatesList()
        print("Got \(users.count) users")
    } catch {
        print("Error: \(error)")
    }
}
```

### Working with Kotlin Flow in iOS

**Problem**: Kotlin Flow is NOT directly compatible with Swift.

**Solutions**:

#### Solution 1: Use Callbacks (Recommended for simplicity)
```kotlin
// In Kotlin
fun observeUsers(callback: (User) -> Unit) {
    // Implementation
}
```

```swift
// In Swift
flowExample.observeUsers { user in
    print("Received: \(user.name)")
}
```

#### Solution 2: Use KMP-NativeCoroutines Library

Add to your `build.gradle.kts`:
```kotlin
plugins {
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-31"
}
```

Then in Kotlin:
```kotlin
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

@NativeCoroutines
fun getUserFlow(): Flow<User> = flow { /* ... */ }
```

In Swift, this generates:
```swift
// Automatically generated
let handle = flowExample.createGetUserFlowNative { user, error in
    if let user = user {
        print("User: \(user.name)")
    }
}

// Cancel when done
handle.cancel()
```

#### Solution 3: Manual Flow Wrapper (Already provided in iosMain)

```swift
let viewModel = IosFriendlyViewModel()

let wrapper = viewModel.observeUserUpdates(
    onEach: { user in
        print("User: \(user.name)")
    },
    onComplete: {
        print("Stream completed")
    },
    onError: { error in
        print("Error: \(error)")
    }
)

// Later, when you want to stop observing:
wrapper.unsubscribe()
```

## 🔄 Part 2: Executing Swift Code from Kotlin

Use the **Dependency Injection pattern** by defining interfaces in Kotlin and implementing them in Swift.

### Step 1: Define Interface in Kotlin (commonMain)

```kotlin
// Already created in PlatformInterface.kt
interface PlatformLogger {
    fun log(message: String)
    fun logError(message: String, error: String)
}

interface PlatformStorage {
    fun saveString(key: String, value: String)
    fun getString(key: String): String?
}
```

### Step 2: Implement in Swift

```swift
class SwiftLogger: PlatformLogger {
    func log(message: String) {
        print("📱 iOS: \(message)")
        // Can use OSLog, analytics, etc.
    }
    
    func logError(message: String, error: String) {
        print("❌ iOS Error: \(message)")
        // Can use Crashlytics, Sentry, etc.
    }
}

class SwiftStorage: PlatformStorage {
    private let defaults = UserDefaults.standard
    
    func saveString(key: String, value: String) {
        defaults.set(value, forKey: key)
    }
    
    func getString(key: String) -> String? {
        defaults.string(forKey: key)
    }
}
```

### Step 3: Pass Swift Implementation to Kotlin

```swift
// Create Swift implementations
let logger = SwiftLogger()
let storage = SwiftStorage()

// Pass to Kotlin code
let viewModel = SharedViewModel(logger: logger, storage: storage)

// Now when Kotlin code runs, it executes your Swift implementation!
viewModel.saveUserData(userId: "123", userName: "Jane")
```

## 🎯 Complete SwiftUI Integration Example

```swift
import SwiftUI
import Shared

class AppViewModel: ObservableObject {
    @Published var users: [User] = []
    @Published var isLoading = false
    
    private let iosViewModel = IosFriendlyViewModel()
    private var flowWrapper: FlowWrapper<User>?
    
    func startObserving() {
        isLoading = true
        
        flowWrapper = iosViewModel.observeUserUpdates(
            onEach: { [weak self] user in
                DispatchQueue.main.async {
                    self?.users.append(user)
                }
            },
            onComplete: { [weak self] in
                DispatchQueue.main.async {
                    self?.isLoading = false
                }
            },
            onError: { error in
                print("Error: \(error)")
            }
        )
    }
    
    func stopObserving() {
        flowWrapper?.unsubscribe()
        flowWrapper = nil
    }
}

struct ContentView: View {
    @StateObject private var viewModel = AppViewModel()
    
    var body: some View {
        NavigationView {
            List(viewModel.users, id: \.id) { user in
                Text(user.name)
            }
            .navigationTitle("Users")
            .onAppear {
                viewModel.startObserving()
            }
            .onDisappear {
                viewModel.stopObserving()
            }
        }
    }
}
```

## 🔧 Expect/Actual Pattern

For platform-specific implementations that Kotlin controls:

### In commonMain:
```kotlin
expect class PlatformHelper() {
    fun getCurrentPlatformName(): String
}
```

### In iosMain:
```kotlin
actual class PlatformHelper {
    actual fun getCurrentPlatformName(): String = "iOS"
}
```

### In androidMain:
```kotlin
actual class PlatformHelper {
    actual fun getCurrentPlatformName(): String = "Android"
}
```

## 📋 Best Practices

1. **Flow Usage**: For iOS, prefer callbacks or use KMP-NativeCoroutines library
2. **Memory Management**: Be careful with strong references between Kotlin and Swift
3. **Threading**: Kotlin coroutines run on their own threads; dispatch to main thread in Swift when updating UI
4. **Error Handling**: Always implement proper error handling when bridging between platforms
5. **Nullability**: Kotlin's nullable types map to Swift optionals automatically

## 🚀 Quick Reference

| Task | Solution |
|------|----------|
| Call Kotlin function from Swift | Direct call: `kotlinObject.functionName()` |
| Call Kotlin suspend function | Use `await` in Swift Task/async context |
| Use Kotlin Flow in iOS | Use callbacks or KMP-NativeCoroutines |
| Execute Swift code from Kotlin | Define Kotlin interface, implement in Swift, inject |
| Platform-specific implementations | Use expect/actual pattern |
| Pass data between platforms | Use data classes (become Swift classes) |

## 📚 Additional Resources

- [Kotlin/Native Interop](https://kotlinlang.org/docs/native-objc-interop.html)
- [KMP-NativeCoroutines](https://github.com/rickclephas/KMP-NativeCoroutines)
- [SKIE - Swift-friendly API](https://skie.touchlab.co/)
