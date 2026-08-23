package com.wiseravenstudios.arithmatic.ui.adults

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.collectAsState
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
import com.wiseravenstudios.arithmatic.domain.adults.report.AdultReport
import com.wiseravenstudios.arithmatic.domain.adults.report.AdultReportBuilder
import com.wiseravenstudios.arithmatic.domain.adults.report.AdultReportOptions
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsCalculator
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.calculateAdultBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

private const val ARITH_MATIC_WEBSITE =
    "https://colemcgregor.github.io/studio/projects/arithmatic.html"

private const val WISE_RAVEN_PATREON =
    "https://www.patreon.com/cw/WiseRavenStudios"

private enum class AdultTab(
    val title: String,
    val color: Color
) {
    Privacy(
        title = "Privacy",
        color = ChalkColors.PastelPurple
    ),
    Support(
        title = "Support",
        color = ChalkColors.PastelPink
    ),
    Statistics(
        title = "Stats",
        color = ChalkColors.PastelBlue
    ),
    Report(
        title = "Report",
        color = ChalkColors.PastelGreen
    )
}

@Composable
fun AdultBoard(
    viewModel: AdultAreaViewModel,
    onExportReport: (AdultReport) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by
    viewModel.uiState.collectAsState()

    var currentTab by remember {
        mutableStateOf(
            AdultTab.Statistics
        )
    }

    var reportOptions by remember {
        mutableStateOf(
            AdultReportOptions()
        )
    }

    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateAdultBoardMetrics(
                width = maxWidth,
                height = maxHeight
            )

        if (metrics.isDoubleColumn) {
            DoubleColumnAdultLayout(
                currentTab = currentTab,
                metrics = metrics,
                onTabSelected = { selectedTab ->
                    currentTab =
                        selectedTab
                },
                onBack = onBack,
                content = {
                    AdultTabContent(
                        currentTab = currentTab,
                        uiState = uiState,
                        viewModel = viewModel,
                        metrics = metrics,
                        reportOptions = reportOptions,
                        onReportOptionsChanged = {
                                newOptions ->

                            reportOptions =
                                newOptions
                        },
                        onExportReport =
                            onExportReport
                    )
                }
            )
        } else {
            SingleColumnAdultLayout(
                currentTab = currentTab,
                metrics = metrics,
                onTabSelected = { selectedTab ->
                    currentTab =
                        selectedTab
                },
                onBack = onBack,
                content = {
                    AdultTabContent(
                        currentTab = currentTab,
                        uiState = uiState,
                        viewModel = viewModel,
                        metrics = metrics,
                        reportOptions = reportOptions,
                        onReportOptionsChanged = {
                                newOptions ->

                            reportOptions =
                                newOptions
                        },
                        onExportReport =
                            onExportReport
                    )
                }
            )
        }
    }
}

@Composable
private fun SingleColumnAdultLayout(
    currentTab: AdultTab,
    metrics: BoardResponsiveMetrics,
    onTabSelected: (AdultTab) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal =
                    metrics.contentHorizontalPadding,
                vertical =
                    metrics.contentVerticalPadding
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Adults",
            color =
                ChalkColors.PastelOrange,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.displayTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.mediumSpacing
                )
        )

        AdultTabBar(
            currentTab = currentTab,
            metrics = metrics,
            onTabSelected =
                onTabSelected
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }

        ChalkTextAction(
            text = "Back",
            color =
                ChalkColors.PastelYellow,
            fontSize =
                metrics.headingTextSize,
            paddingTop =
                metrics.actionVerticalPadding,
            paddingBottom =
                metrics.actionVerticalPadding,
            onClick = onBack
        )
    }
}

@Composable
private fun DoubleColumnAdultLayout(
    currentTab: AdultTab,
    metrics: BoardResponsiveMetrics,
    onTabSelected: (AdultTab) -> Unit,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal =
                    metrics.contentHorizontalPadding,
                vertical =
                    metrics.contentVerticalPadding
            )
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                )
        ) {
            Text(
                text = "Adults",
                color =
                    ChalkColors.PastelOrange,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.displayTextSize,
                fontWeight =
                    FontWeight.Bold,
                maxLines = 1
            )

            AdultTabBar(
                currentTab = currentTab,
                metrics = metrics,
                onTabSelected =
                    onTabSelected,
                modifier =
                    Modifier.weight(1f)
            )

            ChalkTextAction(
                text = "Back",
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = onBack
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }
    }
}

@Composable
private fun AdultTabContent(
    currentTab: AdultTab,
    uiState: AdultAreaUiState,
    viewModel: AdultAreaViewModel,
    metrics: BoardResponsiveMetrics,
    reportOptions: AdultReportOptions,
    onReportOptionsChanged: (AdultReportOptions) -> Unit,
    onExportReport: (AdultReport) -> Unit
) {
    when (currentTab) {
        AdultTab.Privacy -> {
            PrivacyTab(
                metrics = metrics
            )
        }

        AdultTab.Support -> {
            SupportTab(
                metrics = metrics
            )
        }

        AdultTab.Statistics -> {
            StatisticsTab(
                uiState = uiState,
                viewModel = viewModel,
                metrics = metrics
            )
        }

        AdultTab.Report -> {
            ReportTab(
                uiState = uiState,
                viewModel = viewModel,
                metrics = metrics,
                options =
                    reportOptions,
                onOptionsChanged =
                    onReportOptionsChanged,
                onExportReport =
                    onExportReport
            )
        }
    }
}

@Composable
private fun StatisticsTab(
    uiState: AdultAreaUiState,
    viewModel: AdultAreaViewModel,
    metrics: BoardResponsiveMetrics
) {
    when (uiState) {
        AdultAreaUiState.Loading -> {
            AdultLoadingContent(
                metrics = metrics
            )
        }

        is AdultAreaUiState.Error -> {
            AdultErrorContent(
                message =
                    uiState.message,
                metrics = metrics
            )
        }

        is AdultAreaUiState.Success -> {
            val successState =
                uiState

            val summary =
                remember(
                    successState.filteredHistory,
                    successState.selection
                ) {
                    AdultStatsCalculator.calculate(
                        filteredHistory =
                            successState.filteredHistory,
                        period =
                            successState
                                .selection
                                .period
                    )
                }

            AdultStatsContent(
                selection =
                    successState.selection,
                summary =
                    summary,
                metrics =
                    metrics,
                onSelectionChanged = {
                        selection ->

                    viewModel.setSelection(
                        selection
                    )
                },
                onClearFilters = {
                    viewModel.clearSelection()
                }
            )
        }
    }
}

@Composable
private fun ReportTab(
    uiState: AdultAreaUiState,
    viewModel: AdultAreaViewModel,
    metrics: BoardResponsiveMetrics,
    options: AdultReportOptions,
    onOptionsChanged: (AdultReportOptions) -> Unit,
    onExportReport: (AdultReport) -> Unit
) {
    when (uiState) {
        AdultAreaUiState.Loading -> {
            AdultLoadingContent(
                metrics = metrics
            )
        }

        is AdultAreaUiState.Error -> {
            AdultErrorContent(
                message =
                    uiState.message,
                metrics = metrics
            )
        }

        is AdultAreaUiState.Success -> {
            val successState =
                uiState

            val summary =
                remember(
                    successState.filteredHistory,
                    successState.selection
                ) {
                    AdultStatsCalculator.calculate(
                        filteredHistory =
                            successState.filteredHistory,
                        period =
                            successState
                                .selection
                                .period
                    )
                }

            AdultReportContent(
                selection =
                    successState.selection,
                summary =
                    summary,
                metrics =
                    metrics,
                options =
                    options,
                onSelectionChanged = {
                        selection ->

                    viewModel.setSelection(
                        selection
                    )
                },
                onClearFilters = {
                    viewModel.clearSelection()
                },
                onOptionsChanged =
                    onOptionsChanged,
                onExport = {
                    val report =
                        AdultReportBuilder.build(
                            filteredHistory =
                                successState.filteredHistory,
                            selection =
                                successState.selection,
                            options =
                                options
                        )

                    onExportReport(
                        report
                    )
                }
            )
        }
    }
}

@Composable
private fun AdultTabBar(
    currentTab: AdultTab,
    metrics: BoardResponsiveMetrics,
    onTabSelected: (AdultTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(
                space =
                    metrics.tinySpacing,
                alignment =
                    Alignment.CenterHorizontally
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        AdultTab.entries.forEach { tab ->
            val isSelected =
                tab == currentTab

            val tabModifier =
                if (metrics.isDoubleColumn) {
                    Modifier.weight(1f)
                } else {
                    Modifier
                }

            Box(
                modifier = tabModifier
                    .clip(
                        RoundedCornerShape(
                            topStart =
                                metrics.smallSpacing,
                            topEnd =
                                metrics.smallSpacing,
                            bottomStart =
                                metrics.tinySpacing,
                            bottomEnd =
                                metrics.tinySpacing
                        )
                    )
                    .background(
                        color =
                            if (isSelected) {
                                tab.color.copy(
                                    alpha = 0.9f
                                )
                            } else {
                                tab.color.copy(
                                    alpha = 0.35f
                                )
                            }
                    )
                    .clickable {
                        onTabSelected(
                            tab
                        )
                    }
                    .padding(
                        horizontal =
                            metrics.tinySpacing,
                        vertical =
                            metrics.tinySpacing
                    ),
                contentAlignment =
                    Alignment.Center
            ) {
                Text(
                    text = tab.title,
                    color =
                        if (isSelected) {
                            Color(
                                0xFF24313F
                            )
                        } else {
                            ChalkColors.ChalkWhite
                        },
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        metrics.compactTextSize,
                    fontWeight =
                        if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                    textAlign =
                        TextAlign.Center,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun AdultLoadingContent(
    metrics: BoardResponsiveMetrics
) {
    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text =
                "Loading practice history...",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun AdultErrorContent(
    message: String,
    metrics: BoardResponsiveMetrics
) {
    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = message,
            color =
                ChalkColors.PastelPink,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun PrivacyTab(
    metrics: BoardResponsiveMetrics
) {
    if (metrics.isDoubleColumn) {
        DoubleColumnPrivacyTab(
            metrics = metrics
        )
    } else {
        SingleColumnPrivacyTab(
            metrics = metrics
        )
    }
}

@Composable
private fun SingleColumnPrivacyTab(
    metrics: BoardResponsiveMetrics
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Privacy",
            color =
                ChalkColors.PastelPurple,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Text(
            text =
                "Your practice records stay on this device.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Text(
            text =
                "Arith-Matic does not upload student records or share " +
                        "practice information automatically.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Text(
            text =
                "A report only leaves the device when you choose to export " +
                        "and share it.",
            color =
                ChalkColors.PastelGreen,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun DoubleColumnPrivacyTab(
    metrics: BoardResponsiveMetrics
) {
    Column(
        modifier =
            Modifier.fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = "Privacy",
            color =
                ChalkColors.PastelPurple,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text =
                    "Your practice records stay on this device.",
                modifier =
                    Modifier.weight(1f),
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.bodyTextSize,
                lineHeight =
                    metrics.bodyTextSize * 1.15f,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

            Text(
                text =
                    "Arith-Matic does not upload student records or share " +
                            "practice information automatically.",
                modifier =
                    Modifier.weight(1f),
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.bodyTextSize,
                lineHeight =
                    metrics.bodyTextSize * 1.15f,
                textAlign =
                    TextAlign.Center
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Text(
            text =
                "A report only leaves the device when you choose to export " +
                        "and share it.",
            color =
                ChalkColors.PastelGreen,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center,
            modifier =
                Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SupportTab(
    metrics: BoardResponsiveMetrics
) {
    if (metrics.isDoubleColumn) {
        DoubleColumnSupportTab(
            metrics = metrics
        )
    } else {
        SingleColumnSupportTab(
            metrics = metrics
        )
    }
}

@Composable
private fun SingleColumnSupportTab(
    metrics: BoardResponsiveMetrics
) {
    val uriHandler =
        LocalUriHandler.current

    Column(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Support Arith-Matic",
            color =
                ChalkColors.PastelPink,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Text(
            text =
                "Arith-Matic is developed independently by " +
                        "Wise Raven Studios.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.mediumSpacing
                )
        )

        Text(
            text =
                "Support helps fund continued development, testing, " +
                        "and future educational features.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.compactTextSize,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        ChalkTextAction(
            text =
                "Support on Patreon",
            color =
                ChalkColors.PastelPink,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.actionVerticalPadding,
            paddingBottom =
                metrics.actionVerticalPadding,
            onClick = {
                uriHandler.openUri(
                    WISE_RAVEN_PATREON
                )
            }
        )

        ChalkTextAction(
            text =
                "Visit Website",
            color =
                ChalkColors.PastelOrange,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.actionVerticalPadding,
            paddingBottom =
                metrics.actionVerticalPadding,
            onClick = {
                uriHandler.openUri(
                    ARITH_MATIC_WEBSITE
                )
            }
        )
    }
}

@Composable
private fun DoubleColumnSupportTab(
    metrics: BoardResponsiveMetrics
) {
    val uriHandler =
        LocalUriHandler.current

    Column(
        modifier =
            Modifier.fillMaxSize(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text =
                "Support Arith-Matic",
            color =
                ChalkColors.PastelPink,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text =
                    "Arith-Matic is developed independently by " +
                            "Wise Raven Studios.",
                modifier =
                    Modifier.weight(1f),
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.bodyTextSize,
                lineHeight =
                    metrics.bodyTextSize * 1.15f,
                textAlign =
                    TextAlign.Center
            )

            Text(
                text =
                    "Support helps fund continued development, testing, " +
                            "and future educational features.",
                modifier =
                    Modifier.weight(1f),
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.bodyTextSize,
                lineHeight =
                    metrics.bodyTextSize * 1.15f,
                textAlign =
                    TextAlign.Center
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    metrics.smallSpacing
                )
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text =
                    "Visit Website",
                color =
                    ChalkColors.PastelOrange,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.actionVerticalPadding,
                paddingBottom =
                    metrics.actionVerticalPadding,
                onClick = {
                    uriHandler.openUri(
                        ARITH_MATIC_WEBSITE
                    )
                }
            )

            ChalkTextAction(
                text =
                    "Support on Patreon",
                color =
                    ChalkColors.PastelPink,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.actionVerticalPadding,
                paddingBottom =
                    metrics.actionVerticalPadding,
                onClick = {
                    uriHandler.openUri(
                        WISE_RAVEN_PATREON
                    )
                }
            )
        }
    }
}