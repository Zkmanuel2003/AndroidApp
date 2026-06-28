package com.example.testapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AuftragDao {

    @Query("SELECT * FROM auftraege WHERE userEmail = :email ORDER BY datum DESC")
    fun getAlleAuftraege(email: String): Flow<List<Auftrag>>

    @Insert
    suspend fun insertAuftrag(auftrag: Auftrag): Long

    @Update
    suspend fun updateAuftrag(auftrag: Auftrag)

    @Delete
    suspend fun deleteAuftrag(auftrag: Auftrag)
}
