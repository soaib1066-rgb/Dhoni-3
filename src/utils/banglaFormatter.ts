/**
 * Bangla number and currency formatter
 * Maps ASCII digits 0-9 to Bengali numerals: ০, ১, ২, ৩, ৪, ৫, ৬, ৭, ৮, ৯
 */

const BANGLA_DIGITS: Record<string, string> = {
  '0': '০',
  '1': '১',
  '2': '২',
  '3': '৩',
  '4': '৪',
  '5': '৫',
  '6': '৬',
  '7': '৭',
  '8': '৮',
  '9': '৯',
};

/**
 * Converts any number or string of digits to Bangla numerals
 */
export function toBanglaNumber(value: number | string): string {
  if (value === null || value === undefined) return '';
  const str = typeof value === 'number' ? value.toLocaleString('en-US') : String(value);
  return str.replace(/[0-9]/g, (digit) => BANGLA_DIGITS[digit] || digit);
}

/**
 * Formats a currency amount into Bangla Taka representation: e.g. 15000 -> "৳১৫,০০০"
 */
export function formatBanglaTaka(amount: number): string {
  const isNegative = amount < 0;
  const absAmount = Math.abs(amount);
  const formatted = absAmount.toLocaleString('en-US');
  const banglaDigits = formatted.replace(/[0-9]/g, (digit) => BANGLA_DIGITS[digit] || digit);
  return isNegative ? `-৳${banglaDigits}` : `৳${banglaDigits}`;
}

/**
 * Converts English ordinal to Bangla
 */
export function toBanglaOrdinal(num: number): string {
  const map: Record<number, string> = {
    1: '১ম',
    2: '২য়',
    3: '৩য়',
    4: '৪র্থ',
  };
  return map[num] || `${toBanglaNumber(num)}ম`;
}
