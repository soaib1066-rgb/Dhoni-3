import React from 'react';
import { motion } from 'motion/react';
import { toBanglaNumber } from '../utils/banglaFormatter';

interface DiceRollProps {
  value: number | null;
  isRolling: boolean;
  disabled: boolean;
  onRoll: () => void;
  isAI?: boolean;
}

export const DiceRoll: React.FC<DiceRollProps> = ({
  value,
  isRolling,
  disabled,
  onRoll,
  isAI = false,
}) => {
  // Dot coordinates for 3x3 dice face
  const getDots = (num: number) => {
    switch (num) {
      case 1:
        return [4];
      case 2:
        return [0, 8];
      case 3:
        return [0, 4, 8];
      case 4:
        return [0, 2, 6, 8];
      case 5:
        return [0, 2, 4, 6, 8];
      case 6:
        return [0, 2, 3, 5, 6, 8];
      default:
        return [4];
    }
  };

  const activeValue = value || 1;
  const dots = getDots(activeValue);

  return (
    <div className="flex flex-col items-center justify-center gap-3">
      <motion.div
        animate={
          isRolling
            ? {
                rotate: [0, 90, 180, 270, 360],
                scale: [1, 1.15, 0.9, 1.1, 1],
                y: [0, -12, 4, -8, 0],
              }
            : { rotate: 0, scale: 1, y: 0 }
        }
        transition={
          isRolling
            ? { repeat: Infinity, duration: 0.35, ease: 'linear' }
            : { duration: 0.2 }
        }
        className="relative w-16 h-16 sm:w-20 sm:h-20 bg-gradient-to-br from-amber-50 to-amber-200 border-2 border-amber-500 rounded-2xl shadow-xl shadow-amber-950/40 p-2.5 sm:p-3 flex items-center justify-center select-none"
      >
        <div className="grid grid-cols-3 grid-rows-3 w-full h-full gap-1">
          {[0, 1, 2, 3, 4, 5, 6, 7, 8].map((idx) => (
            <div key={idx} className="flex items-center justify-center">
              {dots.includes(idx) && (
                <motion.div
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  className="w-2.5 h-2.5 sm:w-3 sm:h-3 rounded-full bg-red-600 shadow-inner"
                />
              )}
            </div>
          ))}
        </div>

        {/* Small Bangla number badge at corner */}
        {value && !isRolling && (
          <div className="absolute -top-2 -right-2 bg-emerald-600 text-white font-bold text-xs px-2 py-0.5 rounded-full border border-emerald-400 shadow">
            {toBanglaNumber(value)}
          </div>
        )}
      </motion.div>

      <button
        type="button"
        onClick={onRoll}
        disabled={disabled || isRolling || isAI}
        className={`px-5 py-2.5 rounded-xl font-bold text-sm sm:text-base tracking-wide transition-all duration-200 shadow-md ${
          disabled || isRolling || isAI
            ? 'bg-slate-700/60 text-slate-400 cursor-not-allowed border border-slate-600/40'
            : 'bg-gradient-to-r from-emerald-600 to-teal-600 hover:from-emerald-500 hover:to-teal-500 active:scale-95 text-white border border-emerald-400/50 shadow-emerald-900/40 hover:shadow-lg'
        }`}
      >
        {isRolling
          ? 'ছক্কা ঘুরছে...'
          : isAI
          ? 'এআই ভাবছে...'
          : 'ছক্কা চালুন (Roll)'}
      </button>
    </div>
  );
};
