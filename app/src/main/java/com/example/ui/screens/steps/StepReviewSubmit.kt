package com.example.ui.screens.steps

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DocumentRequirement
import com.example.data.model.EnrollmentDraft
import com.example.data.model.QueueSimulationState
import com.example.data.model.SubjectItem
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber

@Composable
fun StepReviewSubmit(
    draft: EnrollmentDraft,
    subjects: List<SubjectItem>,
    documents: List<DocumentRequirement>,
    queueState: QueueSimulationState,
    onSubmitToQueue: () -> Unit,
    onViewCOR: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSubjects = subjects.filter { it.isSelected }
    val totalUnits = selectedSubjects.sumOf { it.units }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NeustNavyDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeustGold
                ) {
                    Text(
                        text = "FINAL STAGE: ASSESSMENT & SUBMISSION",
                        color = NeustNavyDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Review & Queue Enrollment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Resilient token-based submission with zero-loss auto-save protection",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // If currently in queue or just completed
        if (queueState.isInQueue) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_queue_waiting_room"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NeustNavyPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NeustGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "VIRTUAL WAITING ROOM ACTIVE",
                                color = NeustGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "Token: ${queueState.ticketNumber}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(NeustGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { queueState.progressPercent },
                            color = NeustGold,
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(68.dp)
                        )
                        Text(
                            text = "#${queueState.position}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Position in Server Queue: #${queueState.position}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    Text(
                        text = "Est. Wait Time: ~${queueState.estimatedWaitSeconds} seconds (Throughput: 150/s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = queueState.queueMessage,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = NeustGold,
                            modifier = Modifier.padding(10.dp),
                            maxLines = 2
                        )
                    }
                }
            }
        }

        // If successfully submitted
        if (draft.isSubmitted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_confirmed_enrollment"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = SuccessGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ENROLLMENT OFFICIALLY CONFIRMED!",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            )
                            Text(
                                text = "Permit Number: ${draft.permitNumber}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onViewCOR,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("button_view_cor"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeustNavyPrimary)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Official Certificate of Registration (COR)")
                    }
                }
            }
        }

        // Comprehensive Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "STUDENT & ACADEMIC SUMMARY",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeustNavyPrimary
                    )
                )

                SummaryRow("Student Name", "${draft.lastName}, ${draft.firstName} ${draft.middleName}")
                SummaryRow("Student Number", draft.studentNumber.ifBlank { "01-2324-047568" })
                SummaryRow("Permit Number", draft.permitNumber.ifBlank { "2155910369207" })
                SummaryRow("Campus", draft.campus)
                SummaryRow("Program", draft.course)
                SummaryRow("Year & Section", "${draft.yearLevel} • ${draft.section}")
                SummaryRow("Term", "${draft.semester}, ${draft.academicYear}")

                Divider(modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = "COURSE ASSESSMENT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeustNavyPrimary
                    )
                )

                SummaryRow("Total Subjects Enlisted", "${selectedSubjects.size} Subjects")
                SummaryRow("Total Academic Units", "$totalUnits Units")
                SummaryRow("Tuition & Laboratory Fee", "₱5,400.00")
                SummaryRow("Registration & Misc. Fee", "₱1,250.00")
                SummaryRow("Gross Assessment", "₱6,650.00")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SuccessGreen.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "RA 10931 Free Higher Ed Subsidy",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    )
                    Text(
                        text = "-₱6,650.00",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL NET AMOUNT DUE",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "₱0.00 (PAID)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    )
                }
            }
        }

        // Primary Submit Action Button
        if (!draft.isSubmitted && !queueState.isInQueue) {
            Button(
                onClick = onSubmitToQueue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("button_submit_enrollment"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeustGold,
                    contentColor = NeustNavyDark
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudSync,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Queue & Submit Enrollment Application",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
