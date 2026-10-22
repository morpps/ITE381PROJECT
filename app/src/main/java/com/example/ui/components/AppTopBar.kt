package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NetworkMode
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    autoSaveStatus: String,
    networkMode: NetworkMode,
    studentName: String = "",
    onLogout: () -> Unit = {},
    onNetworkModeChange: (NetworkMode) -> Unit,
    onLoadVergaraPreset: () -> Unit,
    onLoadVillaluzPreset: () -> Unit,
    onResetDraft: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NeustNavyPrimary,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeustGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "NEUST Crest",
                            tint = NeustNavyDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "NEUST Portal",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NeustGold.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "v2.6 C-SCALE",
                                    color = NeustGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Nueva Ecija University of Science and Technology",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Color.White.copy(alpha = 0.8f),
                            maxLines = 1
                        )
                    }
                }
            },
            actions = {
                if (studentName.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = NeustGold.copy(alpha = 0.25f),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(NeustGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = studentName.take(1),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeustNavyDark
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = studentName.split(" ").firstOrNull() ?: studentName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.testTag("top_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options menu",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    Text(
                        text = "TESTING PROFILES (FROM DOC)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenuItem(
                        text = { Text("Load Jubert Vergara (BSIT-4)") },
                        onClick = {
                            onLoadVergaraPreset()
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Load Richmond Villaluz (BSIT-4)") },
                        onClick = {
                            onLoadVillaluzPreset()
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Fresh Enrollment Form") },
                        onClick = {
                            onResetDraft()
                            showMenu = false
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SIMULATE SERVER TRAFFIC",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    DropdownMenuItem(
                        text = { Text("● Normal Traffic (High Speed)") },
                        onClick = {
                            onNetworkModeChange(NetworkMode.OPTIMAL)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("▲ Peak Enrollment Surge (15k users)") },
                        onClick = {
                            onNetworkModeChange(NetworkMode.SURGE)
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("✕ Offline Mode (Test Auto-Save)") },
                        onClick = {
                            onNetworkModeChange(NetworkMode.OFFLINE)
                            showMenu = false
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    DropdownMenuItem(
                        text = { Text("Log Out (Exit Portal)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Log Out",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            onLogout()
                            showMenu = false
                        }
                    )
                }
            }
        )

        // Resilient Auto-Save & Network Status Bar
        Surface(
            color = when (networkMode) {
                NetworkMode.OPTIMAL -> NeustNavyDark
                NetworkMode.SURGE -> Color(0xFF1E293B)
                NetworkMode.OFFLINE -> Color(0xFF450A0A)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = when {
                            networkMode == NetworkMode.OFFLINE -> Icons.Default.CloudOff
                            autoSaveStatus.contains("Saving") -> Icons.Default.Sync
                            else -> Icons.Default.CloudDone
                        },
                        contentDescription = "Status",
                        tint = when {
                            networkMode == NetworkMode.OFFLINE -> Color(0xFFFCA5A5)
                            autoSaveStatus.contains("Saving") -> WarningAmber
                            else -> SuccessGreen
                        },
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = autoSaveStatus,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (networkMode) {
                        NetworkMode.OPTIMAL -> SuccessGreen.copy(alpha = 0.2f)
                        NetworkMode.SURGE -> WarningAmber.copy(alpha = 0.25f)
                        NetworkMode.OFFLINE -> Color.Red.copy(alpha = 0.3f)
                    }
                ) {
                    Text(
                        text = networkMode.label.substringBefore(" "),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = when (networkMode) {
                            NetworkMode.OPTIMAL -> SuccessGreen
                            NetworkMode.SURGE -> WarningAmber
                            NetworkMode.OFFLINE -> Color(0xFFFCA5A5)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
