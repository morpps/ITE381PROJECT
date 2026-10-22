package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.EnrollmentViewModel

@Composable
fun AssessmentSlipScreen(
    viewModel: EnrollmentViewModel,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.draft.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val selectedSubjects = subjects.filter { it.isSelected }
    val totalUnits = selectedSubjects.sumOf { it.units }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Actions Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Official Permit & Assessment",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = NeustNavyPrimary
                )
                Text(
                    text = "Authenticated Certificate of Registration",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SuccessGreen.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = SuccessGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VALIDATED",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "Certificate of Registration downloaded to storage", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_download_cor"),
                colors = ButtonDefaults.buttonColors(containerColor = NeustNavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save PDF", fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Permit ready for wireless printer", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("button_print_permit"),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Print", fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Sharing permit verification link", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
            }
        }

        // Official Academic Slip Card (Document-Matched Style)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, NeustNavyPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .testTag("cor_document_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Document Header (Matches PDF Page 1 & 5!)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NUEVA ECIJA UNIVERSITY OF SCIENCE AND TECHNOLOGY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = NeustNavyPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "PHINMA Araullo University - South • College of Information Technology",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Bachelor of Science in Information Technology",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "S.Y. 2026-2027 | First Semester",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeustNavyPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Divider(color = Color.Black, thickness = 1.dp)

                // Student Meta Header (Matches page 5: Name, Permit Number, Date, Ref No, etc.)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1.3f)) {
                        SlipField("Name", "${draft.lastName.uppercase()}, ${draft.firstName.uppercase()} ${draft.middleName.uppercase()}")
                        SlipField("Student No.", draft.studentNumber.ifBlank { "01-2324-047568" })
                        SlipField("Course", "BSc (IT) 4")
                        SlipField("Section", draft.section.ifBlank { "AU1S2627-BSIT4-S1-CS" })
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        SlipField("Permit Number", draft.permitNumber.ifBlank { "2155910369207" }, isBold = true)
                        SlipField("Ref No.", draft.referenceNumber.ifBlank { "2155910369207" })
                        SlipField("Date", "17/8/2026")
                        SlipField("Status", "ENROLLED (FREE)", isBold = true, highlight = true)
                    }
                }

                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 0.8.dp)

                // Enlisted Subjects Table (Exact match to page 5 table)
                Text(
                    text = "ENLISTED SUBJECTS & SCHEDULE (PERIOD 1)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeustNavyPrimary
                    )
                )

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subject", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(0.9f), color = Color.Black)
                    Text("Section", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.4f), color = Color.Black)
                    Text("Block", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.1f), color = Color.Black)
                    Text("Faculty", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1.3f), color = Color.Black)
                    Text("Sign", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(0.6f), color = Color.Black, textAlign = TextAlign.End)
                }

                // Table Rows
                selectedSubjects.forEach { sub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(0.9f)) {
                            Text(sub.code, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NeustNavyPrimary)
                            Text("${sub.units}.0 u", fontSize = 9.sp, color = Color.DarkGray)
                        }
                        Text(sub.section, fontSize = 9.sp, modifier = Modifier.weight(1.4f), color = Color.Black)
                        Text(sub.block, fontSize = 9.sp, modifier = Modifier.weight(1.1f), color = Color.DarkGray)
                        Text(sub.faculty, fontSize = 9.sp, modifier = Modifier.weight(1.3f), color = Color.Black)
                        Text("______", fontSize = 9.sp, modifier = Modifier.weight(0.6f), color = Color.LightGray, textAlign = TextAlign.End)
                    }
                    Divider(color = Color(0xFFF1F5F9), thickness = 0.5.dp)
                }

                // Assessment Breakdown
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Enlisted Units:", fontSize = 11.sp, color = Color.Black)
                            Text("$totalUnits.0 Units", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Assessed Tuition & Lab Fees:", fontSize = 11.sp, color = Color.DarkGray)
                            Text("₱7,500.00", fontSize = 11.sp, color = Color.DarkGray)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Registration & School ID Fee:", fontSize = 11.sp, color = Color.DarkGray)
                            Text("₱1,250.00", fontSize = 11.sp, color = Color.DarkGray)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("RA 10931 FHE Act Exemption:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            Text("-₱8,750.00", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                        Divider(modifier = Modifier.padding(vertical = 2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("NET STUDENT BALANCE:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeustNavyPrimary)
                            Text("₱0.00 (PAID / FREE)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                    }
                }

                // Authentication Stamp & Barcode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "OFFICIAL REGISTRAR WATERMARK",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            color = NeustNavyPrimary
                        )
                        Text(
                            text = "Verified: ${draft.permitNumber.ifBlank { "2155910369207" }}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            ),
                            color = Color.DarkGray
                        )
                    }

                    // Barcode representation
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(30.dp)
                            .background(Color.White)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val barWidth = 3f
                            var x = 0f
                            val pattern = listOf(true, false, true, true, false, true, false, false, true, true, false, true, true, false)
                            while (x < size.width) {
                                val isBar = pattern[((x / barWidth).toInt()) % pattern.size]
                                if (isBar) {
                                    drawLine(
                                        color = Color.Black,
                                        start = Offset(x, 0f),
                                        end = Offset(x, size.height),
                                        strokeWidth = 2f
                                    )
                                }
                                x += barWidth + 1f
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SlipField(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (highlight) SuccessGreen else Color.Black
        )
    }
}
