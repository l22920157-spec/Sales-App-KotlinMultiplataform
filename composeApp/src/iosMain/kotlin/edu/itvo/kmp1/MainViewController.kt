package edu.itvo.kmp1

import androidx.compose.ui.window.ComposeUIViewController
import edu.itvo.kmp1.shared.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}