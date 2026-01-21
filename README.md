# 🪙 AIExpenseTracker

> An intelligent Android expense management application powered by Generative AI, receipt scanning, and dynamic financial analytics.

AIExpenseTracker helps users record, categorize, and understand daily expenses through a modern Android interface. It combines Gemini AI, CameraX, ML Kit, Room Database, and Jetpack Compose to reduce manual expense tracking.

## ✨ Features

- 🤖 **AI-Powered Categorization**
  - Uses Gemini AI to suggest appropriate expense categories.
  - Supports category selection and natural-language expense input.
- 🧾 **Smart Receipt Scanning**
  - Capture receipts using the device camera.
  - Extract expense information using OCR and ML Kit.
- 📊 **Expense Analytics**
  - Dynamic pie/donut chart based on actual expense categories.
  - Category-wise spending visualization.
  - Matching chart colors and category legend.
- 🔎 **Search & Filtering**
  - Search expenses by title or category.
  - Filter transactions by category.
  - Search and category filters work together.
- 🗑️ **Transaction Management**
  - Add detailed expense records.
  - Delete individual transactions with confirmation.
  - Automatically update expense totals and analytics.
- 💾 **Local-First Storage**
  - Uses Room Database for local expense storage.
  - Existing records remain available without requiring constant internet access.
- 🎨 **Modern Material 3 UI**
  - Built using Jetpack Compose.
  - Clean, responsive, professional academic-project design.

## 🏗️ Architecture

The application follows a modular Android architecture:

```text
AIExpenseTracker
│
├── app
│   └── Application entry point
│
├── features
│   ├── expensehome
│   │   ├── Dashboard
│   │   ├── Transactions
│   │   └── Analytics
│   │
│   ├── camerax
│   │   └── Receipt scanning
│   │
│   └── mltoolkit
│       └── OCR / ML processing
│
├── data
│   ├── Room Database
│   ├── Entities
│   ├── DAO
│   ├── Repository
│   └── Data models
│
├── design
│   └── UI design system
│
├── navigation
│   └── Application navigation
│
└── test-utils
    └── Testing utilities
```

### Module Responsibilities

| Module | Responsibility |
|---|---|
| `app` | Application entry point, Hilt setup and root activity |
| `features:expensehome` | Dashboard, transactions, charts and expense workflows |
| `features:camerax` | Camera interface and receipt capture |
| `features:mltoolkit` | On-device OCR and image processing |
| `data` | Room database, repositories, models and data access |
| `design` | Colors, typography, shapes and reusable UI components |
| `navigation` | Application navigation |
| `test-utils` | Testing utilities |

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Declarative Android UI |
| **Material 3** | Modern UI components |
| **Gemini API** | Generative AI and category suggestions |
| **CameraX** | Receipt image capture |
| **ML Kit** | On-device text recognition |
| **Room** | Local database |
| **Hilt** | Dependency injection |
| **Kotlin Coroutines** | Asynchronous operations |
| **Kotlin Flow** | Reactive application state |
| **Ktor Client** | Networking |
| **Kotlinx Serialization** | Data serialization |

## 📱 Application Flow

```text
User
 │
 ├── Add Expense
 │      ├── Title
 │      ├── Amount
 │      ├── Category
 │      ├── Date
 │      ├── Payment Method
 │      └── Notes
 │
 ├── Ask AI
 │      └── Gemini Category Suggestion
 │
 └── Scan Receipt
        └── CameraX → ML Kit OCR
                    │
                    ▼
              Expense Record
                    │
                    ▼
              Room Database
                    │
                    ▼
               Dashboard
              ┌─────┴─────┐
              ▼           ▼
        Transactions    Analytics
              │           │
        Search/Filter   Pie/Donut Chart
              │           │
              └─────┬─────┘
                    ▼
              Category Insights
```

## ⚙️ Setup

### Requirements

- Android Studio Ladybug or newer
- Android SDK 34+
- JDK compatible with the project's Gradle configuration
- Internet connection for Gemini AI functionality

### Gemini API Key

The Gemini API key should remain local and must **never be committed to Git**.

1. Create an API key through Google AI Studio.
2. Open the project's `local.properties`.
3. Add the required API-key property expected by the project's Gradle configuration.
4. Sync the project with Gradle Files.
5. Build and run the application.

> Do not place real API keys directly inside source code or generated `BuildConfig.java` files.

## 🔐 Security

- Keep API keys out of source control.
- Use `local.properties` or another local secret-management mechanism.
- Never commit generated `BuildConfig.java` files containing secrets.
- GitHub Push Protection should remain enabled.

## 📂 Project Structure

```text
AIExpenseTracker/
├── app/
├── data/
├── design/
├── navigation/
├── features/
│   ├── expensehome/
│   ├── camerax/
│   └── mltoolkit/
├── test-utils/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🎓 Project Context

AIExpenseTracker is developed as an academic Android project demonstrating the integration of:

- Modern Android development
- Modular architecture
- Jetpack Compose
- Local database management
- Generative AI
- OCR and computer-vision-assisted workflows
- Data visualization
- Expense management

## 📄 License

This project is intended for academic and educational purposes.
- Github: https://github.com/Nikhil-tech-eng
- Mail ID: teckchandaninikhil@gmail.com
