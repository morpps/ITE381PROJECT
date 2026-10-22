package com.example.ui.screens.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnrollmentDraft
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyLight
import com.example.ui.theme.NeustNavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepAcademicInfo(
    draft: EnrollmentDraft,
    onUpdate: ((EnrollmentDraft) -> EnrollmentDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    var campusExpanded by remember { mutableStateOf(false) }
    var collegeExpanded by remember { mutableStateOf(false) }
    var courseExpanded by remember { mutableStateOf(false) }

    val campuses = listOf(
        "Cabanatuan Main Campus",
        "General Tinio Campus",
        "San Isidro Campus",
        "Atate Campus, Palayan City",
        "Fort Magsaysay Campus",
        "Sumacab Campus",
        "Talavera Off-Campus"
    )

    val colleges = listOf(
        "College of Information Technology (CIT)",
        "College of Engineering (COE)",
        "College of Education (CED)",
        "College of Management and Business Technology (CMBT)",
        "College of Nursing (CON)",
        "College of Criminology (COC)"
    )

    val courses = listOf(
        "Bachelor of Science in Information Technology (BSIT)",
        "Bachelor of Science in Computer Science (BSCS)",
        "Bachelor of Science in Civil Engineering (BSCE)",
        "Bachelor of Science in Business Administration (BSBA)",
        "Bachelor of Secondary Education (BSED)",
        "Bachelor of Science in Hospitality Management (BSHM)"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NeustNavyPrimary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeustGold
                ) {
                    Text(
                        text = "STEP 2: CURRICULAR PROGRAM",
                        color = NeustNavyDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Academic Details & Campus Assignment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "School Year 2026-2027 • First Semester",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Campus Selection
        Text(
            text = "NEUST Campus Location",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        ExposedDropdownMenuBox(
            expanded = campusExpanded,
            onExpandedChange = { campusExpanded = !campusExpanded }
        ) {
            OutlinedTextField(
                value = draft.campus,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Campus *") },
                leadingIcon = { Icon(Icons.Default.Domain, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = campusExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("dropdown_campus")
            )
            ExposedDropdownMenu(
                expanded = campusExpanded,
                onDismissRequest = { campusExpanded = false }
            ) {
                campuses.forEach { campus ->
                    DropdownMenuItem(
                        text = { Text(campus) },
                        onClick = {
                            onUpdate { it.copy(campus = campus) }
                            campusExpanded = false
                        }
                    )
                }
            }
        }

        // College
        Text(
            text = "Academic College",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        ExposedDropdownMenuBox(
            expanded = collegeExpanded,
            onExpandedChange = { collegeExpanded = !collegeExpanded }
        ) {
            OutlinedTextField(
                value = draft.college,
                onValueChange = {},
                readOnly = true,
                label = { Text("College Department *") },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = collegeExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("dropdown_college")
            )
            ExposedDropdownMenu(
                expanded = collegeExpanded,
                onDismissRequest = { collegeExpanded = false }
            ) {
                colleges.forEach { college ->
                    DropdownMenuItem(
                        text = { Text(college) },
                        onClick = {
                            onUpdate { it.copy(college = college) }
                            collegeExpanded = false
                        }
                    )
                }
            }
        }

        // Degree Course
        Text(
            text = "Degree Program / Major",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        ExposedDropdownMenuBox(
            expanded = courseExpanded,
            onExpandedChange = { courseExpanded = !courseExpanded }
        ) {
            OutlinedTextField(
                value = draft.course,
                onValueChange = {},
                readOnly = true,
                label = { Text("Degree Course *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("dropdown_course")
            )
            ExposedDropdownMenu(
                expanded = courseExpanded,
                onDismissRequest = { courseExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course) },
                        onClick = {
                            onUpdate { it.copy(course = course) }
                            courseExpanded = false
                        }
                    )
                }
            }
        }

        // Year Level Chips
        Text(
            text = "Year Level",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1st Year", "2nd Year", "3rd Year", "4th Year").forEach { yr ->
                FilterChip(
                    selected = draft.yearLevel == yr,
                    onClick = { onUpdate { it.copy(yearLevel = yr) } },
                    label = { Text(yr) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_year_$yr")
                )
            }
        }

        // Student Type (Regular / Irregular / Transferee)
        Text(
            text = "Student Classification",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Regular", "Irregular", "Transferee").forEach { type ->
                FilterChip(
                    selected = draft.studentType == type,
                    onClick = { onUpdate { it.copy(studentType = type) } },
                    label = { Text(type) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyLight,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_student_type_$type")
                )
            }
        }

        // Section & Block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft.section,
                onValueChange = { value -> onUpdate { it.copy(section = value) } },
                label = { Text("Assigned Section Block") },
                placeholder = { Text("AU1S2627-BSIT4-S1-CS") },
                leadingIcon = { Icon(Icons.Default.Class, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_section")
            )

            OutlinedTextField(
                value = draft.semester,
                onValueChange = { value -> onUpdate { it.copy(semester = value) } },
                label = { Text("Semester") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .weight(0.8f)
                    .testTag("input_semester")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
