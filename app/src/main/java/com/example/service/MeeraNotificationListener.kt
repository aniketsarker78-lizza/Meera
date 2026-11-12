package com.example.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MeeraNotificationListener : NotificationListenerService() {

  companion object {
    private val _isListenerConnected = MutableStateFlow(false)
    val isListenerConnected = _isListenerConnected.asStateFlow()

    private val _recentNotifications = MutableStateFlow<List<String>>(emptyList())
    val recentNotifications = _recentNotifications.asStateFlow()

    var instance: MeeraNotificationListener? = null
      private set
  }

  override fun onListenerConnected() {
    super.onListenerConnected()
    instance = this
    _isListenerConnected.value = true
  }

  override fun onListenerDisconnected() {
    super.onListenerDisconnected()
    instance = null
    _isListenerConnected.value = false
  }

  override fun onNotificationPosted(sbn: StatusBarNotification?) {
    if (sbn == null) return
    val packageName = sbn.packageName
    val extras = sbn.notification.extras
    val title = extras.getString("android.title") ?: ""
    val text = extras.getCharSequence("android.text")?.toString() ?: ""

    val logSummary = "[$packageName] $title: $text".take(120)
    val current = _recentNotifications.value.toMutableList()
    current.add(0, logSummary)
    if (current.size > 20) {
      current.removeAt(current.size - 1)
    }
    _recentNotifications.value = current
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification?) {
    // Optional tracking
  }
}
