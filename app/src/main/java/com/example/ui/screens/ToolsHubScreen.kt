package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TaskStatus
import com.example.service.AppLauncherManager
import com.example.ui.components.GlassmorphismCard
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.PastelPurpleTertiary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.viewmodel.MeeraViewModel

@Composable
fun ToolsHubScreen(
  viewModel: MeeraViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val appLauncher = remember { AppLauncherManager(context) }
  var installedApps by remember { mutableStateOf(emptyList<com.example.service.InstalledAppItem>()) }
  var appSearchQuery by remember { mutableStateOf("") }

  val parallelTasks by viewModel.parallelTaskManager.tasks.collectAsState()
  val isRunningAllTasks by viewModel.parallelTaskManager.isRunningAll.collectAsState()

  var volumeSlider by remember { mutableFloatStateOf(viewModel.deviceControl.getVolumeMusicPercent().toFloat()) }
  var showCallAssistantSimulation by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Device Hardware Controls Card
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Device Hardware Quick Controls",
            color = CyberCyanNeon,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          // WiFi, Bluetooth, Torch toggles
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { viewModel.deviceControl.openWifiSettings() },
              modifier = Modifier.weight(1f).height(38.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Wifi, contentDescription = "WiFi", tint = CyberCyanNeon, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("WiFi", color = CyberCyanNeon, fontSize = 11.sp)
            }

            Button(
              onClick = { viewModel.deviceControl.openBluetoothSettings() },
              modifier = Modifier.weight(1f).height(38.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A5F)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Bluetooth, contentDescription = "Bluetooth", tint = PastelBlueSecondary, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Bluetooth", color = PastelBlueSecondary, fontSize = 11.sp)
            }

            Button(
              onClick = {
                val isOn = viewModel.deviceControl.toggleTorch()
                viewModel.logEvent("TORCH", "Torch state toggled: $isOn")
              },
              modifier = Modifier.weight(1f).height(38.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A102D)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.FlashlightOn, contentDescription = "Torch", tint = PastelPinkPrimary, modifier = Modifier.size(15.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Torch", color = PastelPinkPrimary, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Volume Slider
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text("Volume: ${volumeSlider.toInt()}%", color = TextLightPrimary, fontSize = 11.sp)
          }

          Slider(
            value = volumeSlider,
            onValueChange = {
              volumeSlider = it
              viewModel.deviceControl.setVolumeMusic(it.toInt())
            },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
              thumbColor = CyberPinkNeon,
              activeTrackColor = PastelPinkPrimary,
              inactiveTrackColor = Color(0x33FFFFFF)
            )
          )
        }
      }
    }

    // 2. Parallel Coroutine Multi-Task Manager (6 Concurrent Tasks)
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Multi-Task Coroutines (6 Concurrent)",
              color = PastelPinkPrimary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )

            Button(
              onClick = {
                if (!isRunningAllTasks) {
                  viewModel.parallelTaskManager.executeAllParallel { logMsg ->
                    viewModel.logEvent("TASK_PARALLEL", logMsg)
                  }
                } else {
                  viewModel.parallelTaskManager.resetTasks()
                }
              },
              modifier = Modifier
                .height(34.dp)
                .testTag("run_parallel_tasks_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunningAllTasks) StatusRed else Color(0xFF1B4332)
              ),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = if (isRunningAllTasks) "Cancel All" else "Run All 6",
                color = if (isRunningAllTasks) Color.White else StatusGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          for (task in parallelTasks) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(task.title, color = TextLightPrimary, fontSize = 11.5.sp)
                Text(
                  text = when (task.status) {
                    TaskStatus.IDLE -> "Standby"
                    TaskStatus.RUNNING -> "Running ${(task.progress * 100).toInt()}%"
                    TaskStatus.SUCCESS -> "Completed ✓"
                    TaskStatus.FAILED -> "Failed ✗"
                  },
                  color = when (task.status) {
                    TaskStatus.IDLE -> TextMuted
                    TaskStatus.RUNNING -> CyberCyanNeon
                    TaskStatus.SUCCESS -> StatusGreen
                    TaskStatus.FAILED -> StatusRed
                  },
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace
                )
              }

              Spacer(modifier = Modifier.height(2.dp))

              LinearProgressIndicator(
                progress = { task.progress },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(3.dp)
                  .clip(RoundedCornerShape(1.5.dp)),
                color = when (task.status) {
                  TaskStatus.SUCCESS -> StatusGreen
                  TaskStatus.RUNNING -> CyberPinkNeon
                  else -> Color(0x33FFFFFF)
                },
                trackColor = Color(0x22FFFFFF)
              )
            }
          }
        }
      }
    }

    // 3. Cute Gallery of Meera (/Meera/meera_pics/ collages)
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Image, contentDescription = null, tint = PastelPurpleTertiary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(6.dp))
              Text(
                text = "Meera's Photo Showcase (/Meera/meera_pics/)",
                color = PastelPurpleTertiary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Button(
              onClick = {
                viewModel.sendChatMessage("মীরা, তুমি দেখতে কেমন? একটি ছবি দেখাও তো!")
              },
              modifier = Modifier.height(30.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B1E4A)),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text("তুমি দেখতে কেমন", color = PastelPinkPrimary, fontSize = 10.sp)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            items(viewModel.meeraPics) { pic ->
              Box(
                modifier = Modifier
                  .size(110.dp, 130.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(Color(0xFF140D22))
                  .border(1.dp, Color(pic.primaryColorHex).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                  .padding(8.dp)
              ) {
                Column(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.SpaceBetween
                ) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(Color(pic.primaryColorHex).copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text("♡", color = Color(pic.primaryColorHex), fontSize = 12.sp)
                  }

                  Column {
                    Text(pic.titleBengali, color = TextLightPrimary, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(pic.tag, color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                  }
                }
              }
            }
          }
        }
      }
    }

    // 4. Call Assistant Simulation
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Call, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(6.dp))
              Text("Call Assistant (2 Missed Calls Detector)", color = StatusGreen, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { showCallAssistantSimulation = !showCallAssistantSimulation },
              modifier = Modifier.height(30.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(if (showCallAssistantSimulation) "Hide Simulation" else "Test Simulation", color = StatusGreen, fontSize = 10.sp)
            }
          }

          if (showCallAssistantSimulation) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F0A1A), RoundedCornerShape(8.dp))
                .border(1.dp, StatusGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "🚨 Missed 2 calls from +8801700000000 within 5 min",
                  color = StatusAmber,
                  fontSize = 11.5.sp,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "মীরার স্বয়ংক্রিয় উত্তর: \"হ্যালো, আমি অনিকেতের অ্যাসিস্ট্যান্ট মীরা বলছি। অনিকেত এখন ফোনের কাছে নেই, তাই কল ধরতে পারছে না। আপনি কি কিছু বলবেন অনিকেতকে? বললে বলুন, আমি জানিয়ে দেব।\"",
                  color = TextLightPrimary,
                  fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                  onClick = {
                    viewModel.ttsService.speak(
                      "হ্যালো, আমি অনিকেতের অ্যাসিস্ট্যান্ট মীরা বলছি। অনিকেত এখন ফোনের কাছে নেই, তাই কলটি ধরতে পারছে না। আপনি একটি বার্তা রেখে যান।"
                    )
                  },
                  modifier = Modifier.height(32.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B))
                ) {
                  Text("Play Auto-Answer Voice", color = CyberCyanNeon, fontSize = 10.5.sp)
                }
              }
            }
          }
        }
      }
    }

    // 5. Installed Apps Launcher
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "App Launcher (PackageManager)",
              color = PastelBlueSecondary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )

            Button(
              onClick = {
                installedApps = appLauncher.getInstalledApps()
                viewModel.logEvent("APP_LAUNCHER", "Loaded ${installedApps.size} installed launchable apps")
              },
              modifier = Modifier.height(30.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
              shape = RoundedCornerShape(6.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = null, tint = CyberCyanNeon, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Load Apps (${installedApps.size})", color = CyberCyanNeon, fontSize = 10.5.sp)
            }
          }

          if (installedApps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = appSearchQuery,
              onValueChange = { appSearchQuery = it },
              placeholder = { Text("Search installed apps...", color = TextMuted, fontSize = 11.sp) },
              modifier = Modifier.fillMaxWidth().height(46.dp),
              singleLine = true,
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PastelBlueSecondary,
                unfocusedBorderColor = Color(0x33FFFFFF),
                focusedTextColor = TextLightPrimary,
                unfocusedTextColor = TextLightSecondary
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val filtered = installedApps.filter { it.appName.contains(appSearchQuery, ignoreCase = true) }
            for (app in filtered.take(10)) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(Color(0x18FFFFFF))
                  .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(app.appName, color = TextLightPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  Text(app.packageName, color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  IconButton(onClick = { appLauncher.launchApp(app.packageName) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Launch, contentDescription = "Launch", tint = CyberCyanNeon, modifier = Modifier.size(16.dp))
                  }
                  IconButton(onClick = { appLauncher.openPlayStore(app.packageName) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Shop, contentDescription = "Store", tint = PastelPinkPrimary, modifier = Modifier.size(16.dp))
                  }
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
