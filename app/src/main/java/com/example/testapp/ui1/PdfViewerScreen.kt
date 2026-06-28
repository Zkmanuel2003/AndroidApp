package com.example.testapp.ui1

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    pdfPfad: String,
    onZurueck: () -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // PDF erste Seite rendern
    LaunchedEffect(pdfPfad) {
        val datei = File(pdfPfad)
        if (datei.exists()) {
            val fd = ParcelFileDescriptor.open(datei, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fd)
            val seite = renderer.openPage(0)
            val bmp = Bitmap.createBitmap(seite.width * 2, seite.height * 2, Bitmap.Config.ARGB_8888)
            seite.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            seite.close()
            renderer.close()
            fd.close()
            bitmap = bmp
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Auftragsbericht") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "PDF Vorschau",
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onZurueck,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zurück")
            }
        }
    }
}
