package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ConsoleScreen
import com.example.ui.screens.JapaneseScreen
import com.example.ui.screens.SanataniScreen
import com.example.ui.screens.ToolsHubScreen
import com.example.ui.screens.VoiceChatScreen
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.PastelPurpleTertiary
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextMuted
import com.example.viewmodel.MeeraViewModel

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MeeraMainApp()
      }
    }
  }
}

data class NavItem(
  val title: String,
  val icon: ImageVector,
  val testTag: String
)

@Composable
fun MeeraMainApp(viewModel: MeeraViewModel = viewModel()) {
  var currentTab by remember { mutableIntStateOf(0) }

  // Permission Launcher for runtime permissions
  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    viewModel.logEvent("PERM_RESULT", "Permission request completed, granted=$isGranted")
    viewModel.refreshSystemStatus()
  }

  val navItems = listOf(
    NavItem("Console", Icons.Default.Terminal, "nav_console"),
    NavItem("Voice", Icons.Default.Mic, "nav_voice"),
    NavItem("Sanatani", Icons.Default.SelfImprovement, "nav_sanatani"),
    NavItem("Japanese", Icons.Default.Translate, "nav_japanese"),
    NavItem("Tools", Icons.Default.Build, "nav_tools")
  )

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            CyberDarkBg,
            Color(0xFF140C24),
            Color(0xFF1A0E2E)
          )
        )
      ),
    containerColor = Color.Transparent,
    bottomBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 8.dp, vertical = 6.dp)
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xE6160F26))
          .border(1.dp, Brush.horizontalGradient(listOf(PastelPinkPrimary.copy(alpha = 0.5f), CyberCyanNeon.copy(alpha = 0.5f))), RoundedCornerShape(20.dp))
      ) {
        NavigationBar(
          containerColor = Color.Transparent,
          modifier = Modifier.fillMaxWidth()
        ) {
          navItems.forEachIndexed { index, item ->
            val isSelected = currentTab == index
            NavigationBarItem(
              selected = isSelected,
              onClick = { currentTab = index },
              icon = {
                Icon(
                  imageVector = item.icon,
                  contentDescription = item.title,
                  modifier = Modifier.size(20.dp),
                  tint = if (isSelected) CyberPinkNeon else TextMuted
                )
              },
              label = {
                Text(
                  text = item.title,
                  color = if (isSelected) PastelPinkPrimary else TextMuted,
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontFamily = FontFamily.Monospace
                )
              },
              modifier = Modifier.testTag(item.testTag),
              colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0x33FF4D94),
                selectedIconColor = CyberPinkNeon,
                selectedTextColor = PastelPinkPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
              )
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        0 -> ConsoleScreen(
          viewModel = viewModel,
          onRequestPermission = { perm -> permissionLauncher.launch(perm) },
          onNavigateToVoiceChat = { currentTab = 1 }
        )
        1 -> VoiceChatScreen(viewModel = viewModel)
        2 -> SanataniScreen(viewModel = viewModel)
        3 -> JapaneseScreen(viewModel = viewModel)
        4 -> ToolsHubScreen(viewModel = viewModel)
      }
    }
  }
}
