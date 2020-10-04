import {Injectable} from '@angular/core';
import {Decimal} from 'decimal.js';
import {Analysis, Industry, Researcher, ResearcherStateMap} from '../reducer/analysis.state';
import {Researchers} from '../reducer/researcher.state';

const ZERO = new Decimal(0);
const TWO = new Decimal(2);
const _5K = new Decimal(5_000);

@Injectable()
export class AnalysisService {
	analyze(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Analysis {
		const upgradeCost = this.getUpgradeCost(researcher, researcherStateMap);
		const upgradeCardCost = this.getUpgradeCardCost(
			researcher,
			researcherStateMap
		);
		const totalUpgradeCost = upgradeCost?.plus(upgradeCardCost) ?? ZERO;
		const canUpgrade = this.canUpgrade(researcher, researcherStateMap);
		const boost =
			canUpgrade || researcher.rarity === 'Supreme'
				? this.getBoost(researcher, researcherStateMap)
				: ZERO;
		const boostPer1kScience = totalUpgradeCost.gt(0)
			? boost.div(totalUpgradeCost).times(1000)
			: ZERO;

		const analysis: Analysis = {
			boost: boost.toString(),
			boostPer1kScience: boostPer1kScience.toString(),
			canUpgrade,
			upgradeCardCost: upgradeCardCost.toString(),
			upgradeCost: upgradeCost.toString()
		};

		return analysis;
	}

	getIndustryPower(industry: Industry, researcherStateMap: ResearcherStateMap) {
		if (industry === 'All') return ZERO;

		const researchers = Researchers.allResearchers.filter(
			(r) => r.industry === industry || r.industry === 'All'
		);

		let power = new Decimal(1);
		for (let researcher of researchers) {
			const lvl = researcherStateMap[researcher.id].currentLevel ?? 0;
			if (
				researcher.modifier === 'Speed' ||
				researcher.modifier === 'SinglePower'
			) {
				power = power.times(TWO.pow(lvl));
			} else if (researcher.modifier === 'Power') {
				power = power.times(
					TWO.pow(lvl).pow(
						this.getCommonCountForIndustry(
							researcher.industry,
							researcherStateMap
						)
					)
				);
			}
		}

		const discountFactor = this.getDiscountFactorForIndustry(
			industry,
			researcherStateMap
		);
		const chance = this.getChanceForIndustry(industry, researcherStateMap);
		const bonusMultiplier = this.getBonusMultiplierForIndustry(
			industry,
			researcherStateMap
		);

		power = power.times(
			new Decimal(1).minus(chance).plus(chance.times(bonusMultiplier))
		);
		power = power.times(discountFactor);

		return power;
	}

	private getBoost(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Decimal {
		switch (researcher.modifier) {
			case 'Speed':
				return new Decimal('1');
			case 'Power':
				return new Decimal(
					this.getCommonCountForIndustry(
						researcher.industry,
						researcherStateMap
					)
				);
			case 'Trade':
				return this.getTradeBoost(researcher, researcherStateMap);
			case 'Chance':
				return this.getChanceBoost(researcher, researcherStateMap);
			case 'Discount':
				return new Decimal('3.32192809489');
			case 'Bonus':
				return new Decimal('2').times(
					new Decimal(
						this.getCommonCountForIndustry(
							researcher.industry,
							researcherStateMap
						)
					)
				);
			case 'SinglePower':
				return new Decimal('1');
		}
	}

	private getTradeBoost(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Decimal {
		let supremeTradeLvl =
			researcherStateMap[
				Researchers.allResearchers.find(
					(r) => r.rarity === 'Supreme' && r.modifier === 'Trade'
				)?.id
				]?.currentLevel ?? 0;

		const industries: Industry[] = [
			'Potato',
			'Land',
			'Ore',
			'Military',
			'Placebo'
		];
		const currentTradePower = industries
			.map((industry) => {
				const lvl =
					researcherStateMap[
						Researchers.allResearchers.find(
							(r) => r.modifier === 'Trade' && r.industry === industry
						)?.id
						]?.currentLevel ?? 0;
				const industryTradeExponent = this.getIndustryPower(
					industry,
					researcherStateMap
				).log(_5K);
				return TWO.pow(lvl)
					.times(TWO.pow(industryTradeExponent))
					.times(TWO.pow(supremeTradeLvl))
					.times(this.getIndustryTradeMultiplier(industry));
			})
			.reduce((acc, power) => acc.plus(power), ZERO);

		if (researcher.rarity === 'Supreme') supremeTradeLvl++;
		const boostedTradePower = industries
			.map((industry) => {
				let lvl =
					researcherStateMap[
						Researchers.allResearchers.find(
							(r) => r.modifier === 'Trade' && r.industry === industry
						)?.id
						]?.currentLevel ?? 0;
				if (researcher.industry === industry) lvl++;
				const industryTradeExponent = this.getIndustryPower(
					industry,
					researcherStateMap
				).log(_5K);
				return TWO.pow(lvl)
					.times(TWO.pow(industryTradeExponent))
					.times(TWO.pow(supremeTradeLvl))
					.times(this.getIndustryTradeMultiplier(industry));
			})
			.reduce((acc, power) => acc.plus(power), ZERO);

		return boostedTradePower.div(currentTradePower);
	}

	private getCommonCountForIndustry(
		industry: Industry,
		researcherStateMap: ResearcherStateMap
	) {
		return Researchers.allResearchers
			.filter(
				(r) =>
					(r.industry === industry || industry == 'All') &&
					r.rarity === 'Common'
			)
			.map((r) => researcherStateMap[r.id].currentLevel)
			.filter((lvl) => lvl > 0).length;
	}

	private getChanceBoost(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	) {
		const chance = this.getChanceForIndustry(
			researcher.industry,
			researcherStateMap
		);
		const nextChance = this.getChanceForIndustry(
			researcher.industry,
			researcherStateMap,
			true
		);
		const multiplier = this.getBonusMultiplierForIndustry(
			researcher.industry,
			researcherStateMap
		);
		const power = new Decimal(1).minus(chance).plus(chance.times(multiplier));
		const nextPower = new Decimal(1)
			.minus(chance)
			.plus(nextChance.times(multiplier));
		const boost = nextPower.div(power);
		return boost.log(TWO);
	}

	private getBonusMultiplierForIndustry(
		industry: Industry,
		researcherStateMap: ResearcherStateMap
	) {
		return Researchers.allResearchers
			.filter(
				(r) =>
					r.modifier == 'Bonus' &&
					(r.industry == industry || r.industry == 'All')
			)
			.map((r) => researcherStateMap[r.id].currentLevel)
			.filter((lvl) => lvl > 0)
			.reduce((acc, lvl) => acc.times(new Decimal(4).pow(lvl)), new Decimal(2));
	}

	private getDiscountFactorForIndustry(
		industry: Industry,
		researcherStateMap: ResearcherStateMap
	) {
		return Researchers.allResearchers
			.filter(
				(r) =>
					r.modifier === 'Discount' &&
					(r.industry === industry || r.industry === 'All')
			)
			.map((r) => researcherStateMap[r.id].currentLevel ?? 0)
			.reduce(
				(acc, lvl) => acc.times(this.getDiscountFactorFromLvl(lvl)),
				new Decimal(1)
			);
	}

	private getDiscountFactorFromLvl(lvl: number) {
		return new Decimal(10).pow(lvl);
	}

	private getChanceForIndustry(
		industry: Industry,
		researcherStateMap: ResearcherStateMap,
		simulatePlusOneLvl: Boolean = false
	) {
		return Researchers.allResearchers
			.filter(
				(r) =>
					r.modifier === 'Chance' &&
					(r.industry === industry || r.industry === 'All')
			)
			.map((r) => {
				let lvl = researcherStateMap[r.id]?.currentLevel ?? 0;
				if (simulatePlusOneLvl && (r.industry !== 'All' || industry == 'All')) {
					lvl++;
				}
				return this.getChanceFromLvl(lvl, r.rarity === 'Supreme');
			})
			.reduce((acc, chance) => acc.plus(chance), new Decimal('0.01'));
	}

	private getChanceFromLvl(chanceLvl: number, isSupreme: Boolean): Decimal {
		if (isSupreme) {
			switch (chanceLvl) {
				case 1:
					return new Decimal('0.08');
				case 2:
					return new Decimal('0.13');
				case 3:
					return new Decimal('0.19');
				case 4:
					return new Decimal('0.26');
				case 5:
					return new Decimal('0.34');
				default:
					return ZERO;
			}
		} else {
			switch (chanceLvl) {
				case 1:
					return new Decimal('0.05');
				case 2:
					return new Decimal('0.0725');
				case 3:
					return new Decimal('0.1');
				case 4:
					return new Decimal('0.1325');
				case 5:
					return new Decimal('0.17');
				case 6:
					return new Decimal('0.2125');
				case 7:
					return new Decimal('0.26');
				case 8:
					return new Decimal('0.3125');
				case 9:
					return new Decimal('0.37');
				case 10:
					return new Decimal('0.4325');
				case 11:
					return new Decimal('0.5');
				default:
					return ZERO;
			}
		}
	}

	private getUpgradeCost(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Decimal {
		const lvl = researcherStateMap[researcher.id].currentLevel ?? 0;
		switch (researcher.rarity) {
			case 'Common':
				switch (lvl) {
					case 0:
						return ZERO;
					case 1:
						return new Decimal(50);
					case 2:
						return new Decimal(100);
					case 3:
						return new Decimal(200);
					case 4:
						return new Decimal(300);
					case 5:
						return new Decimal(400);
					case 6:
						return new Decimal(1_000);
					case 7:
						return new Decimal(2_000);
					case 8:
						return new Decimal(4_000);
					case 9:
						return new Decimal(8_000);
					case 10:
						return new Decimal(20_000);
					case 11:
						return new Decimal(50_000);
					case 12:
						return new Decimal(100_000);
					case 13:
						return ZERO;
				}
			case 'Rare':
				switch (lvl) {
					case 0:
						return ZERO;
					case 1:
						return new Decimal(100);
					case 2:
						return new Decimal(200);
					case 3:
						return new Decimal(400);
					case 4:
						return new Decimal(1_000);
					case 5:
						return new Decimal(2_000);
					case 6:
						return new Decimal(4_000);
					case 7:
						return new Decimal(8_000);
					case 8:
						return new Decimal(20_000);
					case 9:
						return new Decimal(50_000);
					case 10:
						return new Decimal(100_000);
					case 11:
						return ZERO;
				}
			case 'Epic':
				switch (lvl) {
					case 0:
						return ZERO;
					case 1:
						return new Decimal(400);
					case 2:
						return new Decimal(2_000);
					case 3:
						return new Decimal(4_000);
					case 4:
						return new Decimal(8_000);
					case 5:
						return new Decimal(20_000);
					case 6:
						return new Decimal(50_000);
					case 7:
						return new Decimal(100_000);
					case 8:
						return ZERO;
				}
			case 'Supreme':
				switch (lvl) {
					case 0:
						return ZERO;
					case 1:
						return new Decimal(5_000);
					case 2:
						return new Decimal(20_000);
					case 3:
						return new Decimal(50_000);
					case 4:
						return new Decimal(100_000);
					case 5:
						return ZERO;
				}
		}
	}

	private getRequiredCardsForUpgrade(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Decimal {
		const lvl = researcherStateMap[researcher.id].currentLevel ?? 0;
		switch (lvl) {
			case 0:
				return new Decimal(1);
			case 1:
				return new Decimal(2);
			case 2:
				return new Decimal(5);
			case 3:
				return new Decimal(10);
			case 4:
				return new Decimal(20);
			case 5:
				return new Decimal(50);
			case 6:
				return new Decimal(100);
			case 7:
				return new Decimal(200);
			case 8:
				return new Decimal(400);
			case 9:
				return new Decimal(800);
			case 10:
				return new Decimal(1_000);
			case 11:
				return new Decimal(2_000);
			case 12:
				return new Decimal(5_000);
			default:
				return ZERO;
		}
	}

	private getUpgradeCardCost(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	): Decimal {
		const availableCards = researcherStateMap[researcher.id].availableCards;
		if (availableCards != null) {
			const requiredCards = this.getRequiredCardsForUpgrade(
				researcher,
				researcherStateMap
			);
			const missingCards = requiredCards.lte(availableCards)
				? ZERO
				: requiredCards.minus(availableCards);
			return missingCards.times(this.getCardCosts(researcher));
		} else {
			return ZERO;
		}
	}

	private getCardCosts(researcher: Researcher) {
		switch (researcher.rarity) {
			case 'Common':
				return new Decimal(10);
			case 'Rare':
				return new Decimal(100);
			case 'Epic':
				return new Decimal(1_000);
			case 'Supreme':
				return new Decimal(40_000);
		}
	}

	private canUpgrade(
		researcher: Researcher,
		researcherStateMap: ResearcherStateMap
	) {
		const lvl = researcherStateMap[researcher.id]?.currentLevel ?? 0;
		switch (researcher.rarity) {
			case 'Common':
				return lvl < 13;
			case 'Rare':
				return lvl < 11;
			case 'Epic':
				return lvl < 8;
			case 'Supreme':
				return lvl < 5;
		}
	}

	private getIndustryTradeMultiplier(industry: Industry) {
		switch (industry) {
			case 'Potato':
				return new Decimal(1);
			case 'Land':
				return new Decimal(2);
			case 'Ore':
				return new Decimal(3);
			case 'Military':
				return new Decimal(4);
			case 'Placebo':
				return new Decimal(5);
			case 'All':
				return new Decimal(1 + 2 + 3 + 4 + 5);
		}
	}
}
