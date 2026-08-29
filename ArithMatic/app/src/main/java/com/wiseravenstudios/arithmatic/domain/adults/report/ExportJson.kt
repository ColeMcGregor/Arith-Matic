package com.wiseravenstudios.arithmatic.domain.adults.report

import com.wiseravenstudios.arithmatic.domain.adults.AdultHistoryPeriod
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Converts an [AdultReport] into human-readable JSON text.
 *
 * The JSON structure preserves report options, history selection, statistics,
 * stratification, time-series points, rounds, and detailed attempts.
 */
object ExportJson {

    fun export(
        report: AdultReport,
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): String {
        val builder =
            JsonBuilder()

        builder.beginObject()

        builder.property(
            name = "reportType",
            value = "Arith-Matic Practice Report"
        )

        builder.property(
            name = "generatedAtEpochMillis",
            value =
                report.generatedAtEpochMillis
        )

        builder.property(
            name = "generatedAt",
            value =
                formatDateTime(
                    epochMillis =
                        report.generatedAtEpochMillis,
                    zoneId =
                        zoneId
                )
        )

        builder.name(
            name = "options"
        )

        appendOptions(
            builder = builder,
            options = report.options
        )

        builder.name(
            name = "selection"
        )

        appendSelection(
            builder = builder,
            selection =
                report.selection
        )

        builder.name(
            name = "summary"
        )

        appendSummary(
            builder = builder,
            report = report
        )

        builder.name(
            name = "rounds"
        )

        builder.beginArray()

        report.rounds.forEach { round ->
            builder.beginObject()

            builder.property(
                name = "id",
                value = round.id
            )

            builder.property(
                name =
                    "completedAtEpochMillis",
                value =
                    round.completedAtEpochMillis
            )

            builder.property(
                name = "completedAt",
                value =
                    formatDateTime(
                        epochMillis =
                            round.completedAtEpochMillis,
                        zoneId =
                            zoneId
                    )
            )

            builder.property(
                name =
                    "activeRoundDurationMillis",
                value =
                    round.activeRoundDurationMillis
            )

            builder.name(
                name = "enabledOperations"
            )

            builder.beginArray()

            round.enabledOperations
                .sortedBy { operation ->
                    operation.ordinal
                }
                .forEach { operation ->
                    builder.value(
                        operation.name
                    )
                }

            builder.endArray()

            builder.property(
                name = "allowNegatives",
                value =
                    round.allowNegatives
            )

            builder.property(
                name = "allowDecimals",
                value =
                    round.allowDecimals
            )

            builder.property(
                name = "maximumOperand",
                value =
                    round.maximumOperand
            )

            if (
                round.focusNumber != null
            ) {
                builder.property(
                    name = "focusNumber",
                    value =
                        round.focusNumber
                )
            } else {
                builder.nullProperty(
                    name = "focusNumber"
                )
            }

            builder.property(
                name =
                    "originalQuestionCount",
                value =
                    round.originalQuestionCount
            )

            builder.property(
                name =
                    "matchingQuestionCount",
                value =
                    round.matchingQuestionCount
            )

            builder.property(
                name =
                    "matchingCorrectCount",
                value =
                    round.matchingCorrectCount
            )

            builder.property(
                name =
                    "matchingIncorrectCount",
                value =
                    round.matchingIncorrectCount
            )

            builder.name(
                name = "attempts"
            )

            builder.beginArray()

            round.attempts.forEach { attempt ->
                builder.beginObject()

                builder.property(
                    name = "questionIndex",
                    value =
                        attempt.questionIndex
                )

                if (
                    attempt.operation != null
                ) {
                    builder.property(
                        name = "operation",
                        value =
                            attempt.operation.name
                    )
                } else {
                    builder.nullProperty(
                        name = "operation"
                    )
                }

                builder.name(
                    name = "operands"
                )

                builder.beginArray()

                attempt.operands
                    .forEach { operand ->
                        builder.decimalValue(
                            value = operand
                        )
                    }

                builder.endArray()

                builder.property(
                    name = "questionText",
                    value =
                        attempt.questionText
                )

                builder.property(
                    name = "expectedAnswer",
                    value =
                        attempt.expectedAnswer
                )

                builder.property(
                    name = "selectedAnswer",
                    value =
                        attempt.selectedAnswer
                )

                builder.name(
                    name = "answerChoices"
                )

                builder.beginArray()

                attempt.answerChoices
                    .forEach { answerChoice ->
                        builder.value(
                            answerChoice
                        )
                    }

                builder.endArray()

                builder.property(
                    name =
                        "selectedChoiceIndex",
                    value =
                        attempt.selectedChoiceIndex
                )

                builder.property(
                    name =
                        "correctChoiceIndex",
                    value =
                        attempt.correctChoiceIndex
                )

                builder.property(
                    name = "isCorrect",
                    value =
                        attempt.isCorrect
                )

                builder.property(
                    name =
                        "activeDurationMillis",
                    value =
                        attempt.activeDurationMillis
                )

                builder.endObject()
            }

            builder.endArray()

            builder.endObject()
        }

        builder.endArray()

        builder.endObject()

        return builder.build()
    }

    private fun appendOptions(
        builder: JsonBuilder,
        options: AdultReportOptions
    ) {
        builder.beginObject()

        builder.property(
            name = "exportType",
            value =
                options.exportType.name
        )

        builder.property(
            name = "detailLevel",
            value =
                options.detailLevel.name
        )

        builder.property(
            name = "includeGraphs",
            value =
                options.includeGraphs
        )

        builder.property(
            name = "includeStratification",
            value =
                options.includeStratification
        )

        builder.endObject()
    }

    private fun appendSelection(
        builder: JsonBuilder,
        selection: AdultHistorySelection
    ) {
        builder.beginObject()

        builder.name(
            name = "period"
        )

        appendPeriod(
            builder = builder,
            period =
                selection.period
        )

        builder.name(
            name = "operations"
        )

        builder.beginArray()

        selection.operations
            .sortedBy { operation ->
                operation.ordinal
            }
            .forEach { operation ->
                builder.value(
                    operation.name
                )
            }

        builder.endArray()

        builder.name(
            name = "exactOperands"
        )

        builder.beginArray()

        selection.exactOperands
            .sorted()
            .forEach { operand ->
                builder.decimalValue(
                    value = operand
                )
            }

        builder.endArray()

        builder.name(
            name = "operandRanges"
        )

        builder.beginArray()

        selection.operandRanges
            .forEach { range ->
                builder.beginObject()

                builder.decimalProperty(
                    name = "minimumInclusive",
                    value =
                        range.minimumInclusive
                )

                builder.decimalProperty(
                    name = "maximumInclusive",
                    value =
                        range.maximumInclusive
                )

                builder.endObject()
            }

        builder.endArray()

        if (
            selection.containsNegativeOperand !=
            null
        ) {
            builder.property(
                name =
                    "containsNegativeOperand",
                value =
                    selection
                        .containsNegativeOperand
            )
        } else {
            builder.nullProperty(
                name =
                    "containsNegativeOperand"
            )
        }

        if (
            selection.containsDecimalOperand !=
            null
        ) {
            builder.property(
                name =
                    "containsDecimalOperand",
                value =
                    selection
                        .containsDecimalOperand
            )
        } else {
            builder.nullProperty(
                name =
                    "containsDecimalOperand"
            )
        }

        builder.name(
            name = "enabledRoundOperations"
        )

        builder.beginArray()

        selection.enabledRoundOperations
            .sortedBy { operation ->
                operation.ordinal
            }
            .forEach { operation ->
                builder.value(
                    operation.name
                )
            }

        builder.endArray()

        builder.property(
            name =
                "enabledRoundOperationMatchMode",
            value =
                selection
                    .enabledRoundOperationMatchMode
                    .name
        )

        builder.name(
            name = "maximumOperands"
        )

        builder.beginArray()

        selection.maximumOperands
            .sorted()
            .forEach { maximumOperand ->
                builder.value(
                    maximumOperand
                )
            }

        builder.endArray()

        builder.name(
            name = "focusNumbers"
        )

        builder.beginArray()

        selection.focusNumbers
            .sorted()
            .forEach { focusNumber ->
                builder.value(
                    focusNumber
                )
            }

        builder.endArray()

        builder.property(
            name = "correctness",
            value =
                selection.correctness.name
        )

        builder.endObject()
    }

    private fun appendPeriod(
        builder: JsonBuilder,
        period: AdultHistoryPeriod
    ) {
        builder.beginObject()

        when (period) {
            AdultHistoryPeriod.Day -> {
                builder.property(
                    name = "type",
                    value = "Day"
                )
            }

            AdultHistoryPeriod.Last7Days -> {
                builder.property(
                    name = "type",
                    value = "Last7Days"
                )
            }

            AdultHistoryPeriod.Last30Days -> {
                builder.property(
                    name = "type",
                    value = "Last30Days"
                )
            }

            AdultHistoryPeriod.Last365Days -> {
                builder.property(
                    name = "type",
                    value = "Last365Days"
                )
            }

            is AdultHistoryPeriod.Custom -> {
                builder.property(
                    name = "type",
                    value = "Custom"
                )

                builder.property(
                    name = "startDate",
                    value =
                        period.startDate
                            .toString()
                )

                builder.property(
                    name = "endDate",
                    value =
                        period.endDate
                            .toString()
                )
            }
        }

        builder.endObject()
    }

    private fun appendSummary(
        builder: JsonBuilder,
        report: AdultReport
    ) {
        val summary =
            report.summary

        builder.beginObject()

        builder.property(
            name = "roundCount",
            value =
                summary.roundCount
        )

        builder.property(
            name = "questionCount",
            value =
                summary.questionCount
        )

        builder.property(
            name = "correctCount",
            value =
                summary.correctCount
        )

        builder.property(
            name = "incorrectCount",
            value =
                summary.incorrectCount
        )

        builder.decimalProperty(
            name = "accuracyPercent",
            value =
                summary.accuracyPercent
        )

        builder.property(
            name =
                "totalQuestionDurationMillis",
            value =
                summary.totalQuestionDurationMillis
        )

        builder.property(
            name =
                "averageQuestionDurationMillis",
            value =
                summary.averageQuestionDurationMillis
        )

        builder.property(
            name =
                "averageRoundDurationMillis",
            value =
                summary.averageRoundDurationMillis
        )

        builder.name(
            name = "operationSummaries"
        )

        builder.beginArray()

        summary.operationSummaries
            .forEach { operationSummary ->
                builder.beginObject()

                builder.property(
                    name = "operation",
                    value =
                        operationSummary
                            .operation
                            .name
                )

                builder.property(
                    name = "questionCount",
                    value =
                        operationSummary
                            .questionCount
                )

                builder.property(
                    name = "correctCount",
                    value =
                        operationSummary
                            .correctCount
                )

                builder.property(
                    name = "incorrectCount",
                    value =
                        operationSummary
                            .incorrectCount
                )

                builder.decimalProperty(
                    name = "accuracyPercent",
                    value =
                        operationSummary
                            .accuracyPercent
                )

                builder.property(
                    name =
                        "totalDurationMillis",
                    value =
                        operationSummary
                            .totalDurationMillis
                )

                builder.property(
                    name =
                        "averageDurationMillis",
                    value =
                        operationSummary
                            .averageDurationMillis
                )

                builder.endObject()
            }

        builder.endArray()

        builder.name(
            name = "timePoints"
        )

        builder.beginArray()

        summary.timePoints
            .forEach { timePoint ->
                builder.beginObject()

                builder.property(
                    name =
                        "startEpochMillis",
                    value =
                        timePoint.startEpochMillis
                )

                builder.property(
                    name =
                        "endEpochMillisExclusive",
                    value =
                        timePoint
                            .endEpochMillisExclusive
                )

                builder.property(
                    name = "questionCount",
                    value =
                        timePoint.questionCount
                )

                builder.property(
                    name = "correctCount",
                    value =
                        timePoint.correctCount
                )

                builder.property(
                    name = "incorrectCount",
                    value =
                        timePoint.incorrectCount
                )

                builder.decimalProperty(
                    name = "accuracyPercent",
                    value =
                        timePoint.accuracyPercent
                )

                builder.property(
                    name =
                        "totalDurationMillis",
                    value =
                        timePoint.totalDurationMillis
                )

                builder.property(
                    name =
                        "averageDurationMillis",
                    value =
                        timePoint.averageDurationMillis
                )

                builder.endObject()
            }

        builder.endArray()

        if (
            report.options.includeStratification
        ) {
            builder.name(
                name = "operandStratifications"
            )

            builder.beginArray()

            summary.operandStratifications
                .forEach { stratification ->
                    builder.beginObject()

                    builder.property(
                        name = "operation",
                        value =
                            stratification
                                .operation
                                .name
                    )

                    builder.name(
                        name = "strata"
                    )

                    builder.beginArray()

                    stratification.strata
                        .forEach { stratum ->
                            builder.beginObject()

                            builder.decimalProperty(
                                name =
                                    "minimumOperandInclusive",
                                value =
                                    stratum
                                        .band
                                        .minimumInclusive
                            )

                            builder.decimalProperty(
                                name =
                                    "maximumOperandInclusive",
                                value =
                                    stratum
                                        .band
                                        .maximumInclusive
                            )

                            builder.property(
                                name =
                                    "displayName",
                                value =
                                    stratum
                                        .band
                                        .displayName
                            )

                            builder.property(
                                name =
                                    "questionCount",
                                value =
                                    stratum
                                        .questionCount
                            )

                            builder.property(
                                name =
                                    "correctCount",
                                value =
                                    stratum
                                        .correctCount
                            )

                            builder.property(
                                name =
                                    "incorrectCount",
                                value =
                                    stratum
                                        .incorrectCount
                            )

                            builder.decimalProperty(
                                name =
                                    "accuracyPercent",
                                value =
                                    stratum
                                        .accuracyPercent
                            )

                            builder.property(
                                name =
                                    "totalDurationMillis",
                                value =
                                    stratum
                                        .totalDurationMillis
                            )

                            builder.property(
                                name =
                                    "averageDurationMillis",
                                value =
                                    stratum
                                        .averageDurationMillis
                            )

                            builder.endObject()
                        }

                    builder.endArray()

                    builder.endObject()
                }

            builder.endArray()
        }

        builder.endObject()
    }

    private fun formatDateTime(
        epochMillis: Long,
        zoneId: ZoneId
    ): String {
        return Instant.ofEpochMilli(
            epochMillis
        )
            .atZone(zoneId)
            .format(
                DATE_TIME_FORMAT
            )
    }

    private val DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"
        )

    /**
     * Writes structured JSON with indentation, primitive values, arrays,
     * objects, and standard JSON string escaping.
     */
    private class JsonBuilder {

        private val builder =
            StringBuilder()

        private val contexts =
            mutableListOf<Context>()

        private var indentLevel =
            0

        fun beginObject() {
            beforeValue()

            builder.append("{")

            contexts +=
                Context(
                    type =
                        ContextType.Object
                )

            indentLevel++
        }

        fun endObject() {
            indentLevel--

            val context =
                contexts.removeAt(
                    contexts.lastIndex
                )

            if (!context.first) {
                builder.appendLine()
                appendIndent()
            }

            builder.append("}")
        }

        fun beginArray() {
            beforeValue()

            builder.append("[")

            contexts +=
                Context(
                    type =
                        ContextType.Array
                )

            indentLevel++
        }

        fun endArray() {
            indentLevel--

            val context =
                contexts.removeAt(
                    contexts.lastIndex
                )

            if (!context.first) {
                builder.appendLine()
                appendIndent()
            }

            builder.append("]")
        }

        fun name(
            name: String
        ) {
            beforeObjectProperty()

            builder.append(
                quote(name)
            )

            builder.append(": ")

            contexts.last()
                .waitingForValue =
                true
        }

        fun property(
            name: String,
            value: String
        ) {
            name(
                name = name
            )

            value(
                value = value
            )
        }

        fun property(
            name: String,
            value: Int
        ) {
            name(
                name = name
            )

            value(
                value = value
            )
        }

        fun property(
            name: String,
            value: Long
        ) {
            name(
                name = name
            )

            value(
                value = value
            )
        }

        fun property(
            name: String,
            value: Boolean
        ) {
            name(
                name = name
            )

            value(
                value = value
            )
        }

        fun decimalProperty(
            name: String,
            value: Double
        ) {
            name(
                name = name
            )

            decimalValue(
                value = value
            )
        }

        fun decimalProperty(
            name: String,
            value: BigDecimal
        ) {
            name(
                name = name
            )

            decimalValue(
                value = value
            )
        }

        fun nullProperty(
            name: String
        ) {
            name(
                name = name
            )

            nullValue()
        }

        fun value(
            value: String
        ) {
            beforeValue()

            builder.append(
                quote(value)
            )
        }

        fun value(
            value: Int
        ) {
            beforeValue()

            builder.append(value)
        }

        fun value(
            value: Long
        ) {
            beforeValue()

            builder.append(value)
        }

        fun value(
            value: Boolean
        ) {
            beforeValue()

            builder.append(value)
        }

        fun decimalValue(
            value: Double
        ) {
            beforeValue()

            require(value.isFinite()) {
                "JSON numeric values must be finite."
            }

            builder.append(
                String.format(
                    Locale.US,
                    "%.6f",
                    value
                )
                    .trimEnd('0')
                    .trimEnd('.')
            )
        }

        fun decimalValue(
            value: BigDecimal
        ) {
            beforeValue()

            builder.append(
                value
                    .stripTrailingZeros()
                    .toPlainString()
            )
        }

        fun nullValue() {
            beforeValue()

            builder.append("null")
        }

        fun build(): String {
            require(contexts.isEmpty()) {
                "JSON document contains unclosed structures."
            }

            return builder.toString()
        }

        private fun beforeObjectProperty() {
            val context =
                contexts.lastOrNull()
                    ?: error(
                        "JSON property requires an object context."
                    )

            require(
                context.type ==
                        ContextType.Object
            ) {
                "JSON properties may only be written inside objects."
            }

            require(
                !context.waitingForValue
            ) {
                "The previous JSON property has no value."
            }

            if (!context.first) {
                builder.append(",")
            }

            builder.appendLine()
            appendIndent()

            context.first =
                false
        }

        private fun beforeValue() {
            val context =
                contexts.lastOrNull()
                    ?: return

            when (context.type) {
                ContextType.Object -> {
                    require(
                        context.waitingForValue
                    ) {
                        "JSON object values require a property name."
                    }

                    context.waitingForValue =
                        false
                }

                ContextType.Array -> {
                    if (!context.first) {
                        builder.append(",")
                    }

                    builder.appendLine()
                    appendIndent()

                    context.first =
                        false
                }
            }
        }

        private fun appendIndent() {
            repeat(indentLevel) {
                builder.append(
                    "  "
                )
            }
        }

        private fun quote(
            value: String
        ): String {
            return buildString {
                append("\"")

                value.forEach { character ->
                    when (character) {
                        '"' ->
                            append("\\\"")

                        '\\' ->
                            append("\\\\")

                        '\b' ->
                            append("\\b")

                        '\u000C' ->
                            append("\\f")

                        '\n' ->
                            append("\\n")

                        '\r' ->
                            append("\\r")

                        '\t' ->
                            append("\\t")

                        else -> {
                            if (
                                character.code <
                                0x20
                            ) {
                                append(
                                    "\\u%04x".format(
                                        character.code
                                    )
                                )
                            } else {
                                append(character)
                            }
                        }
                    }
                }

                append("\"")
            }
        }

        private data class Context(
            val type: ContextType,
            var first: Boolean = true,
            var waitingForValue:
            Boolean = false
        )

        private enum class ContextType {
            Object,
            Array
        }
    }
}