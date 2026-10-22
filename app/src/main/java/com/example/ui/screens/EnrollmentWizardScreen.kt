package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnrollmentStep
import com.example.ui.components.WizardStepper
import com.example.ui.screens.steps.StepAcademicInfo
import com.example.ui.screens.steps.StepDocuments
import com.example.ui.screens.steps.StepHealthInfo
import com.example.ui.screens.steps.StepPersonalInfo
import com.example.ui.screens.steps.StepReviewSubmit
import com.example.ui.screens.steps.StepSubjectEnlistment
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.viewmodel.EnrollmentViewModel

@Composable
fun EnrollmentWizardScreen(
    viewModel: EnrollmentViewModel,
    onNavigateToCOR: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draft by viewModel.draft.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val queueState by viewModel.queueState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Step Stepper
        WizardStepper(
            currentStep = currentStep,
            onStepClick = { step -> viewModel.goToStep(step) }
        )

        // Scrollable Step Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                when (currentStep) {
                    EnrollmentStep.PERSONAL_INFO -> {
                        StepPersonalInfo(
                            draft = draft,
                            onUpdate = { transform -> viewModel.updateDraft(transform) }
                        )
                    }
                    EnrollmentStep.ACADEMIC_INFO -> {
                        StepAcademicInfo(
                            draft = draft,
                            onUpdate = { transform -> viewModel.updateDraft(transform) }
                        )
                    }
                    EnrollmentStep.HEALTH_INFO -> {
                        StepHealthInfo(
                            draft = draft,
                            onUpdate = { transform -> viewModel.updateDraft(transform) }
                        )
                    }
                    EnrollmentStep.SUBJECT_ENLISTMENT -> {
                        StepSubjectEnlistment(
                            subjects = subjects,
                            onToggleSubject = { code -> viewModel.toggleSubject(code) }
                        )
                    }
                    EnrollmentStep.DOCUMENTS -> {
                        StepDocuments(
                            documents = documents,
                            onToggleUpload = { id -> viewModel.toggleDocumentUpload(id) }
                        )
                    }
                    EnrollmentStep.REVIEW_SUBMIT -> {
                        StepReviewSubmit(
                            draft = draft,
                            subjects = subjects,
                            documents = documents,
                            queueState = queueState,
                            onSubmitToQueue = { viewModel.startEnrollmentSubmission() },
                            onViewCOR = onNavigateToCOR
                        )
                    }
                }
            }
        }

        // Sticky Bottom Wizard Controls
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                if (currentStep.index > 0) {
                    OutlinedButton(
                        onClick = { viewModel.prevStep() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("wizard_prev_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Next / Complete Button
                if (currentStep.index < EnrollmentStep.entries.size - 1) {
                    Button(
                        onClick = { viewModel.nextStep() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeustNavyPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("wizard_next_button")
                    ) {
                        Text("Next Step")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else if (!draft.isSubmitted) {
                    Button(
                        onClick = { viewModel.startEnrollmentSubmission() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeustGold,
                            contentColor = NeustNavyDark
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("wizard_submit_button")
                    ) {
                        Text(
                            text = "Submit & Queue",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onNavigateToCOR,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeustNavyPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("wizard_view_cor_button")
                    ) {
                        Text("View Assessment Slip")
                    }
                }
            }
        }
    }
}
