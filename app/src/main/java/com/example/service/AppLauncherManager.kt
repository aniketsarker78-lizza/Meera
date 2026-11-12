package com.example.service

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri

data class InstalledAppItem(
  val packageName: String,
  val appName: String,
  val icon: Drawable? = null,
  val isSystemApp: Boolean
)

class AppLauncherManager(private val context: Context) {

  fun getInstalledApps(): List<InstalledAppItem> {
    val pm = context.packageManager
    val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
    val appList = mutableListOf<InstalledAppItem>()

    for (appInfo in packages) {
      val launchIntent = pm.getLaunchIntentForPackage(appInfo.packageName)
      if (launchIntent != null) {
        val appName = pm.getApplicationLabel(appInfo).toString()
        val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        appList.add(
          InstalledAppItem(
            packageName = appInfo.packageName,
            appName = appName,
            icon = try { pm.getApplicationIcon(appInfo) } catch (e: Exception) { null },
            isSystemApp = isSystem
          )
        )
      }
    }
    return appList.sortedBy { it.appName.lowercase() }
  }

  fun launchApp(packageName: String): Boolean {
    val pm = context.packageManager
    val intent = pm.getLaunchIntentForPackage(packageName) ?: return false
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    return true
  }

  fun openPlayStore(packageName: String) {
    try {
      val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(marketIntent)
    } catch (e: Exception) {
      val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
      context.startActivity(webIntent)
    }
  }
}
