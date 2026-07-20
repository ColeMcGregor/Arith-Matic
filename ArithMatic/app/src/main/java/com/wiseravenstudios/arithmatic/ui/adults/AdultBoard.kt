package com.wiseravenstudios.arithmatic.ui.adults

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

private const val ARITH_MATIC_WEBSITE =
    "https://colemcgregor.github.io/studio/projects/arithmatic.html"

private enum class AdultTab(
    val title: String,
    val color: Color
) {
    Statistics(
        title = "Stats",
        color = ChalkColors.PastelBlue
    ),
    Report(
        title = "Report",
        color = ChalkColors.PastelGreen
    ),
    Privacy(
        title = "Privacy",
        color = ChalkColors.PastelPurple
    ),
    Donate(
        title = "Donate",
        color = ChalkColors.PastelPink
    )
}

@Composable
fun AdultsBoard(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember {
        mutableStateOf(AdultTab.Statistics)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Adults",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        AdultTabBar(
            currentTab = currentTab,
            onTabSelected = { selectedTab ->
                currentTab = selectedTab
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (currentTab) {
                AdultTab.Statistics -> {
                    StatisticsTab()
                }

                AdultTab.Report -> {
                    ReportTab()
                }

                AdultTab.Privacy -> {
                    PrivacyTab()
                }

                AdultTab.Donate -> {
                    DonateTab()
                }
            }
        }

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            onClick = onBack
        )
    }
}

@Composable
private fun AdultTabBar(
    currentTab: AdultTab,
    onTabSelected: (AdultTab) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        AdultTab.entries.forEach { tab ->
            val isSelected = tab == currentTab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                            bottomStart = 3.dp,
                            bottomEnd = 3.dp
                        )
                    )
                    .background(
                        color = if (isSelected) {
                            tab.color.copy(alpha = 0.9f)
                        } else {
                            tab.color.copy(alpha = 0.35f)
                        }
                    )
                    .clickable {
                        onTabSelected(tab)
                    }
                    .padding(
                        horizontal = 4.dp,
                        vertical = if (isSelected) {
                            5.dp
                        } else {
                            3.dp
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.title,
                    color = if (isSelected) {
                        Color(0xFF24313F)
                    } else {
                        ChalkColors.ChalkWhite
                    },
                    fontFamily = Chalktastic,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

/*
 * Statistics tab
 *
 * This will eventually contain detailed practice history,
 * accuracy information, response times, and operation breakdowns.
 */
@Composable
private fun StatisticsTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics",
            color = ChalkColors.PastelBlue,
            fontFamily = Chalktastic,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Detailed practice statistics will appear here.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text =
                "Future statistics may include accuracy, questions answered, " +
                        "practice time, and average response speed.",
            color = ChalkColors.PastelYellow,
            fontFamily = Chalktastic,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

/*
 * Report tab
 *
 * This will eventually contain report generation,
 * date filtering, previews, and sharing controls.
 */
@Composable
private fun ReportTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Get Report",
            color = ChalkColors.PastelGreen,
            fontFamily = Chalktastic,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text =
                "Create a report of the student's arithmetic practice " +
                        "and share it with a parent, guardian, or teacher.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Report creation is coming in a future update.",
            color = ChalkColors.PastelYellow,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 25.sp,
            textAlign = TextAlign.Center
        )
    }
}

/*
 * Privacy tab
 *
 * This section explains the app's local-first data model.
 */
@Composable
private fun PrivacyTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Privacy",
            color = ChalkColors.PastelPurple,
            fontFamily = Chalktastic,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Your practice records stay on this device.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text =
                "Arith-Matic does not upload student records or share " +
                        "practice information automatically.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text =
                "A report only leaves the device when you choose to export " +
                        "and share it.",
            color = ChalkColors.PastelGreen,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

/*
 * Donate tab
 *
 * This section can later link directly to a dedicated donation service.
 * For now, it directs users to the Arith-Matic project page.
 */
@Composable
private fun DonateTab() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Support Arith-Matic",
            color = ChalkColors.PastelPink,
            fontFamily = Chalktastic,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text =
                "Arith-Matic is developed independently by " +
                        "Wise Raven Studios.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text =
                "Support helps fund continued development, testing, " +
                        "and future educational features.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ChalkTextAction(
            text = "Visit Website",
            color = ChalkColors.PastelOrange,
            paddingTop = 6.dp,
            fontSize = 18.sp,
            onClick = {
                uriHandler.openUri(ARITH_MATIC_WEBSITE)
            }
        )
    }
}