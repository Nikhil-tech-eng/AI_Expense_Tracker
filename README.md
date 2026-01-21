# 🪙 AIExpenseTracker — Intelligent Expense Management

> **Transforming chaotic receipts into structured financial insights with on-device ML and Generative AI.**

---

### 💡 The Vision

Managing daily expenses shouldn't feel like a chore. **AIExpenseTracker** is a next-generation Android application designed to fully automate how you track your personal finances. By fusing advanced **Generative AI (Gemini API)** with robust on-device vision processing (**CameraX** & **ML Kit**), the app eliminates manual data entry, categorizes items instantly, and offers clean visual insights into your spending behavior.

---

### 🚀 Core Capability Matrix

*   **⚡ Automated Intelligence**: Type in any transaction title, and the embedded **Gemini AI** engine automatically predicts the most accurate budget category.
*   **👁️ Smart Receipt Processing**: Point your camera at any receipt. Using high-precision **OCR (Text Recognition)**, the app extracts the vendor, total bill amount, and date in seconds.
*   **📊 Dynamic Visual Analytics**: Make sense of your financial habits with rich, interactive data visualizations built natively with Jetpack Compose.
*   **🔒 Local-First Reliability**: Backed by a high-performance **Room Database**, all your data remains securely cached on your device for absolute offline-first accessibility.

---

### 🗺️ System Architecture Blueprint

This repository is built following modern multi-module architecture guidelines. This guarantees clean boundary separation, excellent testability, and fast incremental Gradle build times.

```mermaid
graph TD
    subgraph App Shell
        A[/:app]
    end
    
    subgraph Feature Modules
        E1[/:features:expensehome]
        E2[/:features:camerax]
        E3[/:features:mltoolkit]
    end

    subgraph Core Framework
        D[/:data]
        DS[/:design]
        N[/:navigation]
        T[/:test-utils]
    end

    A --> E1 & E2 & E3
    E1 & E2 & E3 --> N & DS & D
    D --> T
```

*   `:app` — Application entry point, dependency injection wire-up (`Hilt`), and root activity initialization.
*   `:features:expensehome` — Dashboard hub, transaction feed, interactive charts, and flow control.
*   `:features:camerax` — Implements the device camera interface utilizing Jetpack CameraX components.
*   `:features:mltoolkit` — On-device AI processing layer responsible for parsing images into readable data strings.
*   `:data` — Single source of truth handling local SQLite/Room storage, model mappings, and network protocols.
*   `:design` — The project design token system (colors, typography, shapes, and custom atomic components).
*   `:navigation` — Strongly-typed navigation routes ensuring compile-time safety across multi-module hops.

---

### 🛠️ Technology Stack

*   **Language**: 100% [Kotlin](https://kotlinlang.org/) with modern coroutines and state-driven `Flow`.
*   **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for fully declarative, Material 3-compliant layouts.
*   **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for robust, compile-time safe injection graphs.
*   **Networking & Serialization**: [Ktor Client](https://ktor.io/) paired with [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for fast, lightweight network handshakes.
*   **Local Engine**: [Room Database](https://developer.android.com/target/room) implementing fully-reactive reactive streams for seamless UI state updates.

---

### ⚙️ Onboarding & Environment Setup

Getting a local instance up and running takes less than 5 minutes:

#### 1. Requirements
*   **Android Studio Ladybug** (or any newer version)
*   **Android SDK 34+**

#### 2. Injecting your Secret Key
The Gemini engine requires an API key to communicate with Google's large language models safely. 

1. Head over to [Google AI Studio](https://aistudio.google.com/) and grab a free API key.
2. Open your project's root directory and locate `local.properties`.
3. Append your key to the end of the file like so:

```properties
GEMINI_API_KEY="AIzaSyYourSecretKeyGoesRightHere..."
```

4. Hit **Sync Project with Gradle Files** in Android Studio, press **Run**, and watch the app come to life!

---
