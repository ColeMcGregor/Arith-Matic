package com.wiseravenstudios.arithmatic.domain.history.query

/**
 * Controls how a collection of selected operations is matched.
 *
 * Any:
 * At least one selected operation must be present.
 *
 * All:
 * Every selected operation must be present.
 *
 * For attempt filtering, an individual attempt has one operation, so
 * selected operations naturally behave as an Any match.
 *
 * This mode is primarily useful for round configuration filtering because a
 * round may have several operations enabled.
 */
enum class OperationMatchMode {
    Any,
    All
}