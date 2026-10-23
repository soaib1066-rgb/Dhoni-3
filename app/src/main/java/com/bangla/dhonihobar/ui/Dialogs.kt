package com.bangla.dhonihobar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangla.dhonihobar.model.BoardTile
import com.bangla.dhonihobar.model.ChanceCard
import com.bangla.dhonihobar.model.Player
import com.bangla.dhonihobar.ui.theme.*
import com.bangla.dhonihobar.util.BanglaFormatter

@Composable
fun PropertyPurchaseDialog(
    tile: BoardTile?,
    player: Player?,
    onBuy: () -> Unit,
    onPass: () -> Unit
) {
    if (tile == null || player == null) return
    val canAfford = player.balance >= tile.cost

    AlertDialog(
        onDismissRequest = onPass,
        title = {
            Text(text = "জমি ক্রয়: ${tile.name}", color = GoldAccent, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = tile.description, color = TextLight, fontSize = 14.sp)
                Text(text = "মূল্য: ${BanglaFormatter.formatBanglaTaka(tile.cost)}", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                Text(text = "ভাড়া: ${BanglaFormatter.formatBanglaTaka(tile.rent)}", color = TextMuted)
                Text(text = "আপনার ব্যালেন্স: ${BanglaFormatter.formatBanglaTaka(player.balance)}", color = TextLight)
                if (!canAfford) {
                    Text(text = "পর্যাপ্ত টাকা নেই!", color = PlayerRed, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onBuy,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(text = "কিনুন", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onPass) {
                Text(text = "পাস করুন", color = TextMuted)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ChanceCardDialog(
    card: ChanceCard?,
    onDismiss: () -> Unit
) {
    if (card == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "ভাগ্য পরীক্ষা", color = GoldAccent, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = card.title, color = TextLight, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = card.description, color = TextMuted)
                if (card.amountEffect != 0) {
                    val color = if (card.amountEffect > 0) EmeraldPrimary else PlayerRed
                    val prefix = if (card.amountEffect > 0) "+" else ""
                    Text(
                        text = "প্রভাব: $prefix${BanglaFormatter.formatBanglaTaka(card.amountEffect)}",
                        color = color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Text(text = "কার্যকর করুন", color = SlateBackground, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun JailDialog(
    player: Player?,
    onPayFine: () -> Unit,
    onWait: () -> Unit
) {
    if (player == null) return
    val canAfford = player.balance >= 1000

    AlertDialog(
        onDismissRequest = onWait,
        title = {
            Text(text = "জেলখানা", color = PlayerRed, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "${player.name} জেলখানায় আটকে আছেন!", color = TextLight)
                Text(text = "৳১,০০০ জরিমানা দিয়ে বের হবেন নাকি ১ টার্ন অপেক্ষা করবেন?", color = TextMuted)
            }
        },
        confirmButton = {
            Button(
                onClick = onPayFine,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(text = "৳১,০০০ জরিমানা দিন", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onWait) {
                Text(text = "১ টার্ন অপেক্ষা", color = TextMuted)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun WinnerDialog(
    winner: Player?,
    reason: String? = null,
    onRestart: () -> Unit
) {
    if (winner == null) return

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "অভিনন্দন! বিজয়ী 🏆", color = GoldAccent, fontWeight = FontWeight.Black)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${winner.name} জয়ী হয়েছেন!",
                    color = TextLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (!reason.isNullOrBlank()) {
                    Text(
                        text = reason,
                        color = GoldAccent,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "চূড়ান্ত নগদ: ${BanglaFormatter.formatBanglaTaka(winner.balance)}",
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(text = "আবার খেলুন", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun QuitConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "গেম কাটতে চান?", color = PlayerRed, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(
                text = "আপনার খেলার বর্তমান অগ্রগতি স্বয়ংক্রিয়ভাবে সংরক্ষিত থাকবে। আপনি পরবর্তীতে যেকোনো সময় 'খেলা চালিয়ে যান' বাটন দিয়ে আবার খেলাটি চালিয়ে যেতে পারবেন।",
                color = TextLight,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = PlayerRed)
            ) {
                Text(text = "হ্যাঁ, হোমে যান", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "না, খেলুন", color = TextMuted)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun RestartConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "খেলা রেস্টার্ট করবেন?", color = GoldAccent, fontWeight = FontWeight.Bold)
        },
        text = {
            Text(
                text = "বর্তমান খেলার অগ্রগতি মুছে নতুন করে শুরু হবে। আপনি কি পুনরায় খেলা শুরু করতে চান?",
                color = TextLight,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
            ) {
                Text(text = "হ্যাঁ, রেস্টার্ট করুন", color = SlateBackground, fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "বাতিল", color = TextMuted)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun SettingsDialog(
    soundEnabled: Boolean = true,
    onToggleSound: () -> Unit = {},
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "গেম সেটিংস", color = GoldAccent, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "সাউন্ড এফেক্টস", color = TextLight, fontWeight = FontWeight.Bold)
                        Text(text = "ছক্কা ও ঘুঁটির শব্দ", color = TextMuted, fontSize = 12.sp)
                    }
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = { onToggleSound() }
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SlateCard,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "গেম অভিজ্ঞতা:", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = "• গুটির মুভমেন্ট: ধীরেসুস্থে গতি (৩৮০ms)", color = TextLight, fontSize = 11.sp)
                        Text(text = "• চাল পরিবর্তনের বিরতি: ১.৫ সেকেন্ড", color = TextLight, fontSize = 11.sp)
                        Text(text = "• জয়ের শর্ত: ৩০,০০০৳ ক্যাশ অর্জন বা শেষ টিকে থাকা", color = EmeraldPrimary, fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(text = "ঠিক আছে", color = Color.White)
            }
        },
        containerColor = SlateSurface,
        shape = RoundedCornerShape(20.dp)
    )
}
