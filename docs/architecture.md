# Droid Automator - Architecture

## Overview

Droid Automator is an Android automation app that allows users to create trigger → action workflows. The app follows Clean Architecture principles with MVVM pattern for the presentation layer.

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                         UI Layer                             │
│  (Jetpack Compose screens, ViewModels, UI State)            │
├─────────────────────────────────────────────────────────────┤
│                       Domain Layer                           │
│  (Models, Engine, Use Cases)                                 │
├─────────────────────────────────────────────────────────────┤
│                        Data Layer                            │
│  (Room Database, Repositories, DAOs)                         │
├─────────────────────────────────────────────────────────────┤
│                      Service Layer                           │
│  (NotificationListenerService)                               │
└─────────────────────────────────────────────────────────────┘
```

## Package Structure

```
com.droidautomator/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAOs
│   │   ├── entity/        # Room entities
│   │   ├── converter/     # JSON converters
│   │   └── AutomationDatabase.kt
│   └── repository/        # Repository implementations
├── domain/
│   ├── model/             # Domain models
│   └── engine/            # Automation engine components
├── service/               # Android services
├── ui/
│   ├── navigation/        # Navigation graph
│   ├── screens/           # Feature screens
│   ├── components/        # Reusable components
│   └── theme/             # Material 3 theme
└── di/                    # Hilt modules
```

## Key Components

### Automation Engine

The `AutomationEngine` is the core of the app:

1. Receives notification data from `NotificationListenerService`
2. Fetches enabled automations from the repository
3. Uses `TriggerMatcher` to find matching automations
4. Builds variable context using `VariableParser`
5. Executes actions via `ActionExecutor`
6. Logs results to the database

### Extensible Trigger/Action System

Triggers and Actions use Kotlin sealed classes for type-safe extensibility:

```kotlin
sealed class Trigger {
    data class NotificationTrigger(...) : Trigger()
    // Add new triggers here
}

sealed class Action {
    data class OpenUrlAction(...) : Action()
    // Add new actions here
}
```

### Variable System

Variables are extracted from trigger context and substituted into action templates:

- `{notification_title}` - Notification title
- `{notification_text}` - Notification body
- `{notification_package}` - Source app package
- `{parsed_amount}` - Extracted numeric amount
- `{timestamp}` - Unix timestamp

## Data Flow

```
Notification Posted
       ↓
NotificationListenerService
       ↓
AutomationEngine.processNotification()
       ↓
TriggerMatcher.matches() ─→ Find matching automations
       ↓
VariableParser.buildContext() ─→ Extract variables
       ↓
ActionExecutor.execute() ─→ Execute actions
       ↓
LogRepository.logExecution() ─→ Save to database
```

## Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **DI**: Hilt
- **Database**: Room with Flow
- **Serialization**: Kotlin Serialization
- **Architecture**: MVVM + Clean Architecture
