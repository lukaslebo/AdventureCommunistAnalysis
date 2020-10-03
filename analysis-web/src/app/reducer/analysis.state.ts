import {Decimal} from 'decimal.js';

export interface AnalysisRootState {
	currentRank: number;
	researcher: ResearcherStateMap;
	analysis: AnalysisMap;
}

export type ResearcherStateMap = { [id: string]: ResearcherState };
export type AnalysisMap = { [id: string]: Analysis };

export type Industry =
	| 'Potato'
	| 'Land'
	| 'Ore'
	| 'Military'
	| 'Placebo'
	| 'All';

export type Modifier =
	| 'Speed'
	| 'Power'
	| 'Trade'
	| 'Chance'
	| 'Discount'
	| 'Bonus'
	| 'SinglePower';
export type Rarity = 'Common' | 'Rare' | 'Epic' | 'Supreme';
export type Section =
	| 'Potato'
	| 'Land'
	| 'Ore'
	| 'Military'
	| 'Placebo'
	| 'Supreme'
	| 'Trade';
export const sections = [
	'Potato',
	'Land',
	'Ore',
	'Military',
	'Placebo',
	'Supreme',
	'Trade'
];

export interface Researcher {
	id: string;
	researcherName: string;
	unlockedAtRank: number;
	industry: Industry;
	modifier: Modifier;
	rarity: Rarity;
}

export interface ResearcherState {
	currentLevel: number;
	availableCards: number;
}

export interface Analysis {
	boost: Decimal;
	boostPer1kScience: Decimal;
	upgradeCost: Decimal;
	upgradeCardCost: Decimal;
	canUpgrade: boolean;
}
