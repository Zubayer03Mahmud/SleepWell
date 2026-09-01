# Alarm Feature Implementation Plan

Implement a comprehensive "Set Alarm" feature that allows users to schedule wake-up alarms with system notifications.

## User Review Required

> [!IMPORTANT]
> To ensure the alarm works correctly on Android 12+, the app will request `SCHEDULE_EXACT_ALARM` permission. On Android 13+, it will also request `POST_NOTIFICATIONS` permission.

## Proposed Changes

### Android Manifest & Permissions
- Add `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`, and `POST_NOTIFICATIONS` permissions.
- Register `AlarmReceiver` as a BroadcastReceiver to handle alarm events.
- Add `WAKE_LOCK` to ensure the device stays awake while processing the alarm.

---

### Data & Logic Layer
#### [NEW] [AlarmReceiver.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/data/alarm/AlarmReceiver.kt)
A `BroadcastReceiver` that triggers a notification when the alarm goes off.

#### [NEW] [AlarmManagerHelper.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/data/alarm/AlarmManagerHelper.kt)
A utility class to wrap `AlarmManager` calls for scheduling, updating, and canceling alarms.

#### [NEW] [AlarmData.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/data/alarm/model/AlarmData.kt)
Data class to represent an alarm (time, enabled state, etc.).

---

### UI Layer
#### [NEW] [AlarmViewModel.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/ui/alarm/AlarmViewModel.kt)
ViewModel to manage the alarm list and handle scheduling logic.

#### [NEW] [AlarmScreen.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/ui/alarm/AlarmScreen.kt)
A Compose screen allowing users to:
- View current alarms.
- Toggle alarms on/off.
- Set a new alarm using a time picker.
- Delete alarms.

---

### Navigation & Integration
#### [MODIFY] [MainActivity.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/MainActivity.kt)
- Add `Alarm` to `Screen` enum.
- Add navigation branch for `AlarmScreen`.

#### [MODIFY] [HomeDashboardScreen.kt](file:///D:/CSE--BOOK--SEM/4-1(CSE-30)/4-1 CSE-31/Mobile App Dev/app/src/main/java/com/example/sleepwell/ui/home/HomeDashboardScreen.kt)
- Add an "Alarm" card to the dashboard for quick access.

## Verification Plan

### Automated Tests
- Build verification: `./gradlew assembleDebug`.

### Manual Verification
- Navigate to the Alarm screen from the Home dashboard.
- Set an alarm for 1 minute in the future.
- Verify the notification appears when the time is reached.
- Toggle an alarm off and verify it doesn't trigger.
- Delete an alarm and verify it is removed from the list.
