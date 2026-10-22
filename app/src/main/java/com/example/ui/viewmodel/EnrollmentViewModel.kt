package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.DocumentRequirement
import com.example.data.model.EnrollmentDraft
import com.example.data.model.EnrollmentStep
import com.example.data.model.NetworkMode
import com.example.data.model.QueueSimulationState
import com.example.data.model.SubjectItem
import com.example.data.repository.EnrollmentRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class EnrollmentViewModel(
    private val repository: EnrollmentRepository
) : ViewModel() {

    private val _draft = MutableStateFlow(repository.getSampleStudentVergara())
    val draft: StateFlow<EnrollmentDraft> = _draft.asStateFlow()

    private val _subjects = MutableStateFlow<List<SubjectItem>>(repository.getDefaultSubjects())
    val subjects: StateFlow<List<SubjectItem>> = _subjects.asStateFlow()

    private val _documents = MutableStateFlow<List<DocumentRequirement>>(repository.getDefaultDocuments())
    val documents: StateFlow<List<DocumentRequirement>> = _documents.asStateFlow()

    private val _currentStep = MutableStateFlow(EnrollmentStep.PERSONAL_INFO)
    val currentStep: StateFlow<EnrollmentStep> = _currentStep.asStateFlow()

    private val _autoSaveStatus = MutableStateFlow("Draft loaded from local Room DB")
    val autoSaveStatus: StateFlow<String> = _autoSaveStatus.asStateFlow()

    private val _lastSavedTime = MutableStateFlow(System.currentTimeMillis())
    val lastSavedTime: StateFlow<Long> = _lastSavedTime.asStateFlow()

    private val _networkMode = MutableStateFlow(NetworkMode.OPTIMAL)
    val networkMode: StateFlow<NetworkMode> = _networkMode.asStateFlow()

    private val _queueState = MutableStateFlow(QueueSimulationState())
    val queueState: StateFlow<QueueSimulationState> = _queueState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAuthenticating = MutableStateFlow(false)
    val isAuthenticating: StateFlow<Boolean> = _isAuthenticating.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private var autoSaveJob: Job? = null
    private var queueSimulationJob: Job? = null

    init {
        viewModelScope.launch {
            val existing = repository.getDraftOnce()
            if (existing != null) {
                _draft.value = existing
                _subjects.value = repository.deserializeSubjects(existing.selectedSubjectsJson)
                _documents.value = repository.deserializeDocuments(existing.uploadedDocumentsJson)
                val stepIndex = existing.currentStep.coerceIn(0, EnrollmentStep.entries.size - 1)
                _currentStep.value = EnrollmentStep.entries[stepIndex]
                _lastSavedTime.value = existing.lastSavedTimestamp
                _autoSaveStatus.value = "Restored draft from device database"
            } else {
                // Preload sample student Jubert Vergara as initial draft
                val initial = repository.getSampleStudentVergara()
                repository.saveDraft(initial)
                _draft.value = initial
            }
        }
    }

    fun updateDraft(transform: (EnrollmentDraft) -> EnrollmentDraft) {
        val updated = transform(_draft.value)
        _draft.value = updated
        triggerDebouncedAutoSave()
    }

    private fun triggerDebouncedAutoSave() {
        _autoSaveStatus.value = "Saving draft..."
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(500) // 500ms debounce
            persistDraftInternal()
        }
    }

    private suspend fun persistDraftInternal() {
        val currentSubjects = _subjects.value
        val currentDocs = _documents.value
        val totalUnits = currentSubjects.filter { it.isSelected }.sumOf { it.units }

        val toSave = _draft.value.copy(
            selectedSubjectsJson = repository.serializeSubjects(currentSubjects),
            uploadedDocumentsJson = repository.serializeDocuments(currentDocs),
            currentStep = _currentStep.value.index,
            lastSavedTimestamp = System.currentTimeMillis(),
            totalAssessmentUnits = totalUnits
        )
        repository.saveDraft(toSave)
        _lastSavedTime.value = System.currentTimeMillis()
        val timeStr = SimpleDateFormat("h:mm:ss a", Locale.getDefault()).format(Date())
        _autoSaveStatus.value = "Auto-saved to device at $timeStr"
    }

    fun goToStep(step: EnrollmentStep) {
        _currentStep.value = step
        updateDraft { it.copy(currentStep = step.index) }
    }

    fun nextStep() {
        val currentIndex = _currentStep.value.index
        if (currentIndex < EnrollmentStep.entries.size - 1) {
            val next = EnrollmentStep.entries[currentIndex + 1]
            goToStep(next)
        }
    }

    fun prevStep() {
        val currentIndex = _currentStep.value.index
        if (currentIndex > 0) {
            val prev = EnrollmentStep.entries[currentIndex - 1]
            goToStep(prev)
        }
    }

    fun toggleSubject(code: String) {
        _subjects.update { list ->
            list.map { if (it.code == code) it.copy(isSelected = !it.isSelected) else it }
        }
        triggerDebouncedAutoSave()
    }

    fun toggleDocumentUpload(id: String) {
        _documents.update { list ->
            list.map {
                if (it.id == id) {
                    val newUploaded = !it.isUploaded
                    it.copy(
                        isUploaded = newUploaded,
                        fileName = if (newUploaded) "${it.name.replace(" ", "_")}_verified.pdf" else null
                    )
                } else it
            }
        }
        triggerDebouncedAutoSave()
    }

    fun setNetworkMode(mode: NetworkMode) {
        _networkMode.value = mode
        when (mode) {
            NetworkMode.OFFLINE -> _autoSaveStatus.value = "Offline Mode: Full auto-save active locally"
            NetworkMode.SURGE -> _autoSaveStatus.value = "Surge Mode: Virtual waiting room queue enabled"
            NetworkMode.OPTIMAL -> _autoSaveStatus.value = "Connected: High-speed cloud sync"
        }
    }

    fun loadPresetStudent(preset: String) {
        viewModelScope.launch {
            val draftData = if (preset == "VILLALUZ") {
                repository.getSampleStudentVillaluz()
            } else {
                repository.getSampleStudentVergara()
            }
            _draft.value = draftData
            _subjects.value = repository.deserializeSubjects(draftData.selectedSubjectsJson)
            _documents.value = repository.deserializeDocuments(draftData.uploadedDocumentsJson)
            _currentStep.value = EnrollmentStep.PERSONAL_INFO
            persistDraftInternal()
            _autoSaveStatus.value = "Loaded preset: ${draftData.firstName} ${draftData.lastName}"
        }
    }

    fun resetDraft() {
        viewModelScope.launch {
            val blank = EnrollmentDraft(
                id = 1,
                studentNumber = "01-2627-${Random.nextInt(10000, 99999)}",
                permitNumber = "21559103${Random.nextInt(10000, 99999)}",
                referenceNumber = "REF-2026-NEUST-${Random.nextInt(10000, 99999)}",
                selectedSubjectsJson = repository.serializeSubjects(repository.getDefaultSubjects()),
                uploadedDocumentsJson = repository.serializeDocuments(repository.getDefaultDocuments()),
                currentStep = 0
            )
            _draft.value = blank
            _subjects.value = repository.getDefaultSubjects()
            _documents.value = repository.getDefaultDocuments()
            _currentStep.value = EnrollmentStep.PERSONAL_INFO
            persistDraftInternal()
            _autoSaveStatus.value = "Started fresh enrollment form"
        }
    }

    fun startEnrollmentSubmission() {
        queueSimulationJob?.cancel()
        val randomTicket = "NEUST-Q-${Random.nextInt(1000, 9999)}"
        val initialPosition = if (_networkMode.value == NetworkMode.SURGE) 18 else 3
        val totalQueue = if (_networkMode.value == NetworkMode.SURGE) 1420 else 24

        _queueState.value = QueueSimulationState(
            isInQueue = true,
            ticketNumber = randomTicket,
            position = initialPosition,
            totalInQueue = totalQueue,
            estimatedWaitSeconds = initialPosition * 2,
            progressPercent = 0.1f,
            queueMessage = "Enqueued at Edge Load Balancer. Checking subject quota...",
            isCompleted = false
        )

        queueSimulationJob = viewModelScope.launch {
            var currentPos = initialPosition
            var progress = 0.15f

            while (currentPos > 0) {
                delay(if (_networkMode.value == NetworkMode.SURGE) 1200L else 700L)
                currentPos -= if (_networkMode.value == NetworkMode.SURGE) Random.nextInt(1, 4) else 1
                if (currentPos < 0) currentPos = 0

                progress = 1.0f - (currentPos.toFloat() / initialPosition.toFloat() * 0.85f)
                val statusMessage = when {
                    currentPos > 12 -> "Surge Traffic Protection: In virtual waiting room (Pos #$currentPos)"
                    currentPos > 6 -> "Redis Worker assigning block sections (AU1S2627-BSIT4-S1-CS)..."
                    currentPos > 2 -> "Checking Free Higher Education Act RA 10931 subsidy eligibility..."
                    currentPos > 0 -> "Validating prerequisite units and generating Assessment Slip..."
                    else -> "Finalizing enrollment and generating official Permit Number..."
                }

                _queueState.value = _queueState.value.copy(
                    position = currentPos,
                    estimatedWaitSeconds = currentPos * 2,
                    progressPercent = progress.coerceIn(0.1f, 0.95f),
                    queueMessage = statusMessage
                )
            }

            delay(800)
            // Complete submission
            val finalPermit = _draft.value.permitNumber.ifBlank { "2155910369207" }
            val finalRef = _draft.value.referenceNumber.ifBlank { "REF-2026-NEUST-98214" }

            val confirmedDraft = _draft.value.copy(
                isSubmitted = true,
                status = "CONFIRMED",
                queueTicket = randomTicket,
                permitNumber = finalPermit,
                referenceNumber = finalRef,
                currentStep = EnrollmentStep.REVIEW_SUBMIT.index
            )
            _draft.value = confirmedDraft
            repository.saveDraft(confirmedDraft)

            _queueState.value = _queueState.value.copy(
                isInQueue = false,
                isCompleted = true,
                progressPercent = 1.0f,
                queueMessage = "Enrollment confirmed! Certificate of Registration generated."
            )
        }
    }

    fun dismissQueueModal() {
        _queueState.value = _queueState.value.copy(isInQueue = false, isCompleted = false)
    }

    fun login(studentIdOrEmail: String, pin: String) {
        if (studentIdOrEmail.isBlank()) {
            _loginError.value = "Please enter your Student ID or University Email"
            return
        }
        if (pin.length < 4) {
            _loginError.value = "Password or Portal PIN must be at least 4 characters"
            return
        }

        viewModelScope.launch {
            _isAuthenticating.value = true
            _loginError.value = null
            delay(if (_networkMode.value == NetworkMode.SURGE) 1200 else 600)

            val query = studentIdOrEmail.trim().lowercase()
            when {
                query.contains("46809") || query.contains("villaluz") -> {
                    loadPresetStudent("VILLALUZ")
                }
                query.contains("new") || query.contains("applicant") -> {
                    resetDraft()
                }
                query.contains("047568") || query.contains("vergara") || query.contains("jubert") -> {
                    loadPresetStudent("VERGARA")
                }
                else -> {
                    // For custom student numbers, initialize or use current draft
                    if (_draft.value.studentNumber.isBlank() || _draft.value.studentNumber != studentIdOrEmail.trim()) {
                        loadPresetStudent("VERGARA")
                        updateDraft { it.copy(studentNumber = studentIdOrEmail.trim()) }
                    }
                }
            }

            _isAuthenticating.value = false
            _isLoggedIn.value = true
            _autoSaveStatus.value = "Session active: Authenticated as ${_draft.value.firstName} ${_draft.value.lastName}"
        }
    }

    fun loginWithPreset(preset: String) {
        _loginError.value = null
        if (preset == "NEW") {
            resetDraft()
        } else {
            loadPresetStudent(preset)
        }
        _isLoggedIn.value = true
        _autoSaveStatus.value = "Session active: Authenticated via Quick Profile"
    }

    fun logout() {
        _isLoggedIn.value = false
        _loginError.value = null
    }

    fun dismissLoginError() {
        _loginError.value = null
    }
}

class EnrollmentViewModelFactory(
    private val repository: EnrollmentRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnrollmentViewModel::class.java)) {
            return EnrollmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
