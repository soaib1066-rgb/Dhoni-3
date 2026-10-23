import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { X, Copy, Check, FileCode, Smartphone, Terminal, Download, ShieldCheck, Sparkles } from 'lucide-react';

interface AndroidExportModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const AndroidExportModal: React.FC<AndroidExportModalProps> = ({ isOpen, onClose }) => {
  const [activeTab, setActiveTab] = useState<'guide' | 'workflow' | 'manifest' | 'gradle' | 'kotlin' | 'boardscreen'>('guide');
  const [copied, setCopied] = useState(false);

  if (!isOpen) return null;

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const WORKFLOW_CODE = `name: Build Android Debug APK

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]
  workflow_dispatch:

jobs:
  build:
    name: Build Dhoni Hobar APK
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build Debug APK with Gradle
        run: ./gradlew --no-daemon assembleDebug

      - name: Upload Debug APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
          if-no-files-found: error
`;

  const MANIFEST_CODE = `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.bangla.dhonihobar">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher"
        android:supportsRtl="true"
        android:theme="@style/Theme.DhoniHobar"
        tools:targetApi="35">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.DhoniHobar"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>`;

  const GRADLE_CODE = `// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.bangla.dhonihobar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bangla.dhonihobar"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
`;

  const KOTLIN_CODE = `// MainActivity.kt
package com.bangla.dhonihobar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bangla.dhonihobar.ui.DhonihobarApp
import com.bangla.dhonihobar.ui.theme.DhoniHobarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DhoniHobarTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DhonihobarApp()
                }
            }
        }
    }
}
`;

  const BOARD_SCREEN_CODE = `package com.bangla.dhonihobar.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bangla.dhonihobar.model.*
import com.bangla.dhonihobar.util.BanglaFormatter

/**
 * 2D Square Board with 20 Perimeter Tiles + 4x4 Center Virtual 3D Dice
 */
@Composable
fun SquareGameBoard(
    tiles: List<BoardTile>,
    players: List<Player>,
    activePlayer: Player,
    lastDiceRoll: Int?,
    isRolling: Boolean,
    isMoving: Boolean,
    onRollDice: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        shape = RoundedCornerShape(22.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            val totalSize = maxWidth
            val cellSize = totalSize / 6f

            // 20 perimeter tiles along 6x6 ring
            tiles.forEach { tile ->
                val (col, row) = getTileGridCoord(tile.id)
                val playersOnTile = players.filter { !it.isBankrupt && it.currentPosition == tile.id }

                Box(
                    modifier = Modifier
                        .offset(x = cellSize * col, y = cellSize * row)
                        .size(cellSize)
                        .padding(1.dp)
                ) {
                    TileCardView(tile = tile, playersOnTile = playersOnTile)
                }
            }

            // 4x4 Center Area with 3D Virtual Dice
            Box(
                modifier = Modifier
                    .offset(x = cellSize * 1, y = cellSize * 1)
                    .size(width = cellSize * 4, height = cellSize * 4)
            ) {
                CenterDashboardView(
                    activePlayer = activePlayer,
                    lastDiceRoll = lastDiceRoll,
                    isRolling = isRolling,
                    isMoving = isMoving,
                    onRollDice = onRollDice
                )
            }
        }
    }
}
`;

  const getActiveCode = () => {
    switch (activeTab) {
      case 'workflow':
        return WORKFLOW_CODE;
      case 'manifest':
        return MANIFEST_CODE;
      case 'gradle':
        return GRADLE_CODE;
      case 'kotlin':
        return KOTLIN_CODE;
      case 'boardscreen':
        return BOARD_SCREEN_CODE;
      default:
        return '';
    }
  };

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-3 sm:p-5 bg-black/80 backdrop-blur-md">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 15 }}
          className="w-full max-w-2xl bg-slate-900 border border-emerald-500/40 rounded-3xl p-5 shadow-2xl text-slate-100 flex flex-col max-h-[90vh]"
        >
          {/* Header */}
          <div className="flex items-center justify-between pb-3 border-b border-slate-800">
            <div className="flex items-center gap-2.5">
              <div className="p-2 bg-emerald-500/20 text-emerald-400 rounded-xl">
                <Smartphone className="w-5 h-5" />
              </div>
              <div>
                <h2 className="text-lg font-bold text-slate-100">Android Project & APK বিল্ড সিস্টেম</h2>
                <p className="text-xs text-slate-400">Kotlin · Jetpack Compose · AGP 8.7.3 · GitHub Actions</p>
              </div>
            </div>
            <button
              type="button"
              onClick={onClose}
              className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Navigation Tabs */}
          <div className="flex items-center gap-1.5 py-3 border-b border-slate-800 overflow-x-auto">
            <button
              type="button"
              onClick={() => setActiveTab('guide')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'guide'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              নির্দেশনা ও ধাপ (Guide)
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('workflow')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'workflow'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              .github/workflows/build.yml
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('gradle')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'gradle'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              build.gradle.kts
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('manifest')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'manifest'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              AndroidManifest.xml
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('kotlin')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'kotlin'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              MainActivity.kt
            </button>
            <button
              type="button"
              onClick={() => setActiveTab('boardscreen')}
              className={`px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors whitespace-nowrap ${
                activeTab === 'boardscreen'
                  ? 'bg-emerald-600 text-white shadow'
                  : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
              }`}
            >
              BoardScreen.kt (2D Board)
            </button>
          </div>

          {/* Content Area */}
          <div className="flex-1 overflow-y-auto py-3">
            {activeTab === 'guide' ? (
              <div className="space-y-4 text-xs sm:text-sm text-slate-300 leading-relaxed">
                <div className="p-3.5 rounded-2xl bg-emerald-950/40 border border-emerald-500/30">
                  <div className="flex items-center gap-2 font-bold text-emerald-300 mb-1">
                    <ShieldCheck className="w-4 h-4 text-emerald-400" />
                    <span>প্রজেক্ট কনফিগারেশন বিবরণ</span>
                  </div>
                  <ul className="list-disc list-inside space-y-1 text-slate-300 text-xs">
                    <li><strong>Package:</strong> <code>com.bangla.dhonihobar</code></li>
                    <li><strong>Target / Compile SDK:</strong> 35 · <strong>Min SDK:</strong> 23</li>
                    <li><strong>AGP Version:</strong> 8.7.3 · <strong>Gradle Wrapper:</strong> 8.11.1</li>
                    <li><strong>JDK:</strong> Java 17 LTS (Temurin)</li>
                    <li><strong>UI Framework:</strong> Jetpack Compose & Material 3</li>
                    <li><strong>নতুন ফিচার:</strong> খেলোয়াড়দের নাম কাস্টমাইজেশন, খেলা চালিয়ে যান (Resume) ও ৩০,০০০৳ জয়ের মাইলফলক</li>
                  </ul>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-800/60 border border-slate-700">
                  <div className="flex items-center gap-2 font-bold text-amber-300 mb-1.5">
                    <Terminal className="w-4 h-4 text-amber-400" />
                    <span>কীভাবে স্বয়ংক্রিয়ভাবে APK তৈরি ও ডাউনলোড করবেন?</span>
                  </div>
                  <ol className="list-decimal list-inside space-y-2 text-xs text-slate-300">
                    <li>
                      প্রজেক্টের রুট ডিরেক্টরিতে প্রস্তুতকৃত <code>.github/workflows/build.yml</code> ফাইলটি GitHub রিপোজিটরিতে পুশ করুন।
                    </li>
                    <li>
                      GitHub-এর <strong>Actions</strong> ট্যাবে যান। সেখানে <code>Build Android Debug APK</code> ওয়ার্কফ্লো স্বয়ংক্রিয়ভাবে চালিত হবে।
                    </li>
                    <li>
                      বিল্ড সফল হলে <strong>Artifacts</strong> সেকশনে <code>app-debug-apk</code> ফাইলটি সরাসরি ডাউনলোড করার লিংক পেয়ে যাবেন।
                    </li>
                    <li>
                      ডাউনলোড করা <code>app-debug.apk</code> যেকোনো Android ডিভাইসে ইনস্টল করে অফলাইনে উপভোগ করতে পারবেন।
                    </li>
                  </ol>
                </div>

                <div className="p-3.5 rounded-2xl bg-slate-800/60 border border-slate-700">
                  <div className="flex items-center gap-2 font-bold text-sky-300 mb-1.5">
                    <Smartphone className="w-4 h-4 text-sky-400" />
                    <span>Android Studio-তে সরাসরি চালানোর উপায়</span>
                  </div>
                  <p className="text-xs text-slate-300">
                    প্রজেক্ট ফোল্ডারটি Android Studio Ladybug বা যেকোনো সাম্প্রতিক সংস্করণে ওপেন করুন। Gradle Sync সম্পন্ন হলে সরাসরি <strong>Run 'app'</strong> চাপুন অথবা টার্মিনালে <code>./gradlew assembleDebug</code> চালান।
                  </p>
                </div>
              </div>
            ) : (
              <div className="relative">
                <button
                  type="button"
                  onClick={() => copyToClipboard(getActiveCode())}
                  className="absolute top-2 right-2 px-2.5 py-1 rounded bg-slate-800 hover:bg-slate-700 text-xs text-slate-300 flex items-center gap-1.5 border border-slate-700 transition-colors z-10"
                >
                  {copied ? <Check className="w-3.5 h-3.5 text-emerald-400" /> : <Copy className="w-3.5 h-3.5" />}
                  <span>{copied ? 'কপি হয়েছে!' : 'কোড কপি করুন'}</span>
                </button>
                <pre className="p-4 bg-slate-950 rounded-2xl border border-slate-800 text-[11px] font-mono text-emerald-400 overflow-x-auto leading-relaxed max-h-[420px]">
                  {getActiveCode()}
                </pre>
              </div>
            )}
          </div>

          {/* Footer */}
          <div className="pt-3 border-t border-slate-800 flex items-center justify-between">
            <span className="text-[11px] text-slate-400">
              সম্পূর্ণ ফাইলসমূহ প্রজেক্টের রুটে সংরক্ষিত রয়েছে
            </span>
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-200 text-xs font-semibold"
            >
              বন্ধ করুন
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
