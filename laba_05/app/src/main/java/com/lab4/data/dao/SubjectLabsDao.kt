package com.lab4.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lab4.data.entity.SubjectLabEntity

@Dao
interface SubjectLabsDao {
    @Query("SELECT * FROM subjectsLabs WHERE subject_id = :subjectId")
    suspend fun getSubjectLabsBySubjectId(subjectId: Int): List<SubjectLabEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjectLab(subjectLabEntity: SubjectLabEntity)

    @Update
    suspend fun updateLab(lab: SubjectLabEntity)

    @Query("UPDATE subjectsLabs SET status = :newStatus WHERE id = :labId")
    suspend fun updateStatus(labId: Int, newStatus: String)

    @Query("UPDATE subjectsLabs SET comment = :newComment WHERE id = :labId")
    suspend fun updateComment(labId: Int, newComment: String)
}