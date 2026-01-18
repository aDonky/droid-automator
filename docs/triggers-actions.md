# Triggers and Actions Reference

## Triggers

### Notification Trigger

Fires when a notification is received from a specified app.

**Configuration:**
- `packageName` (required): The app package to monitor (e.g., `com.google.android.apps.walletnfcrel`)
- `titleFilter` (optional): Regex pattern to match notification title
- `textFilter` (optional): Regex pattern to match notification text

**Available Variables:**
| Variable | Description | Example |
|----------|-------------|---------|
| `{notification_title}` | Notification title | `GREGGS` |
| `{notification_text}` | Notification body text | `£1.35 with Mastercard ••2722` |
| `{notification_package}` | Source app package | `com.google.android.apps.walletnfcrel` |
| `{parsed_amount}` | Extracted numeric amount | `1.35` |
| `{timestamp}` | Unix timestamp (ms) | `1705612800000` |

### Future Triggers (Planned)

- **Time Trigger**: Schedule automations at specific times
- **Battery Trigger**: React to battery level changes
- **WiFi Trigger**: React to WiFi connection/disconnection
- **App Trigger**: React to app open/close events
- **Geofence Trigger**: React to location changes

## Actions

### Open URL Action

Opens a URL or deep link with variable substitution.

**Configuration:**
- `urlTemplate` (required): URL template with optional variables

**Example:**
```
keystone://spending?merchant={notification_title}&amount={parsed_amount}
```

Variables are URL-encoded automatically.

### Future Actions (Planned)

- **Show Notification**: Display a custom notification
- **HTTP Request**: Make HTTP GET/POST requests
- **Launch App**: Open a specific app
- **Toggle Settings**: Control WiFi, Bluetooth, etc.
- **Run Shell Command**: Execute shell commands (root required)

## Example: Google Wallet Payment Tracking

**Trigger:**
- Type: Notification
- Package: `com.google.android.apps.walletnfcrel`
- Title Filter: (empty - matches all)
- Text Filter: (empty - matches all)

**Action:**
- Type: Open URL
- URL Template: `keystone://spending?merchant={notification_title}&amount={parsed_amount}`

**How it works:**
1. Google Wallet sends notification: "GREGGS" / "£1.35 with Mastercard ••2722"
2. Trigger matches the notification from Google Wallet package
3. Variables extracted: `notification_title=GREGGS`, `parsed_amount=1.35`
4. Opens: `keystone://spending?merchant=GREGGS&amount=1.35`

## Adding New Triggers/Actions

To add a new trigger type:

1. Add a new subclass to `Trigger` sealed class in `domain/model/Trigger.kt`
2. Add serialization annotation with unique type name
3. Update `TriggerMatcher.matches()` to handle the new trigger type
4. Update `VariableParser.buildContext()` if new variables are needed
5. Update UI editor to support configuring the new trigger

To add a new action type:

1. Add a new subclass to `Action` sealed class in `domain/model/Action.kt`
2. Add serialization annotation with unique type name
3. Add execution logic in `ActionExecutor.execute()`
4. Update UI editor to support configuring the new action
