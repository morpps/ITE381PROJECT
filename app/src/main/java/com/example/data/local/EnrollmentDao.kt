package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.EnrollmentDraft
import kotlinx.coroutines.flow.Flow

@Dao
interface EnrollmentDao {
    @Query("SELECT * FROM enrollment_drafts WHERE id = 1 LIMIT 1")
    fun getDraft(): Flow<EnrollmentDraft?>

    @Query("SELECT * FROM enrollment_drafts WHERE id = 1 LIMIT 1")
    suspend fun getDraftOnce(): EnrollmentDraft?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(draft: EnrollmentDraft)

    @Query("DELETE FROM enrollment_drafts WHERE id = 1")
    suspend fun clearDraft()

    @Query("SELECT * FROM enrollment_drafts WHERE isSubmitted = 1 ORDER BY lastSavedTimestamp DESC")
    fun getSubmittedEnrollments(): Flow<List<EnrollmentDraft>>
}
