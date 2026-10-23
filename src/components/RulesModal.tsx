import React from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { X, BookOpen, Coins, ShieldAlert, Award, Compass } from 'lucide-react';

interface RulesModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const RulesModal: React.FC<RulesModalProps> = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <AnimatePresence>
      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm">
        <motion.div
          initial={{ opacity: 0, scale: 0.95, y: 15 }}
          animate={{ opacity: 1, scale: 1, y: 0 }}
          exit={{ opacity: 0, scale: 0.95, y: 15 }}
          className="w-full max-w-lg bg-slate-900 border border-emerald-500/30 rounded-3xl p-6 shadow-2xl text-slate-100 flex flex-col max-h-[85vh]"
        >
          {/* Header */}
          <div className="flex items-center justify-between pb-4 border-b border-slate-800">
            <div className="flex items-center gap-2.5">
              <div className="p-2 bg-emerald-500/20 text-emerald-400 rounded-xl">
                <BookOpen className="w-5 h-5" />
              </div>
              <h2 className="text-xl font-bold text-slate-100">খেলার নিয়মাবলী</h2>
            </div>
            <button
              type="button"
              onClick={onClose}
              className="p-1.5 rounded-lg text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
            >
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Rules List */}
          <div className="overflow-y-auto py-4 space-y-4 text-xs sm:text-sm text-slate-300 pr-1">
            <div className="flex items-start gap-3 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/50">
              <Coins className="w-5 h-5 text-amber-400 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-slate-100 text-sm mb-1">শুরুর মূলধন ও ছক্কার চাল</h4>
                <p>
                  প্রতি খেলোয়াড় শুরুতে ৳১৫,০০০ পাবেন। পালাক্রমে ১ থেকে ৬ পর্যন্ত ছক্কা ঘুরিয়ে ঘুঁটি ঘড়ির কাঁটার দিকে ২০টি ঘরে ঘুরবে।
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/50">
              <Compass className="w-5 h-5 text-emerald-400 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-slate-100 text-sm mb-1">শুরু ঘর ও বোনাস</h4>
                <p>
                  প্রতিবার সম্পূর্ণ বোর্ড ঘুরে শুরু ঘর অতিক্রম করলে খেলোয়াড় স্বয়ংক্রিয়ভাবে ৳২,০০০ বোনাস পাবেন।
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/50">
              <BookOpen className="w-5 h-5 text-sky-400 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-slate-100 text-sm mb-1">জমি কেনা ও ভাড়া আদায়</h4>
                <p>
                  মালিকবিহীন ঘরে পৌঁছালে নির্ধারিত মূল্যে জমি কেনার সুযোগ থাকবে। অন্য কোনো খেলোয়াড় আপনার জমিতে আসলে নির্ধারিত ভাড়া আপনার তহবিলে জমা হবে।
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/50">
              <ShieldAlert className="w-5 h-5 text-rose-400 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-slate-100 text-sm mb-1">জেলখানা, ভাগ্য পরীক্ষা ও কর</h4>
                <p>
                  জেলখানায় গেলে ১ টার্ন বিরতি অথবা ৳১,০০০ জরিমানা দিতে হবে। করের ঘরে ৳১,০০০ সরকারি কোষাগারে প্রদান করতে হবে। ভাগ্য পরীক্ষা ঘরে বিভিন্ন অভাবনীয় ঘটনা ঘটবে।
                </p>
              </div>
            </div>

            <div className="flex items-start gap-3 p-3 rounded-2xl bg-slate-800/60 border border-slate-700/50">
              <Award className="w-5 h-5 text-amber-300 shrink-0 mt-0.5" />
              <div>
                <h4 className="font-bold text-slate-100 text-sm mb-1">জয়ী হওয়ার নিয়ম (২টি মাধ্যম)</h4>
                <p className="leading-relaxed">
                  ১. <strong>দ্রুততম ধনী:</strong> যে খেলোয়াড় সবার আগে <strong>৳৩০,০০০</strong> নগদ মূলধন অর্জন করবেন, তিনি তাৎক্ষণিক বিজয়ী হবেন।<br />
                  ২. <strong>দেউলিয়া প্রতিযোগিতা:</strong> তহবিল শূন্য হয়ে বাকি সব খেলোয়াড় দেউলিয়া হলে টিকে থাকা শেষ খেলোয়াড় বিজয়ী হবেন।
                </p>
              </div>
            </div>
          </div>

          {/* Close */}
          <div className="pt-3 border-t border-slate-800">
            <button
              type="button"
              onClick={onClose}
              className="w-full py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 font-bold text-white transition-colors"
            >
              বুঝেছি
            </button>
          </div>
        </motion.div>
      </div>
    </AnimatePresence>
  );
};
