export type PlayerColor = 'red' | 'blue' | 'green' | 'yellow';

export interface Player {
  id: string;
  name: string;
  color: PlayerColor;
  hexColor: string;
  balance: number;
  currentPosition: number; // 0 to 19
  ownedProperties: number[]; // Tile IDs
  inJail: boolean;
  jailTurnsRemaining: number;
  isBankrupt: boolean;
  isAI: boolean;
}

export type TileType = 
  | 'start' 
  | 'property' 
  | 'jail' 
  | 'chance' 
  | 'tax';

export interface BoardTile {
  id: number; // 0 to 19
  name: string;
  type: TileType;
  group?: 'city_a' | 'city_b' | 'city_c' | 'transport' | 'special';
  cost: number;
  rent: number;
  ownerId?: string;
  description?: string;
  iconName?: string;
}

export interface ChanceCard {
  id: number;
  title: string;
  description: string;
  amountEffect: number; // positive = gain, negative = pay
  moveEffect?: number | 'start' | 'jail'; // target tile ID or special target
}

export type GameStatus = 'idle' | 'playing' | 'game_over';

export interface GameState {
  players: Player[];
  tiles: BoardTile[];
  currentPlayerIndex: number;
  lastDiceRoll: number | null;
  isRolling: boolean;
  isMoving: boolean;
  gameStatus: GameStatus;
  winnerId: string | null;
  winnerReason?: string | null;
  pendingPurchase: BoardTile | null;
  activeChanceCard: ChanceCard | null;
  activeJailChoice: boolean;
  gameMode: 'bot' | 'multiplayer' | null;
  turnCount: number;
  logs: GameLog[];
  soundEnabled: boolean;
}

export interface GameLog {
  id: string;
  timestamp: number;
  text: string;
  type: 'info' | 'cash_in' | 'cash_out' | 'chance' | 'jail' | 'dice';
  playerId?: string;
}
