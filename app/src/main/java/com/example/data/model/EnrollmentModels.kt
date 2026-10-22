package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "enrollment_drafts")
data class EnrollmentDraft(
    @PrimaryKey
    val id: Int = 1,
    val studentNumber: String = "",
    val permitNumber: String = "",
    val referenceNumber: String = "",
    val lastName: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "Male",
    val civilStatus: String = "Single",
    val contactNumber: String = "",
    val email: String = "",
    val address: String = "",
    val guardianName: String = "",
    val guardianContact: String = "",
    val campus: String = "Cabanatuan Main Campus",
    val college: String = "College of Information Technology (CIT)",
    val course: String = "Bachelor of Science in Information Technology (BSIT)",
    val yearLevel: String = "4th Year",
    val semester: String = "1st Semester",
    val academicYear: String = "S.Y. 2026-2027",
    val studentType: String = "Regular",
    val section: String = "AU1S2627-BSIT4-S1-CS",
    val bloodType: String = "O+",
    val allergies: String = "None",
    val medicalConditions: String = "None",
    val emergencyContactPerson: String = "",
    val emergencyContactPhone: String = "",
    val selectedSubjectsJson: String = "",
    val uploadedDocumentsJson: String = "",
    val currentStep: Int = 0,
    val lastSavedTimestamp: Long = System.currentTimeMillis(),
    val status: String = "DRAFT", // DRAFT, IN_QUEUE, CONFIRMED
    val queueTicket: String = "",
    val queuePosition: Int = 0,
    val isSubmitted: Boolean = false,
    val totalAssessmentUnits: Int = 18,
    val tuitionAssessed: Double = 0.0,
    val freeTuitionCovered: Boolean = true
)

data class SubjectItem(
    val code: String,
    val title: String,
    val units: Int,
    val section: String,
    val block: String,
    val faculty: String,
    val schedule: String,
    val room: String,
    val isSelected: Boolean = true
)

data class DocumentRequirement(
    val id: String,
    val name: String,
    val description: String,
    val isMandatory: Boolean,
    val isUploaded: Boolean,
    val fileName: String? = null
)

enum class EnrollmentStep(val index: Int, val title: String, val subtitle: String) {
    PERSONAL_INFO(0, "Personal", "Basic Details"),
    ACADEMIC_INFO(1, "Academic", "Program & Campus"),
    HEALTH_INFO(2, "Health", "Medical Record"),
    SUBJECT_ENLISTMENT(3, "Subjects", "Course Load"),
    DOCUMENTS(4, "Docs", "Requirements"),
    REVIEW_SUBMIT(5, "Confirm", "Queue & Assessment")
}

data class QueueSimulationState(
    val isInQueue: Boolean = false,
    val ticketNumber: String = "",
    val position: Int = 0,
    val totalInQueue: Int = 0,
    val estimatedWaitSeconds: Int = 0,
    val progressPercent: Float = 0f,
    val queueMessage: String = "",
    val isCompleted: Boolean = false
)

enum class NetworkMode(val label: String, val description: String) {
    OPTIMAL("Normal (High Speed)", "Standard 1Gbps university connection"),
    SURGE("Peak Traffic Surge", "Simulates 15,000+ simultaneous students entering waiting room"),
    OFFLINE("Offline / Connection Drop", "Simulates network crash: all form inputs auto-saved to Room DB")
}
