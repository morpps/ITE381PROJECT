package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.EnrollmentDao
import com.example.data.model.EnrollmentDraft
import com.example.data.repository.EnrollmentRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: EnrollmentDao
    private lateinit var repository: EnrollmentRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.enrollmentDao()
        repository = EnrollmentRepository(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NEUST Enrollment", appName)
    }

    @Test
    fun testRoomDraftAutoSaveAndRestore() = runBlocking {
        val sample = repository.getSampleStudentVergara()
        repository.saveDraft(sample)

        val retrieved = repository.getDraftOnce()
        assertNotNull(retrieved)
        assertEquals("01-2324-047568", retrieved?.studentNumber)
        assertEquals("Vergara", retrieved?.lastName)
        assertEquals("Jubert", retrieved?.firstName)
        assertEquals("2155910369207", retrieved?.permitNumber)

        val subjects = repository.deserializeSubjects(retrieved?.selectedSubjectsJson)
        assertTrue(subjects.isNotEmpty())
        assertEquals("ITE 367", subjects[0].code)
    }

    @Test
    fun testAuthenticationLogic() {
        val viewModel = com.example.ui.viewmodel.EnrollmentViewModel(repository)
        assertEquals(false, viewModel.isLoggedIn.value)

        // Test login with preset student
        viewModel.loginWithPreset("VERGARA")
        assertEquals(true, viewModel.isLoggedIn.value)

        // Test logout
        viewModel.logout()
        assertEquals(false, viewModel.isLoggedIn.value)
    }
}
