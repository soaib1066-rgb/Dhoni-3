package com.bangla.dhonihobar.data

import com.bangla.dhonihobar.model.BoardTile
import com.bangla.dhonihobar.model.ChanceCard
import com.bangla.dhonihobar.model.PropertyGroup
import com.bangla.dhonihobar.model.TileType

object DefaultGameData {
    const val STARTING_BALANCE = 15000
    const val START_PASS_BONUS = 2000
    const val TAX_AMOUNT = 1000
    const val JAIL_FINE = 1000
    const val TARGET_WIN_BALANCE = 30000

    val TILES = listOf(
        BoardTile(0, "শুরু", TileType.START, description = "শুরু ঘর অতিক্রম করলে ৳২,০০০ বোনাস।"),
        BoardTile(1, "নারায়ণগঞ্জ", TileType.PROPERTY, PropertyGroup.CITY_A, cost = 1200, rent = 200, description = "প্রাচ্যের ড্যান্ডি খ্যাত বাণিজ্য নগরী।"),
        BoardTile(2, "কুমিল্লা", TileType.PROPERTY, PropertyGroup.CITY_A, cost = 1500, rent = 250, description = "ঐতিহ্যবাহী রসমালাইয়ের শহর।"),
        BoardTile(3, "যমুনা এক্সপ্রেস", TileType.PROPERTY, PropertyGroup.TRANSPORT, cost = 2500, rent = 400, description = "আধুনিক রেল পরিবহন।"),
        BoardTile(4, "ময়মনসিংহ", TileType.PROPERTY, PropertyGroup.CITY_A, cost = 1800, rent = 300, description = "ব্রহ্মপুত্র তীরের প্রাচীন শিক্ষানগরী।"),
        BoardTile(5, "জেলখানা", TileType.JAIL, description = "জেলখানায় ১ টার্ন স্থগিত অথবা ৳১,০০০ জরিমানা।"),
        BoardTile(6, "বরিশাল", TileType.PROPERTY, PropertyGroup.CITY_B, cost = 2200, rent = 350, description = "ধান-নদী-খালের নান্দনিক দক্ষিণাঞ্চল।"),
        BoardTile(7, "খুলনা", TileType.PROPERTY, PropertyGroup.CITY_B, cost = 2500, rent = 400, description = "রুপসা তীরের শিল্প নগরী।"),
        BoardTile(8, "বিদ্যুৎ কেন্দ্র", TileType.PROPERTY, PropertyGroup.SPECIAL, cost = 3000, rent = 500, description = "জাতীয় গ্রিডের বিদ্যুৎ সরবরাহ কেন্দ্র।"),
        BoardTile(9, "রাজশাহী", TileType.PROPERTY, PropertyGroup.CITY_B, cost = 2800, rent = 450, description = "রেশম ও আমের বিখ্যাত নগরী।"),
        BoardTile(10, "ভাগ্য পরীক্ষা", TileType.CHANCE, description = "ভাগ্যের বিশেষ কার্ড গ্রহণ করুন।"),
        BoardTile(11, "রংপুর", TileType.PROPERTY, PropertyGroup.CITY_C, cost = 3000, rent = 500, description = "উত্তরবঙ্গের কৃষি রাজধানী।"),
        BoardTile(12, "সিলেট", TileType.PROPERTY, PropertyGroup.CITY_C, cost = 3500, rent = 600, description = "সুরমা তীরের সবুজ চায়ের দেশ।"),
        BoardTile(13, "বিমান বাংলাদেশ", TileType.PROPERTY, PropertyGroup.TRANSPORT, cost = 4000, rent = 700, description = "জাতীয় পতাকাবাহী আন্তর্জাতিক এয়ারলাইন্স।"),
        BoardTile(14, "চট্টগ্রাম", TileType.PROPERTY, PropertyGroup.CITY_C, cost = 4200, rent = 750, description = "বাণিজ্যিক রাজধানী ও সমুদ্রবন্দর।"),
        BoardTile(15, "কর / জরিমানা", TileType.TAX, description = "পৌর কর বাবদ ৳১,০০০ সরকারি কোষাগারে প্রদান করুন।"),
        BoardTile(16, "কক্সবাজার", TileType.PROPERTY, PropertyGroup.SPECIAL, cost = 4500, rent = 800, description = "বিশ্বের দীর্ঘতম প্রাকৃতিক সমুদ্র সৈকত।"),
        BoardTile(17, "সুন্দরবন", TileType.PROPERTY, PropertyGroup.SPECIAL, cost = 4800, rent = 850, description = "রয়েল বেঙ্গল টাইগারের ম্যানগ্রোভ বন।"),
        BoardTile(18, "পদ্মা সেতু", TileType.PROPERTY, PropertyGroup.TRANSPORT, cost = 5000, rent = 900, description = "বাঙালির অহংকার স্বপ্নের পদ্মা সেতু।"),
        BoardTile(19, "ঢাকা", TileType.PROPERTY, PropertyGroup.SPECIAL, cost = 5500, rent = 1000, description = "বাংলাদেশের প্রাণকেন্দ্র রাজধানী মেগা সিটি।")
    )

    val CHANCE_CARDS = listOf(
        ChanceCard(1, "ব্যবসা সম্প্রসারণ অনুদান", "নতুন ব্যবসায় সরকার থেকে অনুদান পেলেন।", amountEffect = 2000),
        ChanceCard(2, "আয়কর বিলম্ব ফি", "যথাসময়ে আয়কর না দেওয়ায় জরিমানা কাটা হলো।", amountEffect = -1000),
        ChanceCard(3, "পদ্মা সেতু পরিদর্শনে চলুন", "সরাসরি পদ্মা সেতুতে ভ্রমণ করুন!", moveTarget = 18),
        ChanceCard(4, "শেয়ার বাজারে দারুণ লাভ", "পুঁজিবাজারে বিনিয়োগে চমৎকার মুনাফা!", amountEffect = 3000),
        ChanceCard(5, "বিদ্যুৎ বিল পরিশোধ", "বিল ও সংস্কার খরচ প্রদান করুন।", amountEffect = -800),
        ChanceCard(6, "জাতীয় লটারি জয়", "লটারিতে প্রথম পুরস্কার অর্জন করলেন!", amountEffect = 1500),
        ChanceCard(7, "শুরু ঘরে প্রত্যাবর্তন", "শুরু ঘরে যান এবং বোনাস ৳২,০০০ গ্রহণ করুন।", amountEffect = 2000, moveTarget = 0),
        ChanceCard(8, "জেলখানা পরিদর্শন", "সরাসরি জেলখানায় যান।", isJailMove = true)
    )
}
