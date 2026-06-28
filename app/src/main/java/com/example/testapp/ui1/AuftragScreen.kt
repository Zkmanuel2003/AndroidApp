package com.example.testapp.ui1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.data.Auftrag
import com.example.testapp.viewmodel.AuftragViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuftragScreen(
    viewModel: AuftragViewModel,
    onBackClick: () -> Unit
) {
    val auftraege by viewModel.auftraege.collectAsState()
    var spontanDialogSichtbar by remember { mutableStateOf(false) }
    var ausgewaehlteId by remember { mutableStateOf<Int?>(null) }

    // Immer den aktuellen Auftrag aus dem Flow holen — so sieht man Änderungen sofort
    val ausgewaehlterAuftrag = ausgewaehlteId?.let { id -> auftraege.find { it.id == id } }

    // Dialog: Spontaneinsatz anlegen
    if (spontanDialogSichtbar) {
        SpontaneinsatzDialog(
            onBestaetigen = { name, ort ->
                viewModel.auftragAnlegen(name, ort, istSpontaneinsatz = true)
                spontanDialogSichtbar = false
            },
            onAbbrechen = { spontanDialogSichtbar = false }
        )
    }

    // Detail-Ansicht wenn ein Auftrag ausgewählt wurde
    if (ausgewaehlterAuftrag != null) {
        AuftragDetailScreen(
            auftrag = ausgewaehlterAuftrag,
            onStarten = { viewModel.auftragStarten(it) },
            onAbschliessen = { auftrag, doku -> viewModel.auftragAbschliessen(auftrag, doku) },
            onZurueck = { ausgewaehlteId = null },
            viewModel = viewModel
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Aufträge") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { spontanDialogSichtbar = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Spontaneinsatz") }
            )
        }
    ) { innerPadding ->
        if (auftraege.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Keine Aufträge vorhanden",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(auftraege) { auftrag ->
                    AuftragKarte(
                        auftrag = auftrag,
                        onClick = { ausgewaehlteId = auftrag.id }
                    )
                }
            }
        }
    }
}

@Composable
fun AuftragKarte(auftrag: Auftrag, onClick: () -> Unit) {
    val datum = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).format(Date(auftrag.datum))
    val statusFarbe = when (auftrag.status) {
        "aktiv" -> MaterialTheme.colorScheme.primary
        "abgeschlossen" -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.secondary
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(auftrag.kundenName, fontWeight = FontWeight.SemiBold)
                Text(auftrag.ort, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Text(datum, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    auftrag.status.replaceFirstChar { it.uppercase() },
                    color = statusFarbe,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (auftrag.istSpontaneinsatz) {
                    Text("Spontan", fontSize = 11.sp, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}

@Composable
fun SpontaneinsatzDialog(
    onBestaetigen: (String, String) -> Unit,
    onAbbrechen: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ort by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onAbbrechen,
        title = { Text("Spontaneinsatz") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Kundenname") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ort,
                    onValueChange = { ort = it },
                    label = { Text("Ort") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onBestaetigen(name, ort) },
                enabled = name.isNotBlank() && ort.isNotBlank()
            ) {
                Text("Anlegen")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onAbbrechen) { Text("Abbrechen") }
        }
    )
}

@Composable
fun AuftragDetailScreen(
    auftrag: Auftrag,
    onStarten: (Auftrag) -> Unit,
    onAbschliessen: (Auftrag, String) -> Unit,
    onZurueck: () -> Unit,
    viewModel: AuftragViewModel
) {
    var dokumentation by remember { mutableStateOf(auftrag.dokumentation) }
    var startDialogSichtbar by remember { mutableStateOf(false) }
    var pdfAnzeigen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (pdfAnzeigen && auftrag.pdfPfad != null) {
        PdfViewerScreen(
            pdfPfad = auftrag.pdfPfad,
            onZurueck = { pdfAnzeigen = false }
        )
        return
    }

    if (startDialogSichtbar) {
        AlertDialog(
            onDismissRequest = { startDialogSichtbar = false },
            title = { Text("Auftrag starten") },
            text = { Text("Wollen Sie den Auftrag für ${auftrag.kundenName} in ${auftrag.ort} starten?") },
            confirmButton = {
                Button(onClick = {
                    onStarten(auftrag)
                    startDialogSichtbar = false
                }) { Text("Starten") }
            },
            dismissButton = {
                OutlinedButton(onClick = { startDialogSichtbar = false }) { Text("Abbrechen") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Text(auftrag.kundenName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(auftrag.ort, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(24.dp))

        if (auftrag.status == "offen") {
            Button(
                onClick = { startDialogSichtbar = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Auftrag starten")
            }
        }

        if (auftrag.status == "aktiv") {
            Text("Dokumentation", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dokumentation,
                onValueChange = { dokumentation = it },
                placeholder = { Text("Was wurde gemacht?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onAbschliessen(auftrag, dokumentation) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Auftrag abschließen")
            }
        }

        if (auftrag.status == "abgeschlossen") {
            Text("Dokumentation", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    auftrag.dokumentation.ifBlank { "Keine Dokumentation" },
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (auftrag.pdfPfad != null) {
                Button(
                    onClick = { pdfAnzeigen = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PDF anzeigen")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.pdfErstellen(context, auftrag) { pdfAnzeigen = true }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PDF erstellen")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = onZurueck,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück zur Übersicht")
        }
    }
}
