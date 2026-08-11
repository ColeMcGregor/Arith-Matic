package com.wiseravenstudios.arithmatic.ui.adults

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.wiseravenstudios.arithmatic.domain.adults.report.AdultReport
import com.wiseravenstudios.arithmatic.domain.adults.report.ExportCsv
import com.wiseravenstudios.arithmatic.domain.adults.report.ExportJson
import com.wiseravenstudios.arithmatic.domain.adults.report.ExportPdf
import com.wiseravenstudios.arithmatic.domain.adults.report.ExportType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Creates an Adult Report export action backed by Android's document picker.
 *
 * The selected report format determines the file type, file extension, and
 * exporter used to generate the saved document.
 */
@Composable
fun rememberAdultReportExporter():
            (AdultReport) -> Unit {
    val context =
        LocalContext.current

    val coroutineScope =
        rememberCoroutineScope()

    var pendingReport by remember {
        mutableStateOf<AdultReport?>(null)
    }

    val documentLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .StartActivityForResult()
        ) { result ->

            val report =
                pendingReport

            if (
                result.resultCode !=
                Activity.RESULT_OK ||
                report == null
            ) {
                pendingReport =
                    null

                return@rememberLauncherForActivityResult
            }

            val documentUri =
                result.data?.data

            if (documentUri == null) {
                pendingReport =
                    null

                return@rememberLauncherForActivityResult
            }

            coroutineScope.launch {
                val exportSucceeded =
                    withContext(
                        Dispatchers.IO
                    ) {
                        try {
                            context.contentResolver
                                .openOutputStream(
                                    documentUri,
                                    "w"
                                )
                                ?.use { outputStream ->

                                    when (
                                        report.options
                                            .exportType
                                    ) {
                                        ExportType.Csv -> {
                                            val content =
                                                ExportCsv.export(
                                                    report =
                                                        report
                                                )

                                            outputStream.write(
                                                content.toByteArray(
                                                    Charsets.UTF_8
                                                )
                                            )
                                        }

                                        ExportType.Json -> {
                                            val content =
                                                ExportJson.export(
                                                    report =
                                                        report
                                                )

                                            outputStream.write(
                                                content.toByteArray(
                                                    Charsets.UTF_8
                                                )
                                            )
                                        }

                                        ExportType.Pdf -> {
                                            val content =
                                                ExportPdf.export(
                                                    report =
                                                        report
                                                )

                                            outputStream.write(
                                                content
                                            )
                                        }
                                    }

                                    outputStream.flush()
                                }
                                ?: return@withContext false

                            true
                        } catch (_: Exception) {
                            false
                        }
                    }

                Toast.makeText(
                    context,
                    if (exportSucceeded) {
                        "Report saved."
                    } else {
                        "Unable to save report."
                    },
                    Toast.LENGTH_SHORT
                ).show()

                pendingReport =
                    null
            }
        }

    return remember(
        documentLauncher
    ) {
        { report ->
            pendingReport =
                report

            val exportType =
                report.options.exportType

            val intent =
                Intent(
                    Intent.ACTION_CREATE_DOCUMENT
                ).apply {
                    addCategory(
                        Intent.CATEGORY_OPENABLE
                    )

                    type =
                        exportType.mimeType()

                    putExtra(
                        Intent.EXTRA_TITLE,
                        report.defaultFileName()
                    )
                }

            documentLauncher.launch(
                intent
            )
        }
    }
}

private fun ExportType.mimeType():
        String {
    return when (this) {
        ExportType.Csv ->
            "text/csv"

        ExportType.Json ->
            "application/json"

        ExportType.Pdf ->
            "application/pdf"
    }
}

private fun ExportType.fileExtension():
        String {
    return when (this) {
        ExportType.Csv ->
            "csv"

        ExportType.Json ->
            "json"

        ExportType.Pdf ->
            "pdf"
    }
}

private fun AdultReport.defaultFileName():
        String {
    val date =
        Instant.ofEpochMilli(
            generatedAtEpochMillis
        )
            .atZone(
                ZoneId.systemDefault()
            )
            .format(
                FILE_DATE_FORMAT
            )

    return "arithmatic-report-$date." +
            options.exportType
                .fileExtension()
}

private val FILE_DATE_FORMAT =
    DateTimeFormatter.ofPattern(
        "yyyy-MM-dd"
    )