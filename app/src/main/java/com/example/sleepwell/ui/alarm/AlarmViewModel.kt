package com.example.sleepwell.ui.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sleepwell.data.alarm.AlarmManagerHelper
import com.example.sleepwell.data.alarm.model.AlarmData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmHelper = AlarmManagerHelper(application)
    
    private val _alarms = MutableStateFlow<List<AlarmData>>(emptyList())
    val alarms: StateFlow<List<AlarmData>> = _alarms.asStateFlow()

    fun addAlarm(hour: Int, minute: Int) {
        val newAlarm = AlarmData(
            id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            hour = hour,
            minute = minute,
            isEnabled = true
        )
        _alarms.value = _alarms.value + newAlarm
        alarmHelper.scheduleAlarm(newAlarm)
    }

    fun toggleAlarm(alarm: AlarmData) {
        val updatedAlarms = _alarms.value.map {
            if (it.id == alarm.id) it.copy(isEnabled = !it.isEnabled) else it
        }
        _alarms.value = updatedAlarms
        
        val updatedAlarm = updatedAlarms.find { it.id == alarm.id }
        if (updatedAlarm != null) {
            if (updatedAlarm.isEnabled) {
                alarmHelper.scheduleAlarm(updatedAlarm)
            } else {
                alarmHelper.cancelAlarm(updatedAlarm)
            }
        }
    }

    fun deleteAlarm(alarm: AlarmData) {
        _alarms.value = _alarms.value.filter { it.id != alarm.id }
        alarmHelper.cancelAlarm(alarm)
    }
}
