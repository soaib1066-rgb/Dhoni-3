import React from 'react';
import { Player, GameLog } from '../types/game';
import { DiceRoll } from './DiceRoll';
import { Volume2, VolumeX, HelpCircle, Code2, RefreshCw } from 'lucide-react';

interface CenterDashboardProps {
  activePlayer: Player;
  lastDiceRoll: number | null;
  isRolling: boolean;
  isMoving: boolean;
  onRollDice: () => void;
  logs: GameLog[];
  soundEnabled: boolean;
  onToggleSound: () => void;
  onOpenRules: () => void;
  onOpenAndroidExport: () => void;
  onResetGame: () => void;
}

export const CenterDashboard: React.FC<CenterDashboardProps> = ({
  activePlayer,
  lastDiceRoll,
  isRolling,
  isMoving,
  onRollDice,
  logs,
  soundEnabled,
  onToggleSound,
  onOpenRules,
  onOpenAndroidExport,
  onResetGame,
}) => {
  const latestLog = logs.length > 0 ? logs[logs.length - 1] : null;

  return (
    <div className="w-full h-full flex flex-col justify-between items-center text-center p-1 sm:p-2 select-none">
      {/* Top Header: Active Player Banner */}
      <div className="w-full flex items-center justify-between gap-1 border-b border-emerald-500/20 pb-2">
        <div className="flex items-center gap-2 text-left">
          <div
            className="w-3.5 h-3.5 sm:w-4 sm:h-4 rounded-full border border-white shadow"
            style={{ backgroundColor: activePlayer.hexColor }}
          />
          <div className="leading-none">
            <span className="text-[10px] text-slate-400 block">বর্তমান পালা</span>
            <span className="text-xs sm:text-sm font-black text-slate-100">
              {activePlayer.name}
            </span>
          </div>
        </div>

        {/* Action icons */}
        <div className="flex items-center gap-1">
          <button
            type="button"
            onClick={onToggleSound}
            className="p-1.5 rounded-lg bg-slate-800/80 text-slate-300 hover:text-white transition-colors"
            title={soundEnabled ? 'শব্দ বন্ধ করুন' : 'শব্দ চালু করুন'}
          >
            {soundEnabled ? <Volume2 className="w-3.5 h-3.5" /> : <VolumeX className="w-3.5 h-3.5 text-rose-400" />}
          </button>
          <button
            type="button"
            onClick={onOpenRules}
            className="p-1.5 rounded-lg bg-slate-800/80 text-slate-300 hover:text-white transition-colors"
            title="খেলার নিয়মাবলী"
          >
            <HelpCircle className="w-3.5 h-3.5" />
          </button>
          <button
            type="button"
            onClick={onOpenAndroidExport}
            className="p-1.5 rounded-lg bg-emerald-950 text-emerald-400 border border-emerald-500/40 hover:bg-emerald-900 transition-colors"
            title="Android Project & APK"
          >
            <Code2 className="w-3.5 h-3.5" />
          </button>
          <button
            type="button"
            onClick={onResetGame}
            className="p-1.5 rounded-lg bg-slate-800/80 text-slate-300 hover:text-rose-400 transition-colors"
            title="নতুন করে শুরু"
          >
            <RefreshCw className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* Middle: Dice & Prompt */}
      <div className="my-auto flex flex-col items-center justify-center py-1">
        <DiceRoll
          value={lastDiceRoll}
          isRolling={isRolling}
          disabled={isMoving}
          onRoll={onRollDice}
          isAI={activePlayer.isAI}
        />
      </div>

      {/* Bottom: Latest Status Ticker */}
      <div className="w-full bg-slate-900/90 rounded-xl px-2.5 py-1.5 border border-slate-800 text-[11px] sm:text-xs text-slate-300 flex items-center justify-center min-h-[34px]">
        {isMoving ? (
          <span className="text-amber-300 font-semibold animate-pulse">
            ঘুঁটি এগিয়ে চলেছে...
          </span>
        ) : latestLog ? (
          <span className="truncate">{latestLog.text}</span>
        ) : (
          <span className="text-slate-400">
            {activePlayer.isAI ? 'এআই খেলোয়াড় প্রস্তুতি নিচ্ছে...' : 'ছক্কা ঘুরিয়ে চাল দিন'}
          </span>
        )}
      </div>
    </div>
  );
};
