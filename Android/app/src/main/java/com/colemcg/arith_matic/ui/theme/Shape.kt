package com.colemcg.arith_matic.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Defines the default shapes (corner styles) used throughout the ArithMatic app.
 * These shapes are applied to buttons, cards, text fields, etc.
 *
 * The rounded corners are inspired by the friendly, soft shapes seen in classrooms
 * (like rounded desks, chalkboards, and answer boxes).
 *
 *
 * @author Jardina Gomez
 */
val AppShapes = Shapes(
    // TODO: May need to add or adjust after actually seeing how they look in the app
    extraSmall = RoundedCornerShape(4.dp),   // used for tiny UI elements (chips, small buttons)
    small = RoundedCornerShape(8.dp),        // good for buttons or list items
    medium = RoundedCornerShape(12.dp),      // cards, input fields
    large = RoundedCornerShape(16.dp),       // modal windows or larger containers
    extraLarge = RoundedCornerShape(24.dp)   // large surfaces, special emphasis
)
