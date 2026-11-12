package com.example.service

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log

class DeviceControlManager(private val context: Context) {

  private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
  private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
  private var isTorchOn = false

  fun setVolumeMusic(percent: Int) {
    try {
      val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
      val target = (max * (percent.coerceIn(0, 100) / 100f)).toInt()
      audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, target, AudioManager.FLAG_SHOW_UI)
    } catch (e: Exception) {
      Log.e("DeviceControl", "Failed to adjust volume: ${e.message}")
    }
  }

  fun getVolumeMusicPercent(): Int {
    val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    return if (max > 0) ((current.toFloat() / max) * 100).toInt() else 0
  }

  fun setRingerMode(mode: Int) {
    try {
      audioManager.ringerMode = mode
    } catch (e: Exception) {
      Log.e("DeviceControl", "Failed to change ringer mode: ${e.message}")
    }
  }

  fun toggleTorch(): Boolean {
    return try {
      val cameraId = cameraManager.cameraIdList.firstOrNull() ?: return false
      isTorchOn = !isTorchOn
      cameraManager.setTorchMode(cameraId, isTorchOn)
      isTorchOn
    } catch (e: Exception) {
      Log.e("DeviceControl", "Torch error: ${e.message}")
      false
    }
  }

  fun openWifiSettings() {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun openBluetoothSettings() {
    val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun openDisplaySettings() {
    val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun checkCanWriteSettings(): Boolean {
    return Settings.System.canWrite(context)
  }

  fun requestWriteSettingsPermission() {
    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
      data = Uri.parse("package:" + context.packageName)
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun setScreenBrightness(brightness0To255: Int): Boolean {
    return if (checkCanWriteSettings()) {
      try {
        Settings.System.putInt(
          context.contentResolver,
          Settings.System.SCREEN_BRIGHTNESS,
          brightness0To255.coerceIn(0, 255)
        )
        true
      } catch (e: Exception) {
        false
      }
    } else {
      false
    }
  }
}
