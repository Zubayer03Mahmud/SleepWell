# Alarm Feature Walkthrough

I have successfully implemented the **Set Alarm** feature in the SleepWell application.

## Key Accomplishments

- **System Integration**: Used `AlarmManager` for precise, system-level alarm scheduling that works even when the app is in the background.
- **Notifications**: Implemented a `BroadcastReceiver` (`AlarmReceiver`) that triggers high-priority notifications when an alarm goes off.
- **Modern UI**: Created a dedicated **Alarms Screen** using Jetpack Compose with a Material 3 `TimePicker` for selecting wake-up times.
- **Dashboard Integration**: Added an "ALARM" card to the Home Dashboard for quick access to alarm settings.
- **Robust Logic**: Handled alarm scheduling, toggling (enable/disable), and deletion.

## Technical Details

### Permissions Added
- `SCHEDULE_EXACT_ALARM`: For precise alarm triggers.
- `POST_NOTIFICATIONS`: For showing alarm alerts on Android 13+.
- `WAKE_LOCK`: To ensure the alarm logic executes correctly when the screen is off.
- `RECEIVE_BOOT_COMPLETED`: Prepared for re-scheduling alarms after a device reboot.

### Data Flow
1. **Model**: `AlarmData` stores the time and state of each alarm.
2. **Helper**: `AlarmManagerHelper` interacts with the Android system's `AlarmManager`.
3. **ViewModel**: `AlarmViewModel` manages the list of alarms and coordinates with the helper to schedule/cancel them.
4. **UI**: `AlarmScreen` provides the interface for users to manage their alarms.

## How to Test

1. **Launch the App**: Go to the **Home Dashboard**.
2. **Open Alarms**: Tap the new **ALARM** card.
3. **Set an Alarm**:
    - Tap the **+** (plus) button at the bottom right.
    - Select a time (e.g., 1 minute from now) in the time picker.
    - Tap **Set Alarm**.
4. **Verify Notification**: Wait for the set time and verify that a "SleepWell Alarm" notification appears.
5. **Manage Alarms**:
    - Toggle the switch to disable/enable an alarm.
    - Tap the **Delete** (trash) icon to remove an alarm.
