package com.example.testapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.Auftrag
import com.example.testapp.data.AuftragDao
import com.example.testapp.data.auftragAlsPdfSpeichern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuftragViewModel(
    private val auftragDao: AuftragDao,
    private val userEmail: String
) : ViewModel() {

    val auftraege: StateFlow<List<Auftrag>> = auftragDao.getAlleAuftraege(userEmail)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun auftragAnlegen(kundenName: String, ort: String, istSpontaneinsatz: Boolean) {
        viewModelScope.launch {
            auftragDao.insertAuftrag(
                Auftrag(
                    userEmail = userEmail,
                    kundenName = kundenName,
                    ort = ort,
                    istSpontaneinsatz = istSpontaneinsatz
                )
            )
        }
    }

    fun auftragStarten(auftrag: Auftrag) {
        viewModelScope.launch {
            auftragDao.updateAuftrag(auftrag.copy(status = "aktiv"))
        }
    }

    fun auftragAbschliessen(auftrag: Auftrag, dokumentation: String) {
        viewModelScope.launch {
            auftragDao.updateAuftrag(
                auftrag.copy(
                    status = "abgeschlossen",
                    dokumentation = dokumentation
                )
            )
        }
    }

    fun pdfErstellen(context: Context, auftrag: Auftrag, onFertig: (String) -> Unit) {
        viewModelScope.launch {
            val pfad = withContext(Dispatchers.IO) {
                auftragAlsPdfSpeichern(context, auftrag)
            }
            auftragDao.updateAuftrag(auftrag.copy(pdfPfad = pfad))
            onFertig(pfad)
        }
    }
}
