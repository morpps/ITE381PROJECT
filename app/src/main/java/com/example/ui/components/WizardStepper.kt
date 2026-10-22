package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnrollmentStep
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.theme.SuccessGreen

@Composable
fun WizardStepper(
    currentStep: EnrollmentStep,
    onStepClick: (EnrollmentStep) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val steps = EnrollmentStep.entries
    val progress = (currentStep.index + 1).toFloat() / steps.size.toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STEP ${currentStep.index + 1} OF ${steps.size}: ${currentStep.title.uppercase()}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeustNavyPrimary
                    )
                )
                Text(
                    text = currentStep.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = NeustGold.copy(alpha = 0.2f)
            ) {
                Text(
                    text = "${(progress * 100).toInt()}% Done",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeustNavyDark
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = NeustGold,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Step Pill Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            steps.forEach { step ->
                val isCurrent = step == currentStep
                val isCompleted = step.index < currentStep.index
                val icon = getStepIcon(step)

                StepPill(
                    step = step,
                    isCurrent = isCurrent,
                    isCompleted = isCompleted,
                    icon = icon,
                    onClick = { onStepClick(step) }
                )
            }
        }
    }
}

@Composable
private fun StepPill(
    step: EnrollmentStep,
    isCurrent: Boolean,
    isCompleted: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = when {
            isCurrent -> NeustNavyPrimary
            isCompleted -> SuccessGreen.copy(alpha = 0.15f)
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        },
        modifier = Modifier
            .testTag("step_pill_${step.name.lowercase()}")
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> NeustGold
                            isCompleted -> SuccessGreen
                            else -> Color.Gray.copy(alpha = 0.4f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = step.title,
                        tint = if (isCurrent) NeustNavyDark else Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = step.title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = when {
                    isCurrent -> Color.White
                    isCompleted -> SuccessGreen
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                }
            )
        }
    }
}

private fun getStepIcon(step: EnrollmentStep): ImageVector {
    return when (step) {
        EnrollmentStep.PERSONAL_INFO -> Icons.Default.Person
        EnrollmentStep.ACADEMIC_INFO -> Icons.Default.School
        EnrollmentStep.HEALTH_INFO -> Icons.Default.MedicalServices
        EnrollmentStep.SUBJECT_ENLISTMENT -> Icons.Default.Subject
        EnrollmentStep.DOCUMENTS -> Icons.Default.Description
        EnrollmentStep.REVIEW_SUBMIT -> Icons.Default.FactCheck
    }
}
