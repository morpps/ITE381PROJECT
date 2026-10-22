package com.example.ui.screens.steps

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EnrollmentDraft
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyLight
import com.example.ui.theme.NeustNavyPrimary

@Composable
fun StepPersonalInfo(
    draft: EnrollmentDraft,
    onUpdate: ((EnrollmentDraft) -> EnrollmentDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card with NEUST Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NeustNavyDark)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.neust_campus_hero_1792641532266),
                    contentDescription = "NEUST Admission Campus",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, NeustNavyDark.copy(alpha = 0.92f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = NeustGold
                    ) {
                        Text(
                            text = "STEP 1: STUDENT IDENTIFICATION",
                            color = NeustNavyDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Personal & Contact Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }

        // Notice: Auto-save guarantee
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Safe",
                    tint = NeustNavyPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Zero-loss guarantee: Every keystroke is saved immediately to device storage.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Student Identifiers
        Text(
            text = "Official Identification Numbers",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft.studentNumber,
                onValueChange = { value -> onUpdate { it.copy(studentNumber = value) } },
                label = { Text("Student Number *") },
                placeholder = { Text("01-2324-047568") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_student_number")
            )

            OutlinedTextField(
                value = draft.permitNumber,
                onValueChange = { value -> onUpdate { it.copy(permitNumber = value) } },
                label = { Text("Permit Number") },
                placeholder = { Text("2155910369207") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_permit_number")
            )
        }

        // Name Fields
        Text(
            text = "Student Full Name",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        OutlinedTextField(
            value = draft.lastName,
            onValueChange = { value -> onUpdate { it.copy(lastName = value) } },
            label = { Text("Last Name *") },
            placeholder = { Text("Vergara") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_last_name")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft.firstName,
                onValueChange = { value -> onUpdate { it.copy(firstName = value) } },
                label = { Text("First Name *") },
                placeholder = { Text("Jubert") },
                singleLine = true,
                modifier = Modifier
                    .weight(1.4f)
                    .testTag("input_first_name")
            )

            OutlinedTextField(
                value = draft.middleName,
                onValueChange = { value -> onUpdate { it.copy(middleName = value) } },
                label = { Text("M.I.") },
                placeholder = { Text("G.") },
                singleLine = true,
                modifier = Modifier
                    .weight(0.6f)
                    .testTag("input_middle_name")
            )
        }

        // Gender & Civil Status
        Text(
            text = "Gender & Civil Status",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Male", "Female").forEach { g ->
                FilterChip(
                    selected = draft.gender == g,
                    onClick = { onUpdate { it.copy(gender = g) } },
                    label = { Text(g) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_gender_$g")
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            listOf("Single", "Married").forEach { cs ->
                FilterChip(
                    selected = draft.civilStatus == cs,
                    onClick = { onUpdate { it.copy(civilStatus = cs) } },
                    label = { Text(cs) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyLight,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_status_$cs")
                )
            }
        }

        // Birthdate & Contact Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft.birthDate,
                onValueChange = { value -> onUpdate { it.copy(birthDate = value) } },
                label = { Text("Birthdate (YYYY-MM-DD)") },
                placeholder = { Text("2003-08-17") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_birthdate")
            )

            OutlinedTextField(
                value = draft.contactNumber,
                onValueChange = { value -> onUpdate { it.copy(contactNumber = value) } },
                label = { Text("Contact Number *") },
                placeholder = { Text("09958454214") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_contact_number")
            )
        }

        OutlinedTextField(
            value = draft.email,
            onValueChange = { value -> onUpdate { it.copy(email = value) } },
            label = { Text("Student Email *") },
            placeholder = { Text("vergara.jubert@neust.edu.ph") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_email")
        )

        OutlinedTextField(
            value = draft.address,
            onValueChange = { value -> onUpdate { it.copy(address = value) } },
            label = { Text("Residential Address (Nueva Ecija)") },
            placeholder = { Text("Mabangit, Cabanatuan City, Nueva Ecija") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            singleLine = false,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_address")
        )

        // Guardian / Emergency Contact
        Text(
            text = "Parent / Guardian Contact",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = draft.guardianName,
                onValueChange = { value -> onUpdate { it.copy(guardianName = value) } },
                label = { Text("Guardian Name") },
                placeholder = { Text("Maria G. Vergara") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_guardian_name")
            )

            OutlinedTextField(
                value = draft.guardianContact,
                onValueChange = { value -> onUpdate { it.copy(guardianContact = value) } },
                label = { Text("Guardian Phone") },
                placeholder = { Text("09171234567") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_guardian_phone")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
