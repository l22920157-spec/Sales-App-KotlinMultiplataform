package edu.itvo.kmp1

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import edu.itvo.kmp1.shared.data.local.database.DatabaseDriverFactory
import edu.itvo.kmp1.shared.di.initKoin
import edu.itvo.kmp1.shared.di.wasmJsDatabaseModule
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    MainScope().launch {
        val db = DatabaseDriverFactory().createDatabase()
        initKoin {
            modules(wasmJsDatabaseModule(db))
        }
        ComposeViewport("ComposeTarget") {
            App()
        }
    }
}
