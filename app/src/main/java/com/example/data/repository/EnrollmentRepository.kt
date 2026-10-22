package com.example.data.repository

import com.example.data.local.EnrollmentDao
import com.example.data.model.DocumentRequirement
import com.example.data.model.EnrollmentDraft
import com.example.data.model.SubjectItem
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class EnrollmentRepository(private val dao: EnrollmentDao) {

    val draftFlow: Flow<EnrollmentDraft?> = dao.getDraft()

    suspend fun getDraftOnce(): EnrollmentDraft? = dao.getDraftOnce()

    suspend fun saveDraft(draft: EnrollmentDraft) {
        val updatedDraft = draft.copy(
            lastSavedTimestamp = System.currentTimeMillis()
        )
        dao.insertOrUpdate(updatedDraft)
    }

    suspend fun clearDraft() {
        dao.clearDraft()
    }

    fun getDefaultSubjects(): List<SubjectItem> {
        return listOf(
            SubjectItem(
                code = "ITE 367",
                title = "Advanced Web Systems and Mobile Integration",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 208 Phase 2",
                faculty = "Isaiah Joshua Mizona",
                schedule = "Mon/Wed 8:00 AM - 10:30 AM",
                room = "Room 208 Phase 2",
                isSelected = true
            ),
            SubjectItem(
                code = "ITE 381",
                title = "IT Business Solutions & Architecture",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 402 Phase 3",
                faculty = "Aira P. Lucero",
                schedule = "Tue/Thu 10:30 AM - 1:00 PM",
                room = "Room 402 Phase 3",
                isSelected = true
            ),
            SubjectItem(
                code = "BAM 285",
                title = "Strategic Business Management in IT",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 404 Phase 3,204",
                faculty = "April Jane Larquerta Santos",
                schedule = "Fri 8:00 AM - 1:00 PM",
                room = "Room 404 Phase 3",
                isSelected = true
            ),
            SubjectItem(
                code = "ITE 310",
                title = "Information Assurance and Cybersecurity",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 403 Phase 3",
                faculty = "Aira P. Lucero",
                schedule = "Mon/Wed 1:00 PM - 3:30 PM",
                room = "Room 403 Phase 3",
                isSelected = true
            ),
            SubjectItem(
                code = "CAP 401",
                title = "IT Capstone Project & Research II",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 405 Phase 3",
                faculty = "Engr. Marco D. Ramos",
                schedule = "Sat 9:00 AM - 12:00 PM",
                room = "CIT Lab 3",
                isSelected = true
            ),
            SubjectItem(
                code = "SYS 320",
                title = "Cloud Infrastructure & Systems Administration",
                units = 3,
                section = "AU1S2627-BSIT4-S1-CS",
                block = "Block 401 Phase 3",
                faculty = "Prof. Ronald Cruz",
                schedule = "Thu 2:00 PM - 5:00 PM",
                room = "Server Lab A",
                isSelected = true
            )
        )
    }

    fun getDefaultDocuments(): List<DocumentRequirement> {
        return listOf(
            DocumentRequirement(
                id = "doc_psa",
                name = "PSA Birth Certificate",
                description = "Clear scanned copy from Philippine Statistics Authority",
                isMandatory = true,
                isUploaded = true,
                fileName = "PSA_BirthCert_Verified.pdf"
            ),
            DocumentRequirement(
                id = "doc_f137",
                name = "Transcript of Records / Form 137",
                description = "Certified true copy of previous grades and curriculum evaluation",
                isMandatory = true,
                isUploaded = true,
                fileName = "Official_TOR_Eval.pdf"
            ),
            DocumentRequirement(
                id = "doc_moral",
                name = "Certificate of Good Moral Character",
                description = "Issued by Dean or Prefect of Student Affairs",
                isMandatory = true,
                isUploaded = true,
                fileName = "Good_Moral_NEUST.pdf"
            ),
            DocumentRequirement(
                id = "doc_med",
                name = "NEUST University Clinic Medical Clearance",
                description = "Chest X-Ray, Blood Typing and General Health Assessment",
                isMandatory = true,
                isUploaded = true,
                fileName = "Clinic_Clearance_2026.pdf"
            ),
            DocumentRequirement(
                id = "doc_photo",
                name = "2x2 Formal ID Photo (White Background)",
                description = "Recent studio photograph wearing university polo/blouse",
                isMandatory = true,
                isUploaded = true,
                fileName = "J_Vergara_2x2_ID.jpg"
            )
        )
    }

    fun getSampleStudentVergara(): EnrollmentDraft {
        val subjects = getDefaultSubjects()
        val subjectsJson = serializeSubjects(subjects)
        val docs = getDefaultDocuments()
        val docsJson = serializeDocuments(docs)

        return EnrollmentDraft(
            id = 1,
            studentNumber = "01-2324-047568",
            permitNumber = "2155910369207",
            referenceNumber = "REF-2026-NEUST-98214",
            lastName = "Vergara",
            firstName = "Jubert",
            middleName = "G.",
            birthDate = "2003-08-17",
            gender = "Male",
            civilStatus = "Single",
            contactNumber = "09958454214",
            email = "vergara.jubert@neust.edu.ph",
            address = "Mabangit, Cabanatuan City, Nueva Ecija",
            guardianName = "Maria G. Vergara",
            guardianContact = "09171234567",
            campus = "Cabanatuan Main Campus",
            college = "College of Information Technology (CIT)",
            course = "Bachelor of Science in Information Technology (BSIT)",
            yearLevel = "4th Year",
            semester = "1st Semester",
            academicYear = "S.Y. 2026-2027",
            studentType = "Regular",
            section = "AU1S2627-BSIT4-S1-CS",
            bloodType = "O+",
            allergies = "None",
            medicalConditions = "None",
            emergencyContactPerson = "Maria G. Vergara (Mother)",
            emergencyContactPhone = "09171234567",
            selectedSubjectsJson = subjectsJson,
            uploadedDocumentsJson = docsJson,
            currentStep = 0,
            lastSavedTimestamp = System.currentTimeMillis(),
            status = "DRAFT",
            totalAssessmentUnits = 18,
            tuitionAssessed = 0.0,
            freeTuitionCovered = true
        )
    }

    fun getSampleStudentVillaluz(): EnrollmentDraft {
        val subjects = getDefaultSubjects()
        val subjectsJson = serializeSubjects(subjects)
        val docs = getDefaultDocuments()
        val docsJson = serializeDocuments(docs)

        return EnrollmentDraft(
            id = 1,
            studentNumber = "01-2324-46809",
            permitNumber = "2155910369208",
            referenceNumber = "REF-2026-NEUST-98215",
            lastName = "Villaluz",
            firstName = "Richmond",
            middleName = "A.",
            birthDate = "2003-11-24",
            gender = "Male",
            civilStatus = "Single",
            contactNumber = "09508144661",
            email = "villaluz.richmond@neust.edu.ph",
            address = "Sumacab Este, Cabanatuan City, Nueva Ecija",
            guardianName = "Eduardo Villaluz",
            guardianContact = "09209876543",
            campus = "Cabanatuan Main Campus",
            college = "College of Information Technology (CIT)",
            course = "Bachelor of Science in Information Technology (BSIT)",
            yearLevel = "4th Year",
            semester = "1st Semester",
            academicYear = "S.Y. 2026-2027",
            studentType = "Regular",
            section = "AU1S2627-BSIT4-S1-CS",
            bloodType = "B+",
            allergies = "Penicillin",
            medicalConditions = "None",
            emergencyContactPerson = "Eduardo Villaluz (Father)",
            emergencyContactPhone = "09209876543",
            selectedSubjectsJson = subjectsJson,
            uploadedDocumentsJson = docsJson,
            currentStep = 0,
            lastSavedTimestamp = System.currentTimeMillis(),
            status = "DRAFT",
            totalAssessmentUnits = 18,
            tuitionAssessed = 0.0,
            freeTuitionCovered = true
        )
    }

    fun serializeSubjects(subjects: List<SubjectItem>): String {
        val array = JSONArray()
        for (item in subjects) {
            val obj = JSONObject().apply {
                put("code", item.code)
                put("title", item.title)
                put("units", item.units)
                put("section", item.section)
                put("block", item.block)
                put("faculty", item.faculty)
                put("schedule", item.schedule)
                put("room", item.room)
                put("isSelected", item.isSelected)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun deserializeSubjects(json: String?): List<SubjectItem> {
        if (json.isNullOrBlank()) return getDefaultSubjects()
        return try {
            val array = JSONArray(json)
            val list = mutableListOf<SubjectItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    SubjectItem(
                        code = obj.optString("code", ""),
                        title = obj.optString("title", ""),
                        units = obj.optInt("units", 3),
                        section = obj.optString("section", ""),
                        block = obj.optString("block", ""),
                        faculty = obj.optString("faculty", ""),
                        schedule = obj.optString("schedule", ""),
                        room = obj.optString("room", ""),
                        isSelected = obj.optBoolean("isSelected", true)
                    )
                )
            }
            list
        } catch (e: Exception) {
            getDefaultSubjects()
        }
    }

    fun serializeDocuments(docs: List<DocumentRequirement>): String {
        val array = JSONArray()
        for (doc in docs) {
            val obj = JSONObject().apply {
                put("id", doc.id)
                put("name", doc.name)
                put("description", doc.description)
                put("isMandatory", doc.isMandatory)
                put("isUploaded", doc.isUploaded)
                put("fileName", doc.fileName ?: "")
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun deserializeDocuments(json: String?): List<DocumentRequirement> {
        if (json.isNullOrBlank()) return getDefaultDocuments()
        return try {
            val array = JSONArray(json)
            val list = mutableListOf<DocumentRequirement>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    DocumentRequirement(
                        id = obj.optString("id", ""),
                        name = obj.optString("name", ""),
                        description = obj.optString("description", ""),
                        isMandatory = obj.optBoolean("isMandatory", true),
                        isUploaded = obj.optBoolean("isUploaded", false),
                        fileName = obj.optString("fileName").takeIf { it.isNotBlank() }
                    )
                )
            }
            list
        } catch (e: Exception) {
            getDefaultDocuments()
        }
    }
}
