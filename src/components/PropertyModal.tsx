import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { BoardTile, Player } from '../types/game';
import { formatBanglaTaka } from '../utils/banglaFormatter';
import { Building, ShieldCheck, AlertCircle } from 'lucide-react';

interface PropertyModalProps {
  property: BoardTile | null;
  player: Player | null;
  onBuy: () => void;
  onPass: () => void;
}

export const PropertyModal: React.FC<PropertyModalProps> = ({
  property,
  player,
  onBuy,
  onPass,
}) => {
  if (!property || !player) return null;

  const canAfford = player.balance >= property.cost;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-sm">
        <motion.div
          initial={{ opacity: 0, scale: 0.9, y: 20 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.9, y: 20 }}
          className="w-full max-w-sm bg-slate-900 border-2 border-emerald-500/40 rounded-3xl p-5 shadow-2xl text-slate-100 flex flex-col gap-4"
        >
          {/* Header */}
          <div className="flex items-center gap-3">
            <div className="p-3 bg-emerald-500/20 text-emerald-400 rounded-2xl border border-emerald-500/30">
              <Building className="w-6 h-6" />
            </div>
            <div>
              <div className="text-xs text-slate-400 font-medium">ক্রয়যোগ্য সম্পত্তি</div>
              <h3 className="text-xl font-bold text-emerald-300">{property.name}</h3>
            </div>
          </div>

          {/* Description */}
          {property.description && (
            <p className="text-xs text-slate-300 bg-slate-800/60 p-3 rounded-xl border border-slate-700/50 leading-relaxed">
              {property.description}
            </p>
          )}

          {/* Financial Breakdown */}
          <div className="grid grid-cols-2 gap-2 text-center">
            <div className="bg-slate-800/80 p-3 rounded-2xl border border-slate-700">
              <div className="text-[11px] text-slate-400">জমির মূল্য</div>
              <div className="text-base font-bold text-amber-300 tabular-nums">
                {formatBanglaTaka(property.cost)}
              </div>
            </div>
            <div className="bg-slate-800/80 p-3 rounded-2xl border border-slate-700">
              <div className="text-[11px] text-slate-400">নির্ধারিত ভাড়া</div>
              <div className="text-base font-bold text-emerald-300 tabular-nums">
                {formatBanglaTaka(property.rent)}
              </div>
            </div>
          </div>

          {/* Player balance check */}
          <div className="flex items-center justify-between text-xs px-2 py-1 bg-slate-950/60 rounded-xl border border-slate-800">
            <span className="text-slate-400">{player.name}-এর ব্যালেন্স:</span>
            <span className="font-bold text-emerald-400 tabular-nums">
              {formatBanglaTaka(player.balance)}
            </span>
          </div>

          {!canAfford && (
            <div className="flex items-center gap-2 text-rose-400 text-xs bg-rose-950/40 p-2.5 rounded-xl border border-rose-800/40">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>আপনার পর্যাপ্ত ব্যালেন্স নেই। জমিটি কেনা সম্ভব নয়।</span>
            </div>
          )}

          {/* Actions */}
          <div className="flex items-center gap-3 mt-1">
            <button
              type="button"
              onClick={onPass}
              className="flex-1 py-2.5 rounded-xl bg-slate-800 hover:bg-slate-700 text-slate-300 font-semibold text-sm transition-colors border border-slate-700"
            >
              পাস করুন
            </button>
            <button
              type="button"
              onClick={onBuy}
              disabled={!canAfford}
              className={`flex-1 py-2.5 rounded-xl font-bold text-sm transition-all shadow-md ${
                canAfford
                  ? 'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 text-white border border-emerald-400/40 shadow-emerald-950/40'
                  : 'bg-slate-800 text-slate-500 cursor-not-allowed border border-slate-700'
              }`}
            >
              কিনুন
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
