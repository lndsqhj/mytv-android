package top.yogiczy.mytv.tv.ui.screens.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Switch
import top.yogiczy.mytv.core.data.utils.Constants
import top.yogiczy.mytv.core.util.utils.humanizeMs
import top.yogiczy.mytv.tv.ui.material.LocalPopupManager
import top.yogiczy.mytv.tv.ui.material.SimplePopup
import top.yogiczy.mytv.tv.ui.screens.settings.SettingsViewModel
import top.yogiczy.mytv.tv.ui.screens.ui.UiDensityScaleRatioScreen
import top.yogiczy.mytv.tv.ui.screens.ui.UiFontScaleRatioScreen
import top.yogiczy.mytv.tv.ui.utils.Configs
import java.text.DecimalFormat

@Composable
fun SettingsCategoryUI(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(),
) {
    SettingsContentList(modifier) {
        item {
            SettingsListItem(
                headlineContent = "节目进度",
                supportingContent = "在频道项底部显示当前节目进度条",
                trailingContent = {
                    Switch(
                        checked = settingsViewModel.uiShowEpgProgrammeProgress,
                        onCheckedChange = null
                    )
                },
                onSelected = {
                    settingsViewModel.uiShowEpgProgrammeProgress =
                        !settingsViewModel.uiShowEpgProgrammeProgress
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "台标显示",
                trailingContent = {
                    Switch(
                        checked = settingsViewModel.uiShowChannelLogo,
                        onCheckedChange = null
                    )
                },
                onSelected = {
                    settingsViewModel.uiShowChannelLogo = !settingsViewModel.uiShowChannelLogo
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "经典选台界面",
                supportingContent = "将选台界面替换为经典三段式结构",
                trailingContent = {
                    Switch(
                        checked = settingsViewModel.uiUseClassicPanelScreen, onCheckedChange = null
                    )
                },
                onSelected = {
                    settingsViewModel.uiUseClassicPanelScreen =
                        !settingsViewModel.uiUseClassicPanelScreen
                },
            )
        }

        item {
            val timeShowRangeSeconds = Constants.UI_TIME_SCREEN_SHOW_DURATION / 1000

            SettingsListItem(
                headlineContent = "时间显示",
                supportingContent = when (settingsViewModel.uiTimeShowMode) {
                    Configs.UiTimeShowMode.HIDDEN -> "不显示时间"
                    Configs.UiTimeShowMode.ALWAYS -> "总是显示时间"
                    Configs.UiTimeShowMode.EVERY_HOUR -> "整点前后${timeShowRangeSeconds}s显示时间"
                    Configs.UiTimeShowMode.HALF_HOUR -> "半点前后${timeShowRangeSeconds}s显示时间"
                },
                trailingContent = when (settingsViewModel.uiTimeShowMode) {
                    Configs.UiTimeShowMode.HIDDEN -> "隐藏"
                    Configs.UiTimeShowMode.ALWAYS -> "常显"
                    Configs.UiTimeShowMode.EVERY_HOUR -> "整点"
                    Configs.UiTimeShowMode.HALF_HOUR -> "半点"
                },
                onSelected = {
                    settingsViewModel.uiTimeShowMode =
                        Configs.UiTimeShowMode.entries.let {
                            it[(it.indexOf(settingsViewModel.uiTimeShowMode) + 1) % it.size]
                        }
                },
            )
        }

        item {
            SettingsListItem(
                headlineContent = "超时自动关闭界面",
                supportingContent = "影响选台界面，快捷操作界面",
                trailingContent = Constants.UI_SCREEN_AUTO_CLOSE_DELAY.humanizeMs(),
                locK = true,
            )
        }

        item {
            val popupManager = LocalPopupManager.current
            val focusRequester = remember { FocusRequester() }
            var visible by remember { mutableStateOf(false) }

            SettingsListItem(
                modifier = Modifier.focusRequester(focusRequester),
                headlineContent = "界面整体缩放比例",
                trailingContent = when (settingsViewModel.uiDensityScaleRatio) {
                    0f -> "自适应"
                    else -> "×${DecimalFormat("#.#").format(settingsViewModel.uiDensityScaleRatio)}"
                },
                onSelected = {
                    popupManager.push(focusRequester, true)
                    visible = true
                },
                remoteConfig = true,
            )

            SimplePopup(
                visibleProvider = { visible },
                onDismissRequest = { visible = false },
            ) {
                UiDensityScaleRatioScreen(
                    currentScaleRatioProvider = { settingsViewModel.uiDensityScaleRatio },
                    onScaleRatioSelected = {
                        settingsViewModel.uiDensityScaleRatio = it
                        visible = false
                    },
                    onClose = { visible = false },
                )
            }
        }

        item {
            val popupManager = LocalPopupManager.current
            val focusRequester = remember { FocusRequester() }
            var visible by remember { mutableStateOf(false) }

            SettingsListItem(
                modifier = Modifier.focusRequester(focusRequester),
                headlineContent = "界面字体缩放比例",
                trailingContent = "×${DecimalFormat("#.#").format(settingsViewModel.uiFontScaleRatio)}",
                onSelected = {
                    popupManager.push(focusRequester, true)
                    visible = true
                },
                remoteConfig = true,
            )

            SimplePopup(
                visibleProvider = { visible },
                onDismissRequest = { visible = false },
            ) {
                UiFontScaleRatioScreen(
                    currentScaleRatioProvider = { settingsViewModel.uiFontScaleRatio },
                    onScaleRatioSelected = {
                        settingsViewModel.uiFontScaleRatio = it
                        visible = false
                    },
                    onClose = { visible = false },
                )
            }
        }
    }
}