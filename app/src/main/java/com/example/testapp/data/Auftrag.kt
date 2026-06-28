package com.example.testapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auftraege")
data class Auftrag(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val kundenName: String,
    val ort: String,
    val datum: Long = System.currentTimeMillis(),
    val dokumentation: String = "",
    val status: String = "offen",  // "offen", "aktiv", "abgeschlossen"
    val istSpontaneinsatz: Boolean = false,
    val pdfPfad: String? = null
)
