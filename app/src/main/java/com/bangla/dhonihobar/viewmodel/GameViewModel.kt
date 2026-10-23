package com.bangla.dhonihobar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangla.dhonihobar.data.DefaultGameData
import com.bangla.dhonihobar.model.*
import com.bangla.dhonihobar.util.BanglaFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var savedGameSnapshot: GameState? = null

    val hasSavedGame: Boolean
        get() = savedGameSnapshot != null

    fun startBotGame(humanName: String = "আপনি", botName: String = "এআই রোবট") {
        val players = listOf(
            Player(
                id = "p1",
                name = humanName.ifBlank { "আপনি" },
                color = PlayerColor.RED,
                balance = DefaultGameData.STARTING_BALANCE,
                isAI = false
            ),
            Player(
                id = "p2",
                name = botName.ifBlank { "এআই রোবট" },
                color = PlayerColor.BLUE,
                balance = DefaultGameData.STARTING_BALANCE,
                isAI = true
            )
        )
        val initialLogs = listOf(
            GameLog("log-1", "বটের সঙ্গে খেলা শুরু হলো! প্রতি খেলোয়াড়ের তহবিল ৳১৫,০০০।"),
            GameLog("log-2", "জয়লাভের লক্ষ্য: আগে ৳৩০,০০০ অর্জন বা একমাত্র টিকে থাকা!")
        )
        val newState = GameState(
            players = players,
            tiles = DefaultGameData.TILES,
            currentPlayerIndex = 0,
            gameMode = "bot",
            logs = initialLogs
        )
        _gameState.value = newState
        savedGameSnapshot = newState
    }

    fun startMultiplayerGame(names: List<String>) {
        val colors = listOf(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW)
        val defaultNames = listOf("রাকিব", "সাকিব", "আসিফ", "তানভীর")

        val players = names.mapIndexed { i, name ->
            val finalName = name.ifBlank { defaultNames.getOrElse(i) { "খেলোয়াড় ${i + 1}" } }
            Player(
                id = "p${i + 1}",
                name = finalName,
                color = colors[i % colors.size],
                balance = DefaultGameData.STARTING_BALANCE,
                isAI = false
            )
        }

        val initialLogs = listOf(
            GameLog("log-1", "${BanglaFormatter.toBanglaNumber(players.size)} জন খেলোয়াড়ের খেলা শুরু হলো!"),
            GameLog("log-2", "জয়লাভের লক্ষ্য: আগে ৳৩০,০০০ অর্জন বা একমাত্র টিকে থাকা!")
        )
        val newState = GameState(
            players = players,
            tiles = DefaultGameData.TILES,
            currentPlayerIndex = 0,
            gameMode = "multiplayer",
            logs = initialLogs
        )
        _gameState.value = newState
        savedGameSnapshot = newState
    }

    fun startMultiplayerGame(count: Int) {
        val defaultNames = listOf("রাকিব", "সাকিব", "আসিফ", "তানভীর").take(count)
        startMultiplayerGame(defaultNames)
    }

    fun resumeGame() {
        savedGameSnapshot?.let {
            _gameState.value = it
        }
    }

    fun resetToHome() {
        // Keep snapshot for resume if still in progress
        if (_gameState.value.winner == null && _gameState.value.gameMode != null) {
            savedGameSnapshot = _gameState.value
        }
        _gameState.value = GameState()
    }

    private fun checkVictory(players: List<Player>): Pair<Player, String>? {
        // 1. Reached target ৳30,000
        val targetWinner = players.find { !it.isBankrupt && it.balance >= DefaultGameData.TARGET_WIN_BALANCE }
        if (targetWinner != null) {
            return Pair(targetWinner, "সর্বপ্রথম ৳৩০,০০০ মূলধন পূর্ণ করে দ্রুততম ধনী হয়েছেন!")
        }

        // 2. All opponents went bankrupt
        val activePlayers = players.filter { !it.isBankrupt }
        if (activePlayers.size == 1 && players.size > 1) {
            return Pair(activePlayers.first(), "অন্য সব প্রতিদ্বন্দ্বীদের দেউলিয়া করে সর্বশেষ টিকে থেকে বিজয়ী হয়েছেন!")
        }

        return null
    }

    fun rollDice() {
        val current = _gameState.value
        if (current.isRolling || current.isMoving || current.pendingPurchase != null || current.activeChanceCard != null) return

        val playerIndex = current.currentPlayerIndex
        val player = current.players.getOrNull(playerIndex) ?: return

        if (player.inJail) {
            if (player.isAI) {
                if (player.balance >= DefaultGameData.JAIL_FINE * 2) {
                    payJailFine()
                } else {
                    waitJailTurn()
                    return
                }
            } else {
                _gameState.update { it.copy(activeJailChoice = true) }
                return
            }
        }

        viewModelScope.launch {
            _gameState.update { it.copy(isRolling = true) }
            delay(500)
            val roll = Random.nextInt(1, 7)

            _gameState.update { state ->
                val newLog = GameLog(
                    id = "roll-${System.currentTimeMillis()}",
                    text = "${player.name} ছক্কায় ${BanglaFormatter.toBanglaNumber(roll)} পেয়েছেন।"
                )
                state.copy(
                    lastDiceRoll = roll,
                    isRolling = false,
                    isMoving = true,
                    logs = state.logs.takeLast(20) + newLog
                )
            }

            // Step by step movement
            var pos = player.currentPosition
            var bonusAwarded = false
            for (step in 1..roll) {
                delay(380) // Slower, deliberate token step movement
                val prev = pos
                pos = (pos + 1) % 20

                if (pos == 0 || (prev > pos && !bonusAwarded)) {
                    bonusAwarded = true
                    _gameState.update { s ->
                        val updated = s.players.toMutableList()
                        val p = updated[playerIndex]
                        val newBal = p.balance + DefaultGameData.START_PASS_BONUS
                        updated[playerIndex] = p.copy(balance = newBal)

                        val victory = checkVictory(updated)
                        if (victory != null) {
                            s.copy(
                                players = updated,
                                winner = victory.first,
                                winnerReason = victory.second,
                                logs = s.logs.takeLast(20) + GameLog("victory", "অভিনন্দন! ${victory.first.name} ${victory.second}")
                            )
                        } else {
                            val log = GameLog("start-bonus", "${p.name} শুরু ঘর অতিক্রম করায় ৳২,০০০ বোনাস পেলেন!")
                            s.copy(players = updated, logs = s.logs.takeLast(20) + log)
                        }
                    }
                }

                _gameState.update { s ->
                    val updated = s.players.toMutableList()
                    updated[playerIndex] = updated[playerIndex].copy(currentPosition = pos)
                    s.copy(players = updated)
                }
            }

            _gameState.update { it.copy(isMoving = false) }
            savedGameSnapshot = _gameState.value

            if (_gameState.value.winner != null) return@launch

            delay(550) // Comfortable pause upon landing on destination tile
            handleLanding(playerIndex, pos)
        }
    }

    private fun handleLanding(playerIndex: Int, tileId: Int) {
        val state = _gameState.value
        val player = state.players[playerIndex]
        val tile = state.tiles[tileId]

        when (tile.type) {
            TileType.START -> {
                advanceTurn()
            }
            TileType.JAIL -> {
                _gameState.update { s ->
                    val updated = s.players.toMutableList()
                    updated[playerIndex] = updated[playerIndex].copy(inJail = true, jailTurnsRemaining = 1)
                    val log = GameLog("jail", "${player.name} জেলখানায় আটকে গেলেন!")
                    s.copy(players = updated, logs = s.logs.takeLast(20) + log)
                }
                advanceTurn()
            }
            TileType.TAX -> {
                val newBal = player.balance - DefaultGameData.TAX_AMOUNT
                if (newBal < 0) {
                    handleBankruptcy(playerIndex)
                } else {
                    _gameState.update { s ->
                        val updated = s.players.toMutableList()
                        updated[playerIndex] = updated[playerIndex].copy(balance = newBal)
                        val log = GameLog("tax", "${player.name} কর বাবদ ৳১,০০০ পরিশোধ করলেন।")
                        s.copy(players = updated, logs = s.logs.takeLast(20) + log)
                    }
                    advanceTurn()
                }
            }
            TileType.CHANCE -> {
                val card = DefaultGameData.CHANCE_CARDS.random()
                if (player.isAI) {
                    executeChanceCard(card)
                } else {
                    _gameState.update { it.copy(activeChanceCard = card) }
                }
            }
            TileType.PROPERTY -> {
                if (tile.ownerId == null) {
                    if (player.isAI) {
                        if (player.balance >= tile.cost) {
                            viewModelScope.launch {
                                delay(800)
                                buyProperty()
                            }
                        } else {
                            advanceTurn()
                        }
                    } else {
                        _gameState.update { it.copy(pendingPurchase = tile) }
                    }
                } else if (tile.ownerId != player.id) {
                    // Pay rent
                    val ownerIdx = state.players.indexOfFirst { it.id == tile.ownerId }
                    if (ownerIdx != -1) {
                        val rent = tile.rent
                        if (player.balance < rent) {
                            handleBankruptcy(playerIndex)
                        } else {
                            _gameState.update { s ->
                                val updated = s.players.toMutableList()
                                updated[playerIndex] = updated[playerIndex].copy(balance = updated[playerIndex].balance - rent)
                                updated[ownerIdx] = updated[ownerIdx].copy(balance = updated[ownerIdx].balance + rent)

                                val victory = checkVictory(updated)
                                if (victory != null) {
                                    s.copy(
                                        players = updated,
                                        winner = victory.first,
                                        winnerReason = victory.second,
                                        logs = s.logs.takeLast(20) + GameLog("victory", "অভিনন্দন! ${victory.first.name} ${victory.second}")
                                    )
                                } else {
                                    val log = GameLog("rent", "${player.name} ${updated[ownerIdx].name}-কে ${BanglaFormatter.formatBanglaTaka(rent)} ভাড়া দিলেন।")
                                    s.copy(players = updated, logs = s.logs.takeLast(20) + log)
                                }
                            }
                            if (_gameState.value.winner == null) {
                                advanceTurn()
                            }
                        }
                    } else {
                        advanceTurn()
                    }
                } else {
                    advanceTurn()
                }
            }
        }
    }

    fun buyProperty() {
        val state = _gameState.value
        val playerIdx = state.currentPlayerIndex
        val player = state.players[playerIdx]
        val prop = state.pendingPurchase ?: state.tiles[player.currentPosition]

        if (prop.ownerId == null && player.balance >= prop.cost) {
            _gameState.update { s ->
                val updatedPlayers = s.players.toMutableList()
                updatedPlayers[playerIdx] = player.copy(
                    balance = player.balance - prop.cost,
                    ownedProperties = player.ownedProperties + prop.id
                )
                val updatedTiles = s.tiles.map {
                    if (it.id == prop.id) it.copy(ownerId = player.id) else it
                }
                val log = GameLog("buy", "${player.name} ${BanglaFormatter.formatBanglaTaka(prop.cost)}-এ '${prop.name}' কিনলেন!")
                s.copy(
                    players = updatedPlayers,
                    tiles = updatedTiles,
                    pendingPurchase = null,
                    logs = s.logs.takeLast(20) + log
                )
            }
        }
        advanceTurn()
    }

    fun passProperty() {
        _gameState.update { it.copy(pendingPurchase = null) }
        advanceTurn()
    }

    fun payJailFine() {
        val state = _gameState.value
        val playerIdx = state.currentPlayerIndex
        val player = state.players[playerIdx]

        if (player.balance >= DefaultGameData.JAIL_FINE) {
            _gameState.update { s ->
                val updated = s.players.toMutableList()
                updated[playerIdx] = player.copy(
                    balance = player.balance - DefaultGameData.JAIL_FINE,
                    inJail = false,
                    jailTurnsRemaining = 0
                )
                val log = GameLog("fine", "${player.name} জরিমানা দিয়ে জেল থেকে মুক্ত হলেন।")
                s.copy(players = updated, activeJailChoice = false, logs = s.logs.takeLast(20) + log)
            }
        }
    }

    fun waitJailTurn() {
        val state = _gameState.value
        val playerIdx = state.currentPlayerIndex
        val player = state.players[playerIdx]

        _gameState.update { s ->
            val updated = s.players.toMutableList()
            updated[playerIdx] = player.copy(inJail = false, jailTurnsRemaining = 0)
            val log = GameLog("jail-wait", "${player.name} ১ টার্ন বিরতি শেষ করলেন।")
            s.copy(players = updated, activeJailChoice = false, logs = s.logs.takeLast(20) + log)
        }
        advanceTurn()
    }

    fun executeChanceCard(card: ChanceCard) {
        val state = _gameState.value
        val playerIdx = state.currentPlayerIndex
        val player = state.players[playerIdx]

        var newBal = player.balance + card.amountEffect
        var targetPos = player.currentPosition

        if (card.moveTarget != null) {
            targetPos = card.moveTarget
            if (targetPos == 0) {
                newBal += DefaultGameData.START_PASS_BONUS
            }
        }

        if (newBal < 0) {
            handleBankruptcy(playerIdx)
            return
        }

        _gameState.update { s ->
            val updated = s.players.toMutableList()
            updated[playerIdx] = player.copy(
                balance = newBal,
                currentPosition = targetPos,
                inJail = if (card.isJailMove) true else player.inJail
            )

            val victory = checkVictory(updated)
            if (victory != null) {
                s.copy(
                    players = updated,
                    activeChanceCard = null,
                    winner = victory.first,
                    winnerReason = victory.second,
                    logs = s.logs.takeLast(20) + GameLog("victory", "অভিনন্দন! ${victory.first.name} ${victory.second}")
                )
            } else {
                val log = GameLog("chance", "ভাগ্য পরীক্ষা: ${player.name} '${card.title}' পেয়েছেন।")
                s.copy(players = updated, activeChanceCard = null, logs = s.logs.takeLast(20) + log)
            }
        }
        if (_gameState.value.winner == null) {
            advanceTurn()
        }
    }

    private fun handleBankruptcy(playerIdx: Int) {
        _gameState.update { s ->
            val updated = s.players.toMutableList()
            val bankrupt = updated[playerIdx].copy(isBankrupt = true, balance = 0)
            updated[playerIdx] = bankrupt

            // Free properties
            val updatedTiles = s.tiles.map {
                if (it.ownerId == bankrupt.id) it.copy(ownerId = null) else it
            }

            val log = GameLog("bankrupt", "${bankrupt.name} দেউলিয়া ঘোষিত হলেন!")
            val victory = checkVictory(updated)

            s.copy(
                players = updated,
                tiles = updatedTiles,
                winner = victory?.first,
                winnerReason = victory?.second,
                logs = s.logs.takeLast(20) + log
            )
        }
        if (_gameState.value.winner == null) {
            advanceTurn()
        }
    }

    private fun advanceTurn() {
        val state = _gameState.value
        if (state.winner != null) return

        var nextIdx = (state.currentPlayerIndex + 1) % state.players.size
        var loop = 0
        while (state.players[nextIdx].isBankrupt && loop < state.players.size) {
            nextIdx = (nextIdx + 1) % state.players.size
            loop++
        }

        _gameState.update {
            it.copy(
                currentPlayerIndex = nextIdx,
                turnCount = it.turnCount + 1,
                pendingPurchase = null,
                activeChanceCard = null,
                activeJailChoice = false
            )
        }
        savedGameSnapshot = _gameState.value

        // Check if next player is AI
        val nextPlayer = _gameState.value.players[nextIdx]
        if (nextPlayer.isAI && !nextPlayer.isBankrupt && _gameState.value.winner == null) {
            viewModelScope.launch {
                delay(1500) // Clear pause before AI takes action
                rollDice()
            }
        }
    }
}
