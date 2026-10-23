import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { X, Volume2, VolumeX, Gauge, Hourglass, Trash2, BookOpen, Smartphone, ShieldCheck, Sliders } from 'lucide-react';

export interface GameSettings {
  soundEnabled: boolean;
  ambientSound: boolean;
  movementSpeed: 'slow' | 'normal' | 'fast'; // slow: 400ms, normal: 320ms, fast: 200ms
  turnDelay: 'slow' | 'normal'; // slow: 1500ms, normal: 900ms
}

interface SettingsModalProps {
  isOpen: boolean;
  onClose: () => void;
  settings: GameSettings;
  onUpdateSettings: (newSettings: Partial<GameSettings>) => void;
  onResetGameData?: () => void;
  onOpenRules?: () => void;
  onOpenAndroidExport?: () => void;
}

export const SettingsModal: React.FC<SettingsModalProps> = ({
  isOpen,
  onClose,
  settings,
  onUpdateSettings,
  onResetGameData,
  onOpenRules,
  onOpenAndroidExport,
}) => {
  if (!isOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-3 sm:p-4 bg-black/80 backdrop-blur-sm">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 15 }}
          className="relative w-full max-w-md bg-slate-900 border border-slate-800 rounded-3xl shadow-2xl p-5 sm:p-6 text-slate-100 flex flex-col gap-5 overflow-hidden"
        >
          {/* Header */}
          <div className="flex items-center justify-between border-b border-slate-800 pb-3">
            <div className="flex items-center gap-2.5">
              <div className="p-2 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400">
                <Sliders className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-lg font-bold text-white">গেম সেটিংস</h3>
                <p className="text-xs text-slate-400">সাউন্ড, মুভমেন্ট ও খেলার অভিজ্ঞতা নিয়ন্ত্রণ করুন</p>
              </div>
            </div>
            <button
              type="button"
              onClick={onClose}
              className="p-1.5 rounded-full hover:bg-slate-800 text-slate-400 hover:text-white transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Settings Options List */}
          <div className="flex flex-col gap-4 text-sm">
            {/* 1. Sound Effects Toggle */}
            <div className="flex items-center justify-between p-3 rounded-2xl bg-slate-800/60 border border-slate-700/60">
              <div className="flex items-center gap-3">
                <div className={`p-2 rounded-xl ${settings.soundEnabled ? 'bg-emerald-500/20 text-emerald-400' : 'bg-slate-700/50 text-slate-400'}`}>
                  {settings.soundEnabled ? <Volume2 className="w-5 h-5" /> : <VolumeX className="w-5 h-5" />}
                </div>
                <div>
                  <div className="font-semibold text-slate-100">সাউন্ড এফেক্টস</div>
                  <div className="text-xs text-slate-400">ছক্কার রোল, ধাপ ও লেনদেনের শব্দ</div>
                </div>
              </div>
              <button
                type="button"
                onClick={() => onUpdateSettings({ soundEnabled: !settings.soundEnabled })}
                className={`w-13 h-7 flex items-center rounded-full p-1 transition-colors duration-200 ${
                  settings.soundEnabled ? 'bg-emerald-500' : 'bg-slate-700'
                }`}
              >
                <div
                  className={`bg-white w-5 h-5 rounded-full shadow-md transform transition-transform duration-200 ${
                    settings.soundEnabled ? 'translate-x-6' : 'translate-x-0'
                  }`}
                />
              </button>
            </div>

            {/* 2. Movement Speed (গুটির মুভমেন্টের গতি) */}
            <div className="flex flex-col gap-2 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/60">
              <div className="flex items-center gap-2">
                <Gauge className="w-4 h-4 text-amber-400" />
                <span className="font-semibold text-slate-100">গুটির চলার গতি (Movement Speed)</span>
              </div>
              <div className="grid grid-cols-3 gap-2 mt-1">
                {(
                  [
                    { id: 'slow', label: 'ধীর (সুপারিশকৃত)' },
                    { id: 'normal', label: 'স্বাভাবিক' },
                    { id: 'fast', label: 'দ্রুত' },
                  ] as const
                ).map((opt) => (
                  <button
                    key={opt.id}
                    type="button"
                    onClick={() => onUpdateSettings({ movementSpeed: opt.id })}
                    className={`py-2 px-2 rounded-xl text-xs font-semibold border transition-all ${
                      settings.movementSpeed === opt.id
                        ? 'bg-amber-500/20 border-amber-400 text-amber-300 shadow-sm'
                        : 'bg-slate-900 border-slate-800 text-slate-400 hover:text-slate-200'
                    }`}
                  >
                    {opt.label}
                  </button>
                ))}
              </div>
              <p className="text-[11px] text-slate-400 mt-0.5">
                {settings.movementSpeed === 'slow'
                  ? 'প্রতিটি ঘরের উপর গুটি ধীরেসুস্থে সুন্দরভাবে লাফিয়ে চলবে।'
                  : settings.movementSpeed === 'normal'
                  ? 'স্বাভাবিক গতিতে গুটি মুভ করবে।'
                  : 'দ্রুতগতির মুভমেন্ট।'}
              </p>
            </div>

            {/* 3. Turn Transition Delay (একজনের পর অন্যজনের সময় আসার বিরতি) */}
            <div className="flex flex-col gap-2 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/60">
              <div className="flex items-center gap-2">
                <Hourglass className="w-4 h-4 text-sky-400" />
                <span className="font-semibold text-slate-100">টার্ন পরিবর্তনের বিরতি (Turn Transition)</span>
              </div>
              <div className="grid grid-cols-2 gap-2 mt-1">
                {(
                  [
                    { id: 'slow', label: 'শান্ত ও ধীর (১.৫ সে.)' },
                    { id: 'normal', label: 'স্বাভাবিক (০.৯ সে.)' },
                  ] as const
                ).map((opt) => (
                  <button
                    key={opt.id}
                    type="button"
                    onClick={() => onUpdateSettings({ turnDelay: opt.id })}
                    className={`py-2 px-2 rounded-xl text-xs font-semibold border transition-all ${
                      settings.turnDelay === opt.id
                        ? 'bg-sky-500/20 border-sky-400 text-sky-300 shadow-sm'
                        : 'bg-slate-900 border-slate-800 text-slate-400 hover:text-slate-200'
                    }`}
                  >
                    {opt.label}
                  </button>
                ))}
              </div>
              <p className="text-[11px] text-slate-400 mt-0.5">
                একজনের চাল শেষ হওয়ার পর পরবর্তী খেলোয়াড়ের আসার আগে পর্যাপ্ত বিরতি নিশ্চিত করে।
              </p>
            </div>

            {/* Quick Links */}
            <div className="grid grid-cols-2 gap-2">
              {onOpenRules && (
                <button
                  type="button"
                  onClick={() => {
                    onClose();
                    onOpenRules();
                  }}
                  className="p-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-xs text-slate-300 flex items-center justify-center gap-1.5 transition-colors border border-slate-700"
                >
                  <BookOpen className="w-4 h-4 text-emerald-400" />
                  <span>নিয়মাবলী দেখুন</span>
                </button>
              )}

              {onOpenAndroidExport && (
                <button
                  type="button"
                  onClick={() => {
                    onClose();
                    onOpenAndroidExport();
                  }}
                  className="p-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-xs text-slate-300 flex items-center justify-center gap-1.5 transition-colors border border-slate-700"
                >
                  <Smartphone className="w-4 h-4 text-amber-400" />
                  <span>Android APK কোড</span>
                </button>
              )}
            </div>

            {/* Clear Saved Game */}
            {onResetGameData && (
              <button
                type="button"
                onClick={() => {
                  if (confirm('আপনি কি পূর্বের সকল সংরক্ষিত খেলা মুছে নতুন শুরু করতে চান?')) {
                    onResetGameData();
                    onClose();
                  }
                }}
                className="p-2.5 rounded-xl bg-rose-950/40 hover:bg-rose-900/60 border border-rose-500/30 text-xs text-rose-300 flex items-center justify-center gap-1.5 transition-colors"
              >
                <Trash2 className="w-4 h-4 text-rose-400" />
                <span>সংরক্ষিত খেলার ডাটা রিসেট করুন</span>
              </button>
            )}
          </div>

          {/* Footer Close Button */}
          <button
            type="button"
            onClick={onClose}
            className="w-full py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-bold text-sm shadow transition-colors"
          >
            সেটিংস সম্পন্ন
          </button>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
