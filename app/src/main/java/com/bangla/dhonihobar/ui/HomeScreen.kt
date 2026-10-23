package com.bangla.dhonihobar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangla.dhonihobar.ui.theme.*
import com.bangla.dhonihobar.util.BanglaFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    hasSavedGame: Boolean = false,
    onResumeGame: () -> Unit = {},
    onStartBot: (humanName: String, botName: String) -> Unit,
    onStartMultiplayer: (names: List<String>) -> Unit
) {
    var humanName by remember { mutableStateOf("আপনি") }
    var botName by remember { mutableStateOf("কম্পিউটার বট") }

    var selectedPlayersCount by remember { mutableStateOf(2) }
    var p1Name by remember { mutableStateOf("রাকিব") }
    var p2Name by remember { mutableStateOf("সাকিব") }
    var p3Name by remember { mutableStateOf("আসিফ") }
    var p4Name by remember { mutableStateOf("তানভীর") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game Header Title
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(GoldAccent.copy(alpha = 0.4f))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "ধনী হবার মজার খেলা",
                        style = MaterialTheme.typography.headlineMedium,
                        color = GoldAccent,
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "বাংলাদেশের ঐতিহ্যবাহী 2D বোর্ড গেম",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )

                    // Target 30,000 Taka Victory Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GoldAccent.copy(alpha = 0.15f),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(GoldAccent.copy(alpha = 0.5f))
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "জয়ের লক্ষ্য: ৩০,০০০৳ ক্যাশ অর্জন অথবা প্রতিপক্ষদের দেউলিয়া করা!",
                                color = TextLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // 1. Resume / খেলা চালিয়ে যান Button (if a game is in progress)
            if (hasSavedGame) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.15f)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GoldAccent)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "আগের খেলা সংরক্ষিত আছে",
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "যেখান থেকে খেলা ছেড়েছিলেন সেখান থেকেই খেলা চালিয়ে যেতে পারবেন।",
                                color = TextLight,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onResumeGame,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = SlateBackground,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "চালিয়ে যান",
                                color = SlateBackground,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // 2. Bot Mode (বট বনাম আপনি) with Custom Names
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary.copy(alpha = 0.3f))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "বটের সঙ্গে খেলুন (AI বনাম আপনি)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextLight
                        )
                    }

                    // User name field
                    OutlinedTextField(
                        value = humanName,
                        onValueChange = { humanName = it },
                        label = { Text("আপনার নাম (লাল ঘুঁটি)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PlayerRed,
                            unfocusedBorderColor = SlateCard,
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedLabelColor = PlayerRed,
                            unfocusedLabelColor = TextMuted
                        )
                    )

                    // Bot name field
                    OutlinedTextField(
                        value = botName,
                        onValueChange = { botName = it },
                        label = { Text("বটের নাম (নীল ঘুঁটি)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PlayerBlue,
                            unfocusedBorderColor = SlateCard,
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedLabelColor = PlayerBlue,
                            unfocusedLabelColor = TextMuted
                        )
                    )

                    Button(
                        onClick = {
                            val userFinal = humanName.ifBlank { "আপনি" }
                            val botFinal = botName.ifBlank { "কম্পিউটার বট" }
                            onStartBot(userFinal, botFinal)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text(
                            text = "বটের সঙ্গে শুরু করুন",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // 3. Multiplayer Mode (একই ডিভাইসে ২-৪ জন) with Custom Names
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(PlayerBlue.copy(alpha = 0.3f))
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = PlayerBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "বন্ধুদের সঙ্গে খেলুন (মাল্টিপ্লেয়ার)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextLight
                        )
                    }

                    // Player Count Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "খেলোয়াড় সংখ্যা নির্বাচন করুন:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(2, 3, 4).forEach { count ->
                                val isSelected = selectedPlayersCount == count
                                OutlinedButton(
                                    onClick = { selectedPlayersCount = count },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) PlayerBlue else Color.Transparent,
                                        contentColor = if (isSelected) Color.White else TextMuted
                                    ),
                                    modifier = Modifier.size(38.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = BanglaFormatter.toBanglaNumber(count),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Player Names Input Fields
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Player 1 (Red)
                        OutlinedTextField(
                            value = p1Name,
                            onValueChange = { p1Name = it },
                            label = { Text("১ম খেলোয়াড় (লাল ঘুঁটি)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PlayerRed,
                                unfocusedBorderColor = SlateCard,
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedLabelColor = PlayerRed,
                                unfocusedLabelColor = TextMuted
                            )
                        )

                        // Player 2 (Blue)
                        OutlinedTextField(
                            value = p2Name,
                            onValueChange = { p2Name = it },
                            label = { Text("২য় খেলোয়াড় (নীল ঘুঁটি)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PlayerBlue,
                                unfocusedBorderColor = SlateCard,
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedLabelColor = PlayerBlue,
                                unfocusedLabelColor = TextMuted
                            )
                        )

                        // Player 3 (Green)
                        if (selectedPlayersCount >= 3) {
                            OutlinedTextField(
                                value = p3Name,
                                onValueChange = { p3Name = it },
                                label = { Text("৩য় খেলোয়াড় (সবুজ ঘুঁটি)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PlayerGreen,
                                    unfocusedBorderColor = SlateCard,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = PlayerGreen,
                                    unfocusedLabelColor = TextMuted
                                )
                            )
                        }

                        // Player 4 (Yellow)
                        if (selectedPlayersCount >= 4) {
                            OutlinedTextField(
                                value = p4Name,
                                onValueChange = { p4Name = it },
                                label = { Text("৪র্থ খেলোয়াড় (হলুদ ঘুঁটি)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PlayerYellow,
                                    unfocusedBorderColor = SlateCard,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = PlayerYellow,
                                    unfocusedLabelColor = TextMuted
                                )
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val names = mutableListOf(
                                p1Name.ifBlank { "রাকিব" },
                                p2Name.ifBlank { "সাকিব" }
                            )
                            if (selectedPlayersCount >= 3) {
                                names.add(p3Name.ifBlank { "আসিফ" })
                            }
                            if (selectedPlayersCount >= 4) {
                                names.add(p4Name.ifBlank { "তানভীর" })
                            }
                            onStartMultiplayer(names)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PlayerBlue)
                    ) {
                        Text(
                            text = "${BanglaFormatter.toBanglaNumber(selectedPlayersCount)} জনের খেলা শুরু করুন",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Rules Brief
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SlateSurface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "নিয়মাবলী সংক্ষেপে:",
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "• প্রারম্ভিক মূলধন: ১৫,০০০৳ প্রতিটি খেলোয়াড়ের জন্য।",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• শুরু ঘর পার হলে ২,০০০৳ বোনাস যোগ হবে।",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• যে সবার আগে ৩০,০০০৳ ক্যাশ সংগ্রহ করবে সে সাথে সাথে জয়ী হবে।",
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• অথবা বাকী খেলোয়াড়েরা দেউলিয়া হয়ে গেলে সর্বশেষ ব্যক্তি জয়ী হবে।",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
