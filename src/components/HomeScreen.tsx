import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Bot, Users, Play, RotateCcw, BookOpen, Smartphone, Volume2, VolumeX, Trophy, Sparkles, UserCheck, Sliders } from 'lucide-react';
import { toBanglaNumber } from '../utils/banglaFormatter';

interface HomeScreenProps {
  hasSavedGame: boolean;
  onResumeGame: () => void;
  onStartBotGame: (humanName: string, botName: string) => void;
  onStartMultiplayerGame: (playerNames: string[]) => void;
  onOpenRules: () => void;
  onOpenAndroidExport: () => void;
  soundEnabled: boolean;
  onToggleSound: () => void;
  onOpenSettings?: () => void;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({
  hasSavedGame,
  onResumeGame,
  onStartBotGame,
  onStartMultiplayerGame,
  onOpenRules,
  onOpenAndroidExport,
  soundEnabled,
  onToggleSound,
  onOpenSettings,
}) => {
  const [activeTab, setActiveTab] = useState<'bot' | 'multiplayer'>('bot');
  const [selectedMultiplayerCount, setSelectedMultiplayerCount] = useState<number>(2);

  // Custom player names
  const [humanName, setHumanName] = useState<string>('আপনি');
  const [botName, setBotName] = useState<string>('এআই রোবট');

  const [multiplayerNames, setMultiplayerNames] = useState<string[]>([
    'রাকিব',
    'সাকিব',
    'আসিফ',
    'তানভীর',
  ]);

  const handleMultiplayerNameChange = (index: number, newName: string) => {
    const updated = [...multiplayerNames];
    updated[index] = newName;
    setMultiplayerNames(updated);
  };

  const handleStartBot = () => {
    const cleanHuman = humanName.trim() || 'আপনি';
    const cleanBot = botName.trim() || 'এআই রোবট';
    onStartBotGame(cleanHuman, cleanBot);
  };

  const handleStartMultiplayer = () => {
    const activeNames = multiplayerNames.slice(0, selectedMultiplayerCount).map((name, i) => {
      const trimmed = name.trim();
      return trimmed || `খেলোয়াড় ${i + 1}`;
    });
    onStartMultiplayerGame(activeNames);
  };

  const PLAYER_COLORS = [
    { label: '১ম খেলোয়াড়', color: '#EF4444', text: 'লাল' },
    { label: '২য় খেলোয়াড়', color: '#3B82F6', text: 'নীল' },
    { label: '৩য় খেলোয়াড়', color: '#10B981', text: 'সবুজ' },
    { label: '৪র্থ খেলোয়াড়', color: '#F59E0B', text: 'হলুদ' },
  ];

  return (
    <div className="min-h-screen w-full flex flex-col items-center justify-center p-4 sm:p-6 bg-gradient-to-b from-slate-950 via-emerald-950/40 to-slate-950 relative overflow-hidden select-none">
      {/* Background ambient accents */}
      <div className="absolute -top-40 -left-40 w-96 h-96 bg-emerald-500/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute -bottom-40 -right-40 w-96 h-96 bg-amber-500/10 rounded-full blur-3xl pointer-events-none" />

      {/* Top action bar */}
      <div className="absolute top-4 right-4 sm:top-6 sm:right-6 flex items-center gap-2 z-20">
        {onOpenSettings && (
          <button
            type="button"
            onClick={onOpenSettings}
            className="p-2.5 rounded-xl bg-slate-900/80 border border-slate-700/80 text-slate-300 hover:text-white transition-colors shadow-sm"
            title="সেটিংস"
          >
            <Sliders className="w-5 h-5 text-emerald-400" />
          </button>
        )}

        <button
          type="button"
          onClick={onToggleSound}
          className="p-2.5 rounded-xl bg-slate-900/80 border border-slate-700/80 text-slate-300 hover:text-white transition-colors shadow-sm"
          title={soundEnabled ? 'শব্দ বন্ধ' : 'শব্দ চালু'}
        >
          {soundEnabled ? <Volume2 className="w-5 h-5" /> : <VolumeX className="w-5 h-5 text-rose-400" />}
        </button>

        <button
          type="button"
          onClick={onOpenAndroidExport}
          className="px-3 py-2 rounded-xl bg-emerald-950/80 border border-emerald-500/40 text-emerald-300 hover:bg-emerald-900/80 text-xs font-semibold flex items-center gap-1.5 transition-colors shadow-sm"
          title="Android APK ও সোর্স কোড"
        >
          <Smartphone className="w-4 h-4 text-emerald-400" />
          <span className="hidden sm:inline">Android APK</span>
        </button>
      </div>

      {/* Hero Center Card */}
      <motion.div
        initial={{ opacity: 0, scale: 0.94, y: 15 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        transition={{ duration: 0.35, ease: [0.16, 1, 0.3, 1] }}
        className="w-full max-w-lg bg-slate-900/95 border-2 border-emerald-500/30 rounded-3xl p-5 sm:p-7 shadow-2xl backdrop-blur-md flex flex-col items-center text-center gap-5 relative z-10 my-4"
      >
        {/* Game Logo */}
        <div className="relative">
          <div className="w-16 h-16 sm:w-20 sm:h-20 rounded-2xl bg-gradient-to-br from-emerald-600 via-teal-700 to-slate-900 border-2 border-emerald-400/50 flex items-center justify-center shadow-xl shadow-emerald-950/60 p-2">
            <svg viewBox="0 0 100 100" className="w-full h-full text-amber-300">
              <rect x="18" y="22" width="64" height="64" rx="14" fill="#047857" stroke="#34D399" strokeWidth="3" />
              <circle cx="34" cy="38" r="5" fill="#FCD34D" />
              <circle cx="66" cy="38" r="5" fill="#FCD34D" />
              <circle cx="50" cy="54" r="5" fill="#FCD34D" />
              <circle cx="34" cy="70" r="5" fill="#FCD34D" />
              <circle cx="66" cy="70" r="5" fill="#FCD34D" />
              <path d="M30 18 L38 25 L50 14 L62 25 L70 18 L68 28 L32 28 Z" fill="#F59E0B" stroke="#FDE68A" strokeWidth="1.5" />
            </svg>
          </div>
          <div className="absolute -bottom-2 -right-1 bg-amber-500 text-slate-950 font-black text-[9px] px-2 py-0.5 rounded-full shadow border border-amber-300">
            2D সংস্করণ
          </div>
        </div>

        {/* Title Lockup */}
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-emerald-300 via-amber-200 to-emerald-300 tracking-tight">
            ধনী হবার মজার খেলা
          </h1>
          <p className="text-xs text-slate-300 mt-1 font-medium">
            বাংলাদেশের ঐতিহ্যবাহী বোর্ড গেম · অফলাইন ও এআই মোড
          </p>

          {/* Victory Goal Badge */}
          <div className="inline-flex items-center gap-1.5 px-3 py-1 mt-2 rounded-full bg-amber-500/10 border border-amber-500/30 text-amber-300 text-[11px] font-semibold">
            <Trophy className="w-3.5 h-3.5 text-amber-400" />
            <span>জয়লাভের লক্ষ্য: আগে ৳৩০,০০০ বা একমাত্র টিকে থাকা</span>
          </div>
        </div>

        {/* 1. Resume বা "খেলা চালিয়ে যান" বাটন (Prominent when available) */}
        {hasSavedGame && (
          <div className="w-full p-1 rounded-2xl bg-gradient-to-r from-amber-500 via-emerald-500 to-amber-500 shadow-lg shadow-amber-950/30">
            <button
              type="button"
              onClick={onResumeGame}
              className="w-full py-3 px-4 rounded-[14px] bg-slate-900 hover:bg-slate-800 text-white font-bold text-sm sm:text-base flex items-center justify-between transition-all group"
            >
              <div className="flex items-center gap-3 text-left">
                <div className="p-2 rounded-xl bg-amber-500/20 text-amber-400 group-hover:scale-110 transition-transform">
                  <RotateCcw className="w-5 h-5" />
                </div>
                <div>
                  <div className="text-amber-300 font-black text-sm">খেলা চালিয়ে যান (Resume)</div>
                  <div className="text-[11px] text-slate-400 font-normal">আগের চলমান খেলার অবস্থা সংরক্ষিত রয়েছে</div>
                </div>
              </div>
              <span className="text-xs font-bold text-emerald-400 bg-emerald-950/80 px-2.5 py-1 rounded-lg border border-emerald-500/30">
                চালান →
              </span>
            </button>
          </div>
        )}

        {/* Mode Selector Tabs */}
        <div className="w-full flex items-center p-1 bg-slate-800/80 rounded-xl border border-slate-700/60">
          <button
            type="button"
            onClick={() => setActiveTab('bot')}
            className={`flex-1 py-2 rounded-lg font-bold text-xs sm:text-sm flex items-center justify-center gap-2 transition-all ${
              activeTab === 'bot'
                ? 'bg-emerald-600 text-white shadow-md'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Bot className="w-4 h-4" />
            <span>বট মোড (AI)</span>
          </button>
          <button
            type="button"
            onClick={() => setActiveTab('multiplayer')}
            className={`flex-1 py-2 rounded-lg font-bold text-xs sm:text-sm flex items-center justify-center gap-2 transition-all ${
              activeTab === 'multiplayer'
                ? 'bg-sky-600 text-white shadow-md'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            <Users className="w-4 h-4" />
            <span>বন্ধুদের সঙ্গে (মাল্টিপ্লেয়ার)</span>
          </button>
        </div>

        {/* Tab 1: Bot Mode Setup with Name Customization */}
        {activeTab === 'bot' && (
          <div className="w-full flex flex-col gap-3 text-left bg-slate-800/40 p-3.5 sm:p-4 rounded-2xl border border-slate-700/60">
            <div className="text-xs font-bold text-slate-300 flex items-center gap-1.5">
              <UserCheck className="w-4 h-4 text-emerald-400" />
              <span>খেলোয়াড়দের নাম কাস্টমাইজ করুন</span>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
              {/* Human Player Name */}
              <div className="flex flex-col gap-1">
                <label className="text-[11px] text-slate-400 flex items-center gap-1.5 font-medium">
                  <span className="w-2.5 h-2.5 rounded-full bg-red-500 inline-block" />
                  আপনার নাম (লাল ঘুঁটি):
                </label>
                <input
                  type="text"
                  value={humanName}
                  onChange={(e) => setHumanName(e.target.value)}
                  placeholder="আপনার নাম"
                  maxLength={16}
                  className="w-full px-3 py-2 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 text-xs sm:text-sm focus:outline-none focus:border-emerald-500 transition-colors font-medium"
                />
              </div>

              {/* Bot Player Name */}
              <div className="flex flex-col gap-1">
                <label className="text-[11px] text-slate-400 flex items-center gap-1.5 font-medium">
                  <span className="w-2.5 h-2.5 rounded-full bg-blue-500 inline-block" />
                  রোবট বটের নাম (নীল ঘুঁটি):
                </label>
                <input
                  type="text"
                  value={botName}
                  onChange={(e) => setBotName(e.target.value)}
                  placeholder="বটের নাম"
                  maxLength={16}
                  className="w-full px-3 py-2 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 text-xs sm:text-sm focus:outline-none focus:border-emerald-500 transition-colors font-medium"
                />
              </div>
            </div>

            <button
              type="button"
              onClick={handleStartBot}
              className="w-full mt-1 py-3 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-sm sm:text-base shadow-lg shadow-emerald-950/50 flex items-center justify-center gap-2 border border-emerald-400/40 active:scale-95 transition-all"
            >
              <Play className="w-4 h-4 fill-current" />
              <span>বটের সঙ্গে খেলা শুরু করুন</span>
            </button>
          </div>
        )}

        {/* Tab 2: Multiplayer Mode Setup with Name Customization */}
        {activeTab === 'multiplayer' && (
          <div className="w-full flex flex-col gap-3 text-left bg-slate-800/40 p-3.5 sm:p-4 rounded-2xl border border-slate-700/60">
            {/* Player Count Select */}
            <div className="flex items-center justify-between">
              <span className="text-xs text-slate-300 font-bold">খেলোয়াড় সংখ্যা নির্বাচন:</span>
              <div className="flex items-center gap-1.5">
                {[2, 3, 4].map((count) => (
                  <button
                    key={count}
                    type="button"
                    onClick={() => setSelectedMultiplayerCount(count)}
                    className={`w-8 h-8 rounded-lg font-bold text-xs transition-all ${
                      selectedMultiplayerCount === count
                        ? 'bg-sky-500 text-white shadow-md scale-105'
                        : 'bg-slate-700 text-slate-300 hover:bg-slate-600'
                    }`}
                  >
                    {toBanglaNumber(count)}
                  </button>
                ))}
              </div>
            </div>

            {/* Custom Name Inputs for Each Player */}
            <div className="flex flex-col gap-2 pt-1 border-t border-slate-700/50">
              <span className="text-[11px] text-slate-400 font-semibold">
                খেলোয়াড়দের নাম পরিবর্তন করুন:
              </span>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                {PLAYER_COLORS.slice(0, selectedMultiplayerCount).map((p, idx) => (
                  <div key={idx} className="flex flex-col gap-1">
                    <label className="text-[11px] text-slate-400 flex items-center gap-1.5">
                      <span
                        className="w-2.5 h-2.5 rounded-full inline-block"
                        style={{ backgroundColor: p.color }}
                      />
                      <span>{p.label} ({p.text}):</span>
                    </label>
                    <input
                      type="text"
                      value={multiplayerNames[idx] || ''}
                      onChange={(e) => handleMultiplayerNameChange(idx, e.target.value)}
                      placeholder={`খেলোয়াড় ${idx + 1}`}
                      maxLength={16}
                      className="w-full px-3 py-1.5 rounded-xl bg-slate-900 border border-slate-700 text-slate-100 text-xs focus:outline-none focus:border-sky-500 font-medium transition-colors"
                    />
                  </div>
                ))}
              </div>
            </div>

            <button
              type="button"
              onClick={handleStartMultiplayer}
              className="w-full mt-1 py-3 rounded-xl bg-gradient-to-r from-sky-600 to-blue-600 hover:from-sky-500 hover:to-blue-500 text-white font-bold text-sm sm:text-base shadow-lg shadow-sky-950/50 flex items-center justify-center gap-2 border border-sky-400/40 active:scale-95 transition-all"
            >
              <Play className="w-4 h-4 fill-current" />
              <span>{toBanglaNumber(selectedMultiplayerCount)} জন খেলোয়াড় শুরু করুন</span>
            </button>
          </div>
        )}

        {/* Footer info links */}
        <div className="w-full flex items-center justify-between pt-2 border-t border-slate-800 text-xs text-slate-400">
          <button
            type="button"
            onClick={onOpenRules}
            className="flex items-center gap-1.5 hover:text-slate-200 transition-colors"
          >
            <BookOpen className="w-4 h-4 text-emerald-400" />
            <span>খেলার নিয়মাবলী (৳৩০,০০০ জয়)</span>
          </button>

          <button
            type="button"
            onClick={onOpenAndroidExport}
            className="flex items-center gap-1.5 hover:text-slate-200 transition-colors"
          >
            <Smartphone className="w-4 h-4 text-sky-400" />
            <span>Android APK গাইড</span>
          </button>
        </div>
      </motion.div>

      {/* Footer copyright */}
      <div className="mt-2 text-xs text-slate-400 text-center z-10">
        ধনী হবার মজার খেলা · বাংলা 2D বোর্ড গেম · অফলাইন সংস্করণ
      </div>
    </div>
  );
};
