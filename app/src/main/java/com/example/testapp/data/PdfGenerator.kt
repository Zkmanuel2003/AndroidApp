package com.example.testapp.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun auftragAlsPdfSpeichern(context: Context, auftrag: Auftrag): String {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
    val page = document.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    val titelPaint = Paint().apply {
        textSize = 24f
        color = Color.BLACK
        isFakeBoldText = true
    }
    val textPaint = Paint().apply {
        textSize = 14f
        color = Color.BLACK
    }
    val grauPaint = Paint().apply {
        textSize = 12f
        color = Color.GRAY
    }
    val liniePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
    }

    val datum = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN).format(Date(auftrag.datum))
    var y = 60f

    canvas.drawText("Auftragsbericht", 40f, y, titelPaint)
    y += 10f
    canvas.drawLine(40f, y, 555f, y, liniePaint)
    y += 30f

    canvas.drawText("Kunde:", 40f, y, grauPaint)
    canvas.drawText(auftrag.kundenName, 150f, y, textPaint)
    y += 25f

    canvas.drawText("Ort:", 40f, y, grauPaint)
    canvas.drawText(auftrag.ort, 150f, y, textPaint)
    y += 25f

    canvas.drawText("Datum:", 40f, y, grauPaint)
    canvas.drawText(datum, 150f, y, textPaint)
    y += 25f

    if (auftrag.istSpontaneinsatz) {
        canvas.drawText("Art:", 40f, y, grauPaint)
        canvas.drawText("Spontaneinsatz", 150f, y, textPaint)
        y += 25f
    }

    y += 10f
    canvas.drawLine(40f, y, 555f, y, liniePaint)
    y += 30f

    canvas.drawText("Dokumentation", 40f, y, titelPaint.apply { textSize = 16f })
    y += 25f

    val maxBreite = 515f
    val woerter = auftrag.dokumentation.ifBlank { "Keine Dokumentation vorhanden." }.split(" ")
    var zeile = ""
    for (wort in woerter) {
        val testZeile = if (zeile.isEmpty()) wort else "$zeile $wort"
        if (textPaint.measureText(testZeile) > maxBreite) {
            canvas.drawText(zeile, 40f, y, textPaint)
            y += 20f
            zeile = wort
        } else {
            zeile = testZeile
        }
    }
    if (zeile.isNotEmpty()) canvas.drawText(zeile, 40f, y, textPaint)

    document.finishPage(page)

    // In App-internen Ordner speichern (bleibt dauerhaft erhalten)
    val ordner = File(context.filesDir, "pdfs")
    ordner.mkdirs()
    val datei = File(ordner, "Auftrag_${auftrag.id}_${auftrag.kundenName}.pdf")
    document.writeTo(datei.outputStream())
    document.close()

    return datei.absolutePath
}
