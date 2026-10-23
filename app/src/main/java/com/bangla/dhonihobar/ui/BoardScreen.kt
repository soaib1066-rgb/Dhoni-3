package com.bangla.dhonihobar.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangla.dhonihobar.model.*
import com.bangla.dhonihobar.ui.theme.*
import com.bangla.dhonihobar.util.BanglaFormatter

/**
 * 2D 20-Tile Ring Grid Coordinates (6x6 perimeter in clockwise order)
 * Returns Pair(col, row) where col in 0..5, row in 0..5
 */
fun getTileGridCoord(tileId: Int): Pair<Int, Int> {
    return when (tileId) {
        0 -> Pair(0, 0)   // শুরু (Top-Left corner)
        1 -> Pair(1, 0)
        2 -> Pair(2, 0)
        3 -> Pair(3, 0)
        4 -> Pair(4, 0)
        5 -> Pair(5, 0)   // জেলখানা (Top-Right corner)
        6 -> Pair(5, 1)
        7 -> Pair(5, 2)
        8 -> Pair(5, 3)
        9 -> Pair(5, 4)
        10 -> Pair(5, 5)  // ভাগ্য পরীক্ষা (Bottom-Right corner)
        11 -> Pair(4, 5)
        12 -> Pair(3, 5)
        13 -> Pair(2, 5)
        14 -> Pair(1, 5)
        15 -> Pair(0, 5)  // কর / জরিমানা (Bottom-Left corner)
        16 -> Pair(0, 4)
        17 -> Pair(0, 3)
        18 -> Pair(0, 2)
        19 -> Pair(0, 1)
        else -> Pair(0, 0)
    }
}

/**
 * Top stripe color for property groups and corner categories
 */
fun getTileHeaderColor(tile: BoardTile): Color {
    return when (tile.type) {
        TileType.START -> Color(0xFF059669)   // Emerald Green
        TileType.JAIL -> Color(0xFFE11D48)    // Rose Red
        TileType.CHANCE -> Color(0xFFD97706)  // Amber Gold
        TileType.TAX -> Color(0xFFEA580C)     // Deep Orange
        TileType.PROPERTY -> {
            when (tile.group) {
                PropertyGroup.CITY_A -> Color(0xFF9333EA)    // Purple
                PropertyGroup.CITY_B -> Color(0xFF0D9488)    // Teal
                PropertyGroup.CITY_C -> Color(0xFF2563EB)    // Royal Blue
                PropertyGroup.TRANSPORT -> Color(0xFF4F46E5) // Indigo
                PropertyGroup.SPECIAL -> Color(0xFFB45309)   // Amber Dark
                null -> Color(0xFF475569)
            }
        }
    }
}

/**
 * Icon symbol for each tile
 */
fun getTileSymbol(tile: BoardTile): String {
    return when (tile.type) {
        TileType.START -> "🚩"
        TileType.JAIL -> "🔒"
        TileType.CHANCE -> "✨"
        TileType.TAX -> "🏛️"
        TileType.PROPERTY -> {
            when (tile.group) {
                PropertyGroup.TRANSPORT -> {
                    when (tile.id) {
                        3 -> "🚆"
                        13 -> "✈️"
                        18 -> "🌉"
                        else -> "🚆"
                    }
                }
                PropertyGroup.SPECIAL -> {
                    if (tile.id == 8) "⚡" else "🏖️"
                }
                else -> "🏢"
            }
        }
    }
}

/**
 * Main Board Screen displaying the 2D Square Board, Virtual Dice, and Bottom Dashboard
 */
@Composable
fun BoardScreen(
    state: GameState,
    onRollDice: () -> Unit,
    onBuyProperty: () -> Unit,
    onPassProperty: () -> Unit,
    onPayFine: () -> Unit,
    onWaitTurn: () -> Unit,
    onDismissChance: () -> Unit,
    onRestart: () -> Unit,
    onGoHome: () -> Unit = {}
) {
    val activePlayer = state.players.getOrNull(state.currentPlayerIndex) ?: return
    val scrollState = rememberScrollState()

    var showQuitDialog by remember { mutableStateOf(false) }
    var showRestartDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var soundEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SlateSurface)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // গেম কাটুন (Quit / Exit) Button
                OutlinedButton(
                    onClick = { showQuitDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = PlayerRed.copy(alpha = 0.2f),
                        contentColor = PlayerRed
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PlayerRed.copy(alpha = 0.6f))
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "গেম কাটুন",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFCA5A5)
                    )
                }

                // রেস্টার্ট (Restart) Button
                OutlinedButton(
                    onClick = { showRestartDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = GoldAccent.copy(alpha = 0.2f),
                        contentColor = GoldAccent
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GoldAccent.copy(alpha = 0.6f))
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "রেস্টার্ট",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }

                // সেটিংস (Settings) Button
                IconButton(
                    onClick = { showSettingsDialog = true },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "সেটিংস",
                        tint = EmeraldPrimary
                    )
                }
            }
        },
        containerColor = SlateBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ১. চারকোনা বোর্ড লেআউট (Responsive 2D Square Board)
            SquareGameBoard(
                tiles = state.tiles,
                players = state.players,
                activePlayer = activePlayer,
                lastDiceRoll = state.lastDiceRoll,
                isRolling = state.isRolling,
                isMoving = state.isMoving,
                onRollDice = onRollDice,
                latestLog = state.logs.lastOrNull()?.text
            )

            // ২. নিচের ড্যাশবোর্ড (Player HUD Dashboard)
            PlayerHudDashboard(
                players = state.players,
                activePlayerIndex = state.currentPlayerIndex
            )

            // ৩. খেলার সাম্প্রতিক বিবরণী (Recent Activity Logs)
            RecentLogsCard(logs = state.logs)
        }
    }

    // Interactive Dialogs
    PropertyPurchaseDialog(
        tile = state.pendingPurchase,
        player = activePlayer,
        onBuy = onBuyProperty,
        onPass = onPassProperty
    )

    ChanceCardDialog(
        card = state.activeChanceCard,
        onDismiss = onDismissChance
    )

    if (state.activeJailChoice) {
        JailDialog(
            player = activePlayer,
            onPayFine = onPayFine,
            onWait = onWaitTurn
        )
    }

    WinnerDialog(
        winner = state.winner,
        reason = state.winnerReason,
        onRestart = onRestart
    )

    if (showQuitDialog) {
        QuitConfirmDialog(
            onConfirm = {
                showQuitDialog = false
                onGoHome()
            },
            onDismiss = { showQuitDialog = false }
        )
    }

    if (showRestartDialog) {
        RestartConfirmDialog(
            onConfirm = {
                showRestartDialog = false
                onRestart()
            },
            onDismiss = { showRestartDialog = false }
        )
    }

    if (showSettingsDialog) {
        SettingsDialog(
            soundEnabled = soundEnabled,
            onToggleSound = { soundEnabled = !soundEnabled },
            onDismiss = { showSettingsDialog = false }
        )
    }
}

/**
 * Responsive 2D Square Board with 20 Perimeter Tiles + 4x4 Center Area
 */
@Composable
fun SquareGameBoard(
    tiles: List<BoardTile>,
    players: List<Player>,
    activePlayer: Player,
    lastDiceRoll: Int?,
    isRolling: Boolean,
    isMoving: Boolean,
    onRollDice: () -> Unit,
    latestLog: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(EmeraldPrimary.copy(alpha = 0.6f), GoldAccent.copy(alpha = 0.4f))
            )
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            val totalSize = maxWidth
            val cellSize = totalSize / 6f

            // ১. রিং আকারে ২০টি টাইলস সাজানো
            tiles.forEach { tile ->
                val (col, row) = getTileGridCoord(tile.id)
                val playersOnTile = players.filter { !it.isBankrupt && it.currentPosition == tile.id }
                val owner = tile.ownerId?.let { oId -> players.find { it.id == oId } }

                Box(
                    modifier = Modifier
                        .offset(x = cellSize * col, y = cellSize * row)
                        .size(cellSize)
                        .padding(1.dp)
                ) {
                    TileCardView(
                        tile = tile,
                        playersOnTile = playersOnTile,
                        owner = owner
                    )
                }
            }

            // ২. বোর্ডের কেন্দ্রভাগ (৪x৪ সেন্ট্রাল ড্যাশবোর্ড)
            Box(
                modifier = Modifier
                    .offset(x = cellSize * 1, y = cellSize * 1)
                    .size(width = cellSize * 4, height = cellSize * 4)
                    .padding(3.dp)
            ) {
                CenterDashboardView(
                    activePlayer = activePlayer,
                    lastDiceRoll = lastDiceRoll,
                    isRolling = isRolling,
                    isMoving = isMoving,
                    onRollDice = onRollDice,
                    latestLog = latestLog
                )
            }
        }
    }
}

/**
 * Individual Tile Card with Color Stripe, Title, Price, and Player Tokens
 */
@Composable
fun TileCardView(
    tile: BoardTile,
    playersOnTile: List<Player>,
    owner: Player?
) {
    val isCorner = tile.type == TileType.START || tile.type == TileType.JAIL ||
            tile.type == TileType.CHANCE || tile.type == TileType.TAX
    val isOwned = owner != null

    // Distinct background color for corners or owned properties
    val cornerBgColor = when (tile.type) {
        TileType.START -> Color(0xFF064E3B)   // Rich Dark Emerald
        TileType.JAIL -> Color(0xFF881337)    // Rich Dark Rose
        TileType.CHANCE -> Color(0xFF78350F)  // Rich Dark Amber
        TileType.TAX -> Color(0xFF7C2D12)     // Rich Dark Orange
        else -> Color(0xFF0F172A)             // Dark Slate
    }

    val tileBgColor = when {
        isOwned -> Color(owner!!.color.hex)
        isCorner -> cornerBgColor
        else -> Color(0xFF0F172A)
    }

    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(7.dp),
        colors = CardDefaults.cardColors(containerColor = tileBgColor),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isOwned) Color.White else if (isCorner) GoldAccent.copy(alpha = 0.5f) else Color(0xFF334155)
            )
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Color Strip with Owner Flag or Crown
            if (isOwned) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.5.dp)
                        .background(Color.Black.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👑 ${owner!!.name}",
                        fontSize = 5.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.5.dp)
                        .background(getTileHeaderColor(tile)),
                    contentAlignment = Alignment.CenterEnd
                )
            }

            // Middle Content: Symbol + Name + Price inside dark contrast surface when owned
            Surface(
                color = if (isOwned) Color.Black.copy(alpha = 0.55f) else Color.Transparent,
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier.padding(horizontal = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 1.dp, vertical = 0.5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = getTileSymbol(tile),
                        fontSize = 9.sp
                    )

                    Text(
                        text = tile.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )

                    when {
                        isOwned -> {
                            Text(
                                text = "ভাড়া: ${BanglaFormatter.formatBanglaTaka(tile.rent)}",
                                fontSize = 6.5.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldAccent
                            )
                        }
                        tile.type == TileType.START -> {
                            Text(
                                text = "+৳২,০০০",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                        }
                        tile.type == TileType.TAX -> {
                            Text(
                                text = "-৳১,০০০",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFB923C)
                            )
                        }
                        tile.cost > 0 -> {
                            Text(
                                text = BanglaFormatter.formatBanglaTaka(tile.cost),
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        }
                        else -> {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }
            }

            // ৩. রঙিন ঘুঁটি (Player Tokens visible on this tile)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(13.dp)
                    .padding(horizontal = 1.dp, vertical = 0.5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                playersOnTile.forEach { p ->
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .clip(CircleShape)
                            .background(Color(p.color.hex))
                            .border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = p.name.take(1),
                            color = Color.White,
                            fontSize = 6.5.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(1.dp))
                }
            }
        }
    }
}

/**
 * Center Area: Active Player Banner, Large Virtual 3D Dice, and Roll Button
 */
@Composable
fun CenterDashboardView(
    activePlayer: Player,
    lastDiceRoll: Int?,
    isRolling: Boolean,
    isMoving: Boolean,
    onRollDice: () -> Unit,
    latestLog: String?
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF030712).copy(alpha = 0.95f)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary.copy(alpha = 0.35f))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Active Player Turn Banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .background(Color(0xFF111827), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(activePlayer.color.hex).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(activePlayer.color.hex))
                        .border(1.dp, Color.White, CircleShape)
                )
                Text(
                    text = "${activePlayer.name}-এর পালা",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    fontSize = 11.5.sp
                )
            }

            // ২. ভার্চুয়াল থ্রি-ডি ছক্কা (3D Virtual Dice)
            Virtual3DDice(
                value = lastDiceRoll ?: 1,
                isRolling = isRolling
            )

            // বড় সাইজের "ছক্কা রোল করুন" বাটন
            Button(
                onClick = onRollDice,
                enabled = !isRolling && !isMoving && !activePlayer.isAI,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    disabledContainerColor = Color(0xFF334155)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text(
                        text = when {
                            isRolling -> "ছক্কা ঘুরছে..."
                            isMoving -> "ঘুঁটি চলছে..."
                            activePlayer.isAI -> "এআই ভাবছে..."
                            else -> "ছক্কা রোল করুন"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            // Bottom Live Event Ticker
            Text(
                text = when {
                    isMoving -> "ঘুঁটি সঠিক ঘরে এগিয়ে চলেছে..."
                    latestLog != null -> latestLog
                    else -> "ছক্কা ঘুরিয়ে নতুন চাল দিন"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (isMoving) GoldAccent else TextMuted,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 3D-Styled Animated Virtual Dice with Dots (1 to 6)
 */
@Composable
fun Virtual3DDice(
    value: Int,
    isRolling: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dice_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val currentRotation = if (isRolling) rotation else 0f

    Box(
        modifier = Modifier
            .size(54.dp)
            .rotate(currentRotation)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF3C7)) // Cream amber dice face
            .border(2.dp, Color(0xFFF59E0B), RoundedCornerShape(12.dp))
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        // 3x3 Dot Grid representing real dice face
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val dots = getDiceDots(value)

            // Row 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DiceDot(visible = dots.contains(0))
                DiceDot(visible = dots.contains(1))
                DiceDot(visible = dots.contains(2))
            }

            // Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DiceDot(visible = dots.contains(3))
                DiceDot(visible = dots.contains(4))
                DiceDot(visible = dots.contains(5))
            }

            // Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DiceDot(visible = dots.contains(6))
                DiceDot(visible = dots.contains(7))
                DiceDot(visible = dots.contains(8))
            }
        }
    }
}

@Composable
fun DiceDot(visible: Boolean) {
    Box(
        modifier = Modifier
            .size(7.5.dp)
            .clip(CircleShape)
            .background(if (visible) Color(0xFFDC2626) else Color.Transparent) // Red dots on dice
    )
}

/**
 * Matrix indices for dice dots (0..8)
 * 0 1 2
 * 3 4 5
 * 6 7 8
 */
fun getDiceDots(value: Int): List<Int> {
    return when (value) {
        1 -> listOf(4)
        2 -> listOf(0, 8)
        3 -> listOf(0, 4, 8)
        4 -> listOf(0, 2, 6, 8)
        5 -> listOf(0, 2, 4, 6, 8)
        6 -> listOf(0, 2, 3, 5, 6, 8)
        else -> listOf(4)
    }
}

/**
 * ৪. নিচের ড্যাশবোর্ড (Player HUD Dashboard with Bangla Balances)
 */
@Composable
fun PlayerHudDashboard(
    players: List<Player>,
    activePlayerIndex: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "খেলোয়াড়দের বিবরণ ও নগদ তহবিল",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(players) { player ->
                    val isActive = players.indexOf(player) == activePlayerIndex
                    val playerColor = Color(player.color.hex)

                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isActive) Color(0xFF1E293B) else Color(0xFF0F172A)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (isActive) EmeraldPrimary else Color(0xFF334155)
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(playerColor)
                                        .border(1.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = player.name.take(1),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = player.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isActive) GoldAccent else TextLight,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Balance
                            Text(
                                text = BanglaFormatter.formatBanglaTaka(player.balance),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Black,
                                color = if (player.isBankrupt) PlayerRed else EmeraldPrimary
                            )

                            // Status flags
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${BanglaFormatter.toBanglaNumber(player.ownedProperties.size)}টি জমি",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )

                                when {
                                    player.isBankrupt -> {
                                        Text(
                                            text = "💀 দেউলিয়া",
                                            fontSize = 10.sp,
                                            color = PlayerRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    player.inJail -> {
                                        Text(
                                            text = "🔒 জেলে",
                                            fontSize = 10.sp,
                                            color = PlayerRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    player.isAI -> {
                                        Text(
                                            text = "🤖 এআই",
                                            fontSize = 10.sp,
                                            color = Color(0xFF38BDF8)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Recent Activity Log Card
 */
@Composable
fun RecentLogsCard(logs: List<GameLog>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "খেলার সাম্প্রতিক বিবরণী",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            logs.takeLast(3).reversed().forEach { log ->
                Text(
                    text = "• ${log.text}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
