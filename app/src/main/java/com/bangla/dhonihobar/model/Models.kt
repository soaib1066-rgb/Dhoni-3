package com.bangla.dhonihobar.model

enum class PlayerColor(val hex: Long, val displayName: String) {
    RED(0xFFEF4444, "লাল"),
    BLUE(0xFF3B82F6, "নীল"),
    GREEN(0xFF10B981, "সবুজ"),
    YELLOW(0xFFF59E0B, "হলুদ")
}

data class Player(
    val id: String,
    val name: String,
    val color: PlayerColor,
    val balance: Int,
    val currentPosition: Int = 0,
    val ownedProperties: List<Int> = emptyList(),
    val inJail: Boolean = false,
    val jailTurnsRemaining: Int = 0,
    val isBankrupt: Boolean = false,
    val isAI: Boolean = false
)

enum class TileType {
    START,
    PROPERTY,
    JAIL,
    CHANCE,
    TAX
}

enum class PropertyGroup {
    CITY_A,
    CITY_B,
    CITY_C,
    TRANSPORT,
    SPECIAL
}

data class BoardTile(
    val id: Int,
    val name: String,
    val type: TileType,
    val group: PropertyGroup? = null,
    val cost: Int = 0,
    val rent: Int = 0,
    val ownerId: String? = null,
    val description: String = ""
)

data class ChanceCard(
    val id: Int,
    val title: String,
    val description: String,
    val amountEffect: Int = 0,
    val moveTarget: Int? = null,
    val isJailMove: Boolean = false
)

data class GameLog(
    val id: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class GameState(
    val players: List<Player> = emptyList(),
    val tiles: List<BoardTile> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val lastDiceRoll: Int? = null,
    val isRolling: Boolean = false,
    val isMoving: Boolean = false,
    val winner: Player? = null,
    val winnerReason: String? = null,
    val pendingPurchase: BoardTile? = null,
    val activeChanceCard: ChanceCard? = null,
    val activeJailChoice: Boolean = false,
    val gameMode: String? = null,
    val turnCount: Int = 1,
    val logs: List<GameLog> = emptyList()
)
