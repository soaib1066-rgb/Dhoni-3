import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { Player } from '../types/game';
import { formatBanglaTaka } from '../utils/banglaFormatter';
import { Lock, DollarSign, Clock } from 'lucide-react';
import { JAIL_FINE } from '../data/boardTiles';

interface JailModalProps {
  player: Player | null;
  onPayFine: () => void;
  onWaitTurn: () => void;
}

export const JailModal: React.FC<JailModalProps> = ({
  player,
  onPayFine,
  onWaitTurn,
}) => {
  if (!player) return null;

  const canAffordFine = player.balance >= JAIL_FINE;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 20 }}
          className="w-full max-w-sm bg-slate-900 border-2 border-rose-600/40 rounded-3xl p-5 shadow-2xl text-slate-100 flex flex-col gap-4 text-center"
        >
          <div className="mx-auto w-14 h-14 bg-rose-500/20 text-rose-400 rounded-2xl border border-rose-500/40 flex items-center justify-center shadow-lg shadow-rose-950/40">
            <Lock className="w-8 h-8" />
          </div>

          <div>
            <div className="text-xs uppercase tracking-wider text-rose-400 font-bold">
              জেলখানা
            </div>
            <h3 className="text-xl font-bold text-rose-200 mt-1">
              {player.name} জেলে আছেন!
            </h3>
          </div>

          <p className="text-xs text-slate-300 bg-slate-800/70 p-3 rounded-xl border border-slate-700 leading-relaxed">
            জেল থেকে মুক্ত হতে ৳১,০০০ জরিমানা দিন অথবা এই টার্নটি বিরতি হিসেবে অপেক্ষা করুন।
          </p>

          <div className="flex items-center justify-between text-xs px-3 py-2 bg-slate-950/60 rounded-xl border border-slate-800">
            <span className="text-slate-400">বর্তমান ব্যালেন্স:</span>
            <span className="font-bold text-emerald-400 tabular-nums">
              {formatBanglaTaka(player.balance)}
            </span>
          </div>

          <div className="flex flex-col gap-2 mt-2">
            <button
              type="button"
              onClick={onPayFine}
              disabled={!canAffordFine}
              className={`w-full py-3 rounded-xl font-bold text-sm shadow-md flex items-center justify-center gap-2 transition-all ${
                canAffordFine
                  ? 'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white border border-emerald-400/40'
                  : 'bg-slate-800 text-slate-500 cursor-not-allowed border border-slate-700'
              }`}
            >
              <DollarSign className="w-4 h-4" />
              <span>জরিমানা দিন ({formatBanglaTaka(JAIL_FINE)}) ও চালুন</span>
            </button>

            <button
              type="button"
              onClick={onWaitTurn}
              className="w-full py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 font-semibold text-xs transition-colors border border-slate-700 flex items-center justify-center gap-2"
            >
              <Clock className="w-4 h-4" />
              <span>১ টার্ন অপেক্ষা করুন</span>
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
