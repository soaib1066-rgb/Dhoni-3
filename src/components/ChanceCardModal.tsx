import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { ChanceCard, Player } from '../types/game';
import { formatBanglaTaka } from '../utils/banglaFormatter';
import { Sparkles, ArrowRight, TrendingUp, TrendingDown, MapPin } from 'lucide-react';

interface ChanceCardModalProps {
  card: ChanceCard | null;
  player: Player | null;
  onConfirm: () => void;
}

export const ChanceCardModal: React.FC<ChanceCardModalProps> = ({
  card,
  player,
  onConfirm,
}) => {
  if (!card || !player) return null;

  const isGain = card.amountEffect > 0;
  const isLoss = card.amountEffect < 0;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 20 }}
          className="w-full max-w-sm bg-gradient-to-b from-slate-900 to-amber-950/40 border-2 border-amber-500/50 rounded-3xl p-5 shadow-2xl text-slate-100 flex flex-col gap-4 text-center"
        >
          {/* Card Icon Header */}
          <div className="mx-auto w-14 h-14 bg-amber-500/20 text-amber-400 rounded-2xl border border-amber-500/40 flex items-center justify-center shadow-lg shadow-amber-950/40">
            <Sparkles className="w-8 h-8 animate-pulse" />
          </div>

          <div>
            <div className="text-xs uppercase tracking-wider text-amber-400 font-bold">
              ভাগ্য পরীক্ষা
            </div>
            <h3 className="text-xl font-extrabold text-amber-200 mt-1">
              {card.title}
            </h3>
          </div>

          {/* Description */}
          <p className="text-sm text-slate-200 bg-slate-800/80 p-3.5 rounded-2xl border border-slate-700 leading-relaxed font-medium">
            {card.description}
          </p>

          {/* Amount Effect */}
          {isGain && (
            <div className="flex items-center justify-center gap-2 text-emerald-400 bg-emerald-950/50 p-3 rounded-2xl border border-emerald-800/50 font-bold text-lg">
              <TrendingUp className="w-5 h-5" />
              <span>প্রাপ্তি: +{formatBanglaTaka(card.amountEffect)}</span>
            </div>
          )}

          {isLoss && (
            <div className="flex items-center justify-center gap-2 text-rose-400 bg-rose-950/50 p-3 rounded-2xl border border-rose-800/50 font-bold text-lg">
              <TrendingDown className="w-5 h-5" />
              <span>জরিমানা: {formatBanglaTaka(card.amountEffect)}</span>
            </div>
          )}

          {card.moveEffect && (
            <div className="flex items-center justify-center gap-2 text-sky-400 bg-sky-950/50 p-3 rounded-2xl border border-sky-800/50 font-bold text-sm">
              <MapPin className="w-4 h-4" />
              <span>
                {card.moveEffect === 'start'
                  ? 'শুরু ঘরে গমন'
                  : card.moveEffect === 'jail'
                  ? 'জেলখানায় গমন'
                  : 'নির্দিষ্ট ঘরে যাত্রা'}
              </span>
            </div>
          )}

          {/* Confirmation Button */}
          <button
            type="button"
            onClick={onConfirm}
            className="w-full mt-2 py-3 rounded-xl bg-gradient-to-r from-amber-600 to-amber-700 hover:from-amber-500 hover:to-amber-600 text-white font-bold text-sm tracking-wide shadow-lg shadow-amber-950/50 flex items-center justify-center gap-2 transition-all active:scale-95 border border-amber-400/40"
          >
            <span>কার্যকর করুন</span>
            <ArrowRight className="w-4 h-4" />
          </button>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
