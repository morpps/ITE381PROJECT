package com.example.ui.screens.steps

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnrollmentDraft
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary

@Composable
fun StepHealthInfo(
    draft: EnrollmentDraft,
    onUpdate: ((EnrollmentDraft) -> EnrollmentDraft) -> Unit,
    modifier: Modifier = Modifier
) {
    val bloodTypes = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
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
                        text = "STEP 3: UNIVERSITY HEALTH SERVICES",
                        color = NeustNavyDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Medical History & Health Declaration",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Required by CHED and NEUST Health Services Division",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Quick Fill Helper Button (Addresses "completing the many required fields takes time")
        OutlinedButton(
            onClick = {
                onUpdate {
                    it.copy(
                        allergies = "None / No known drug allergies",
                        medicalConditions = "Physically Fit, No chronic conditions",
                        emergencyContactPerson = it.guardianName.ifBlank { "Maria G. Vergara (Parent)" },
                        emergencyContactPhone = it.guardianContact.ifBlank { "09171234567" }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("button_fast_health_fill"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeustNavyPrimary)
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = null,
                tint = NeustGold,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Quick Fill: Declare Fit & Clear All Conditions")
        }

        // Blood Type Selection
        Text(
            text = "Student Blood Type",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            bloodTypes.take(4).forEach { bt ->
                FilterChip(
                    selected = draft.bloodType == bt,
                    onClick = { onUpdate { it.copy(bloodType = bt) } },
                    label = { Text(bt) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_blood_$bt")
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            bloodTypes.drop(4).forEach { bt ->
                FilterChip(
                    selected = draft.bloodType == bt,
                    onClick = { onUpdate { it.copy(bloodType = bt) } },
                    label = { Text(bt) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeustNavyPrimary,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("chip_blood_$bt")
                )
            }
        }

        // Allergies
        Text(
            text = "Allergies (Food, Medicines, Environmental)",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        OutlinedTextField(
            value = draft.allergies,
            onValueChange = { value -> onUpdate { it.copy(allergies = value) } },
            label = { Text("Known Allergies") },
            placeholder = { Text("e.g. None or Penicillin, Seafood") },
            leadingIcon = { Icon(Icons.Default.MedicalInformation, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_allergies")
        )

        // Medical Conditions
        Text(
            text = "Existing Medical Conditions / History",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        OutlinedTextField(
            value = draft.medicalConditions,
            onValueChange = { value -> onUpdate { it.copy(medicalConditions = value) } },
            label = { Text("Medical History") },
            placeholder = { Text("e.g. Asthma, Hypertension, None") },
            leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_medical_conditions")
        )

        // Emergency Contact for Clinic
        Text(
            text = "Medical Emergency Contact Person",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        OutlinedTextField(
            value = draft.emergencyContactPerson,
            onValueChange = { value -> onUpdate { it.copy(emergencyContactPerson = value) } },
            label = { Text("Full Name & Relationship *") },
            placeholder = { Text("Maria G. Vergara (Mother)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_emergency_person")
        )

        OutlinedTextField(
            value = draft.emergencyContactPhone,
            onValueChange = { value -> onUpdate { it.copy(emergencyContactPhone = value) } },
            label = { Text("Emergency Contact Number *") },
            placeholder = { Text("09171234567") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_emergency_phone")
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NeustNavyPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "I certify that all health information provided is truthful for campus emergency triage.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
