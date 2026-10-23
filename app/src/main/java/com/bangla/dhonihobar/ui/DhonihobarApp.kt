package com.bangla.dhonihobar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangla.dhonihobar.viewmodel.GameViewModel

@Composable
fun DhonihobarApp(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.gameState.collectAsState()

    if (state.gameMode == null) {
        HomeScreen(
            hasSavedGame = viewModel.hasSavedGame,
            onResumeGame = { viewModel.resumeGame() },
            onStartBot = { humanName, botName -> viewModel.startBotGame(humanName, botName) },
            onStartMultiplayer = { names -> viewModel.startMultiplayerGame(names) }
        )
    } else {
        BoardScreen(
            state = state,
            onRollDice = { viewModel.rollDice() },
            onBuyProperty = { viewModel.buyProperty() },
            onPassProperty = { viewModel.passProperty() },
            onPayFine = { viewModel.payJailFine() },
            onWaitTurn = { viewModel.waitJailTurn() },
            onDismissChance = {
                state.activeChanceCard?.let { viewModel.executeChanceCard(it) }
            },
            onRestart = {
                if (state.gameMode == "bot") {
                    val p1 = state.players.getOrNull(0)?.name ?: "আপনি"
                    val p2 = state.players.getOrNull(1)?.name ?: "কম্পিউটার বট"
                    viewModel.startBotGame(p1, p2)
                } else {
                    val names = state.players.map { it.name }
                    viewModel.startMultiplayerGame(names)
                }
            },
            onGoHome = { viewModel.resetToHome() }
        )
    }
}
