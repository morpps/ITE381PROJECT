package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.EnrollmentRepository
import com.example.ui.components.AppTopBar
import com.example.ui.screens.AssessmentSlipScreen
import com.example.ui.screens.CloudArchitectureScreen
import com.example.ui.screens.EnrollmentWizardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ServerMonitorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.viewmodel.EnrollmentViewModel
import com.example.ui.viewmodel.EnrollmentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = EnrollmentRepository(database.enrollmentDao())
        val viewModelFactory = EnrollmentViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val enrollmentViewModel: EnrollmentViewModel = viewModel(factory = viewModelFactory)
                val isLoggedIn by enrollmentViewModel.isLoggedIn.collectAsState()

                Crossfade(targetState = isLoggedIn, label = "auth_crossfade") { loggedIn ->
                    if (loggedIn) {
                        MainAppScreen(viewModel = enrollmentViewModel)
                    } else {
                        LoginScreen(viewModel = enrollmentViewModel)
                    }
                }
            }
        }
    }
}

enum class MainTab(val index: Int, val label: String) {
    ENROLLMENT(0, "Enrollment"),
    ASSESSMENT(1, "Assessment"),
    SERVER_MONITOR(2, "Server Telemetry"),
    ARCHITECTURE(3, "Architecture Plan")
}

@Composable
fun MainAppScreen(viewModel: EnrollmentViewModel) {
    var selectedTab by remember { mutableIntStateOf(MainTab.ENROLLMENT.index) }
    val autoSaveStatus by viewModel.autoSaveStatus.collectAsState()
    val networkMode by viewModel.networkMode.collectAsState()
    val draft by viewModel.draft.collectAsState()

    // Handle back press if user is on secondary tabs
    if (selectedTab != MainTab.ENROLLMENT.index) {
        BackHandler {
            selectedTab = MainTab.ENROLLMENT.index
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                autoSaveStatus = autoSaveStatus,
                networkMode = networkMode,
                studentName = "${draft.firstName} ${draft.lastName}".trim(),
                onLogout = { viewModel.logout() },
                onNetworkModeChange = { mode -> viewModel.setNetworkMode(mode) },
                onLoadVergaraPreset = {
                    viewModel.loadPresetStudent("VERGARA")
                    selectedTab = MainTab.ENROLLMENT.index
                },
                onLoadVillaluzPreset = {
                    viewModel.loadPresetStudent("VILLALUZ")
                    selectedTab = MainTab.ENROLLMENT.index
                },
                onResetDraft = {
                    viewModel.resetDraft()
                    selectedTab = MainTab.ENROLLMENT.index
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NeustNavyDark,
                contentColor = Color.White,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == MainTab.ENROLLMENT.index,
                    onClick = { selectedTab = MainTab.ENROLLMENT.index },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.ENROLLMENT.index) Icons.Filled.AppRegistration else Icons.Outlined.AppRegistration,
                            contentDescription = "Enrollment"
                        )
                    },
                    label = {
                        Text(
                            text = "Enrollment",
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == MainTab.ENROLLMENT.index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeustNavyDark,
                        selectedTextColor = NeustGold,
                        indicatorColor = NeustGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_enrollment")
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.ASSESSMENT.index,
                    onClick = { selectedTab = MainTab.ASSESSMENT.index },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.ASSESSMENT.index) Icons.Filled.Article else Icons.Outlined.Article,
                            contentDescription = "Assessment"
                        )
                    },
                    label = {
                        Text(
                            text = "Assessment",
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == MainTab.ASSESSMENT.index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeustNavyDark,
                        selectedTextColor = NeustGold,
                        indicatorColor = NeustGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_assessment")
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.SERVER_MONITOR.index,
                    onClick = { selectedTab = MainTab.SERVER_MONITOR.index },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.SERVER_MONITOR.index) Icons.Filled.Dns else Icons.Outlined.Dns,
                            contentDescription = "Server Monitor"
                        )
                    },
                    label = {
                        Text(
                            text = "Server",
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == MainTab.SERVER_MONITOR.index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeustNavyDark,
                        selectedTextColor = NeustGold,
                        indicatorColor = NeustGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_server")
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.ARCHITECTURE.index,
                    onClick = { selectedTab = MainTab.ARCHITECTURE.index },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.ARCHITECTURE.index) Icons.Filled.AccountTree else Icons.Outlined.AccountTree,
                            contentDescription = "Architecture"
                        )
                    },
                    label = {
                        Text(
                            text = "Blueprint",
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == MainTab.ARCHITECTURE.index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeustNavyDark,
                        selectedTextColor = NeustGold,
                        indicatorColor = NeustGold,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_blueprint")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = selectedTab, label = "tab_crossfade") { tabIndex ->
                when (tabIndex) {
                    MainTab.ENROLLMENT.index -> {
                        EnrollmentWizardScreen(
                            viewModel = viewModel,
                            onNavigateToCOR = { selectedTab = MainTab.ASSESSMENT.index }
                        )
                    }
                    MainTab.ASSESSMENT.index -> {
                        AssessmentSlipScreen(viewModel = viewModel)
                    }
                    MainTab.SERVER_MONITOR.index -> {
                        ServerMonitorScreen(viewModel = viewModel)
                    }
                    MainTab.ARCHITECTURE.index -> {
                        CloudArchitectureScreen()
                    }
                }
            }
        }
    }
}
