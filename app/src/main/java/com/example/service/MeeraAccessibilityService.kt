package com.example.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MeeraAccessibilityService : AccessibilityService() {

  companion object {
    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected = _isServiceConnected.asStateFlow()

    private val _lastEventLog = MutableStateFlow("Accessibility Service waiting...")
    val lastEventLog = _lastEventLog.asStateFlow()

    var instance: MeeraAccessibilityService? = null
      private set
  }

  override fun onServiceConnected() {
    super.onServiceConnected()
    instance = this
    _isServiceConnected.value = true
    _lastEventLog.value = "Meera Accessibility Service Connected"
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    if (event == null) return
    val packageName = event.packageName?.toString() ?: "Unknown"
    val eventType = AccessibilityEvent.eventTypeToString(event.eventType)
    _lastEventLog.value = "Event from $packageName: $eventType"
  }

  override fun onInterrupt() {
    _lastEventLog.value = "Meera Accessibility Service Interrupted"
  }

  override fun onDestroy() {
    super.onDestroy()
    instance = null
    _isServiceConnected.value = false
  }

  /**
   * Helper method to assist user with typing text into editable focused fields
   */
  fun setInputTextOnFocusedNode(text: String): Boolean {
    val root = rootInActiveWindow ?: return false
    val focusedNode = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT) ?: return false
    val arguments = Bundle().apply {
      putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
    }
    return focusedNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
  }

  /**
   * Helper method to find and click a clickable node by its text
   */
  fun clickNodeByText(text: String): Boolean {
    val root = rootInActiveWindow ?: return false
    val nodes = root.findAccessibilityNodeInfosByText(text)
    for (node in nodes) {
      if (node.isClickable) {
        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
      }
      var parent = node.parent
      while (parent != null) {
        if (parent.isClickable) {
          return parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
        parent = parent.parent
      }
    }
    return false
  }

  /**
   * Helper method to navigate back via global action
   */
  fun performGlobalBack(): Boolean {
    return performGlobalAction(GLOBAL_ACTION_BACK)
  }

  /**
   * Helper method to navigate to home screen via global action
   */
  fun performGlobalHome(): Boolean {
    return performGlobalAction(GLOBAL_ACTION_HOME)
  }
}
