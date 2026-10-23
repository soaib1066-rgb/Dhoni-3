import React, { useEffect } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import confetti from 'canvas-confetti';
import { Player } from '../types/game';
import { formatBanglaTaka, toBanglaNumber } from '../utils/banglaFormatter';
import { Trophy, RotateCcw, Home, Crown, CheckCircle2 } from 'lucide-react';

interface WinnerModalProps {
  winner: Player | null;
  reason?: string | null;
  onPlayAgain: () => void;
  onGoHome: () => void;
}

export const WinnerModal: React.FC<WinnerModalProps> = ({
  winner,
  reason,
  onPlayAgain,
  onGoHome,
}) => {
  useEffect(() => {
    if (winner) {
      // Fire victory confetti bursts
      const duration = 3 * 1000;
      const end = Date.now() + duration;

      const frame = () => {
        confetti({
          particleCount: 4,
          angle: 60,
          spread: 55,
          origin: { x: 0 },
          colors: ['#10B981', '#F59E0B', '#EF4444', '#3B82F6'],
        });
        confetti({
          particleCount: 4,
          angle: 120,
          spread: 55,
          origin: { x: 1 },
          colors: ['#10B981', '#F59E0B', '#EF4444', '#3B82F6'],
        });

        if (Date.now() < end) {
          requestAnimationFrame(frame);
        }
      };
      frame();
    }
  }, [winner]);

  if (!winner) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/85 backdrop-blur-md">
        <motion.div
          initial={{ opacity: 0, scale: 0.8, y: 30 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.8, y: 30 }}
          className="w-full max-w-md bg-gradient-to-b from-slate-900 via-emerald-950/40 to-slate-900 border-2 border-emerald-500/60 rounded-3xl p-6 sm:p-8 shadow-2xl text-slate-100 flex flex-col items-center text-center gap-5 relative overflow-hidden"
        >
          {/* Decorative crown / glow */}
          <div className="absolute top-0 inset-x-0 h-2 bg-gradient-to-r from-amber-400 via-emerald-400 to-amber-400" />

          <div className="relative">
            <motion.div
              animate={{ rotate: [0, -10, 10, -10, 0], scale: [1, 1.1, 1] }}
              transition={{ repeat: Infinity, duration: 2.5 }}
              className="w-20 h-20 rounded-3xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-slate-950 shadow-xl shadow-amber-500/30 border-2 border-amber-300"
            >
              <Trophy className="w-11 h-11" />
            </motion.div>
            <div className="absolute -top-3 -right-2 text-amber-300">
              <Crown className="w-7 h-7" />
            </div>
          </div>

          <div>
            <div className="text-xs font-bold tracking-widest uppercase text-emerald-400">
              অভিনন্দন! বিজয়ী ঘোষিত
            </div>
            <h2 className="text-3xl font-black text-amber-300 mt-1">
              {winner.name}
            </h2>
            <p className="text-xs text-amber-200 mt-1 font-medium bg-amber-500/10 px-3 py-1.5 rounded-xl border border-amber-500/20">
              {reason || 'সবাইকে ছাড়িয়ে সবচেয়ে ধনী ব্যবসায়ী হলেন!'}
            </p>
          </div>

          {/* Stats summary */}
          <div className="w-full grid grid-cols-2 gap-3 bg-slate-800/80 p-4 rounded-2xl border border-slate-700/60">
            <div>
              <div className="text-[11px] text-slate-400">চূড়ান্ত নগদ তহবিল</div>
              <div className="text-lg font-black text-emerald-300 tabular-nums">
                {formatBanglaTaka(winner.balance)}
              </div>
            </div>
            <div>
              <div className="text-[11px] text-slate-400">মালিকানাধীন জমি</div>
              <div className="text-lg font-black text-amber-300 tabular-nums">
                {toBanglaNumber(winner.ownedProperties.length)} টি
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="w-full flex flex-col sm:flex-row items-center gap-3 mt-2">
            <button
              type="button"
              onClick={onPlayAgain}
              className="w-full sm:flex-1 py-3 px-4 rounded-xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white font-bold text-sm shadow-lg shadow-emerald-950/50 flex items-center justify-center gap-2 border border-emerald-400/40 active:scale-95 transition-all"
            >
              <RotateCcw className="w-4 h-4" />
              <span>আবার খেলুন</span>
            </button>
            <button
              type="button"
              onClick={onGoHome}
              className="w-full sm:flex-1 py-3 px-4 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 font-semibold text-sm border border-slate-700 flex items-center justify-center gap-2 transition-colors"
            >
              <Home className="w-4 h-4" />
              <span>হোম মেনু</span>
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
