import React from 'react';
import { Player } from '../types/game';
import { formatBanglaTaka, toBanglaNumber } from '../utils/banglaFormatter';
import { Bot, User, Lock, Skull, Building } from 'lucide-react';

interface PlayerCardProps {
  player: Player;
  isActive: boolean;
}

export const PlayerCard: React.FC<PlayerCardProps> = ({ player, isActive }) => {
  return (
    <div
      className={`relative p-2.5 sm:p-3 rounded-2xl border transition-all duration-300 flex items-center justify-between gap-3 ${
        player.isBankrupt
          ? 'bg-rose-950/20 border-rose-900/40 opacity-60'
          : isActive
          ? 'bg-slate-800/95 border-emerald-400 shadow-lg shadow-emerald-950/50 scale-[1.02]'
          : 'bg-slate-900/80 border-slate-700/60 hover:bg-slate-800/60'
      }`}
    >
      {/* Left side: Avatar + Details */}
      <div className="flex items-center gap-2.5 min-w-0">
        <div
          className="relative w-9 h-9 sm:w-10 sm:h-10 rounded-xl flex items-center justify-center text-white font-bold text-sm shadow-md border-2 border-white/80 shrink-0"
          style={{ backgroundColor: player.hexColor }}
        >
          {player.isAI ? (
            <Bot className="w-5 h-5 text-white" />
          ) : (
            <User className="w-5 h-5 text-white" />
          )}

          {isActive && !player.isBankrupt && (
            <span className="absolute -top-1 -right-1 flex h-3 w-3">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-3 w-3 bg-emerald-500"></span>
            </span>
          )}
        </div>

        <div className="min-w-0">
          <div className="flex items-center gap-1.5">
            <span className="font-bold text-xs sm:text-sm text-slate-100 truncate">
              {player.name}
            </span>
            {player.isAI && (
              <span className="text-[10px] bg-slate-800 text-sky-400 px-1.5 py-0.2 rounded border border-sky-500/30">
                এআই
              </span>
            )}
          </div>

          <div className="flex items-center gap-2 text-[11px] text-slate-400 mt-0.5">
            <span className="flex items-center gap-1">
              <Building className="w-3 h-3 text-amber-400/80" />
              <span>{toBanglaNumber(player.ownedProperties.length)} টি জমি</span>
            </span>

            {player.inJail && !player.isBankrupt && (
              <span className="flex items-center gap-0.5 text-rose-400 font-medium">
                <Lock className="w-2.5 h-2.5" />
                <span>জেলে</span>
              </span>
            )}

            {player.isBankrupt && (
              <span className="flex items-center gap-0.5 text-rose-400 font-bold">
                <Skull className="w-2.5 h-2.5" />
                <span>দেউলিয়া</span>
              </span>
            )}
          </div>
        </div>
      </div>

      {/* Right side: Balance in Bengali digits */}
      <div className="text-right shrink-0">
        <div
          className={`font-black text-sm sm:text-base tabular-nums ${
            player.isBankrupt
              ? 'text-rose-400 line-through'
              : 'text-emerald-400'
          }`}
        >
          {formatBanglaTaka(player.balance)}
        </div>
      </div>
    </div>
  );
};
