package com.example.ui.screens.steps

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DocumentRequirement
import com.example.ui.theme.NeustGold
import com.example.ui.theme.NeustNavyDark
import com.example.ui.theme.NeustNavyPrimary
import com.example.ui.theme.SuccessGreen

@Composable
fun StepDocuments(
    documents: List<DocumentRequirement>,
    onToggleUpload: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uploadedCount = documents.count { it.isUploaded }
    val totalCount = documents.size

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
                        text = "STEP 5: ADMISSION CREDENTIALS",
                        color = NeustNavyDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Registrar Document Checklist",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = "Uploaded: $uploadedCount of $totalCount required credentials",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Quick verify button
        OutlinedButton(
            onClick = {
                documents.forEach { doc ->
                    if (!doc.isUploaded) onToggleUpload(doc.id)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("button_upload_all_docs"),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeustNavyPrimary)
        ) {
            Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Auto-Attach All Verified Digital Documents")
        }

        Text(
            text = "Required Admission Documents",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = NeustNavyPrimary
        )

        documents.forEach { doc ->
            DocumentItemCard(
                doc = doc,
                onClick = { onToggleUpload(doc.id) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DocumentItemCard(
    doc: DocumentRequirement,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("doc_card_${doc.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (doc.isUploaded) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (doc.isUploaded) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (doc.isUploaded) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = if (doc.isUploaded) "Uploaded" else "Missing",
                tint = if (doc.isUploaded) SuccessGreen else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = doc.name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeustNavyPrimary
                    )

                    if (doc.isMandatory) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = NeustGold.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "MANDATORY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeustNavyDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = doc.description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (doc.isUploaded && doc.fileName != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.InsertDriveFile,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = doc.fileName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = SuccessGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (doc.isUploaded) {
                    SuccessGreen.copy(alpha = 0.15f)
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                }
            ) {
                Icon(
                    imageVector = if (doc.isUploaded) Icons.Default.UploadFile else Icons.Default.CloudUpload,
                    contentDescription = "Upload status",
                    tint = if (doc.isUploaded) SuccessGreen else NeustNavyPrimary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(18.dp)
                )
            }
        }
    }
}
