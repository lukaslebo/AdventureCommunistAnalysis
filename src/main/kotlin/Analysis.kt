import Industry.All
import Modifier.*
import Rarity.*
import java.lang.Integer.max
import java.math.BigDecimal
import java.math.RoundingMode

private val TWO = BigDecimal(2)
private val NINE = BigDecimal(9)

sealed class Analysis(val researcher: Researcher) {
	val upgradeCost = getUpgradeCost(researcher)
	val upgradeCardCost = getUpgradeCardCost(researcher)
	val totalUpgradeCost = upgradeCost + upgradeCardCost
	val canUpgrade = upgradeCost > BigDecimal.ZERO
}

class PowerAnalysis(
	researcher: Researcher
) : Analysis(researcher) {
	val industryBoost = getIndustryBoostAsExponentOfTwo(researcher)
	val boostPer1kScience = getBoostPer1kScience(totalUpgradeCost, industryBoost)
}

class TradeAnalysis(
	researcher: Researcher
) : Analysis(researcher) {
	init {
		if (researcher.modifier != Trade)
			throw Exception("Cannot do trade analysis for researcher with modifier ${researcher.modifier}")
	}

	val tradeBoost = getTradeBoost(researcher)
	val boostPer1kScience = getBoostPer1kScience(totalUpgradeCost, tradeBoost)
}

fun Industry.getIndustryPower(): BigDecimal {
	val industry = this
	if (industry == All) {
		return BigDecimal.ZERO
	}
	val researchers = Researcher.getAllByIndustry(industry, true)

	var power: BigDecimal = BigDecimal.ONE
	for (researcher in researchers) {
		when (researcher.modifier) {
			Speed -> power *= TWO.pow(researcher.getLevel())
			Power -> power *= TWO.pow(researcher.getLevel())
				.pow(researcher.industry.getCommonCountForIndustry().intValueExact())
			SinglePower -> power *= NINE.times(TWO.pow(max(researcher.getLevel() - 1, 0)))
			Chance -> Unit
			Bonus -> Unit
			Discount -> Unit
			Trade -> Unit
		}
	}

	val discountFactor = industry.getDiscountFactorForIndustry()
	val chance = industry.getChanceForIndustry()
	val bonusMultiplier = industry.getBonusMultiplierForIndustry()
	power *= ((BigDecimal.ONE - chance) + (chance * bonusMultiplier))
	power *= discountFactor

	return power.stripTrailingZeros()
}

private fun getIndustryBoostAsExponentOfTwo(researcher: Researcher): BigDecimal = when (researcher.modifier) {
	Speed -> BigDecimal.ONE
	Power -> researcher.industry.getCommonCountForIndustry()
	Trade -> BigDecimal.ZERO
	Chance -> {
		val chance = researcher.industry.getChanceForIndustry()
		val nextChance = researcher.industry.getChanceForIndustry(true)
		val multiplier = researcher.industry.getBonusMultiplierForIndustry()
		val power = ((BigDecimal.ONE - chance) + (chance * multiplier))
		val nextPower = ((BigDecimal.ONE - chance) + (nextChance * multiplier))
		val boost = nextPower.divide(power, 3, RoundingMode.HALF_UP).stripTrailingZeros()
		boost.log(2, 2)
	}
	Discount -> BigDecimal(10).log(2, 2)
	Bonus -> researcher.industry.getCommonCountForIndustry() * TWO
	SinglePower -> if (researcher.getLevel() == 0) NINE else TWO
}

private fun getTradeBoost(researcher: Researcher): BigDecimal {
	var supremeTradeLvl =
		Researcher.values().first { it.rarity == Supreme && it.modifier == Trade }.getLevel()

	val currentTradePower = Industry.values()
		.filter { it != All }
		.map { industry ->
			val lvl = Researcher.values().first { it.modifier == Trade && it.industry == industry }.getLevel()
			val industryTradeExponent = industry.getIndustryPower().log(5_000, 6)
			TWO.pow(lvl) * TWO.pow(industryTradeExponent.toInt()) * TWO.pow(supremeTradeLvl) * industry.tradeMultiplier
		}
		.fold(BigDecimal.ZERO) { acc, p -> acc + p }

	if (researcher.rarity == Supreme) supremeTradeLvl++

	val boostedTradePower = Industry.values()
		.filter { it != All }
		.map { industry ->
			var lvl = Researcher.values().first { it.modifier == Trade && it.industry == industry }.getLevel()
			if (industry == researcher.industry) lvl++
			val industryTradeExponent = industry.getIndustryPower().log(5_000, 6)
			TWO.pow(lvl) * TWO.pow(industryTradeExponent.toInt()) * TWO.pow(supremeTradeLvl) * industry.tradeMultiplier
		}
		.fold(BigDecimal.ZERO) { acc, p -> acc + p }

	return boostedTradePower.divide(currentTradePower, 2, RoundingMode.HALF_UP).stripTrailingZeros()
}

private fun getUpgradeCardCost(researcher: Researcher): BigDecimal {
	val availableCards = researcher.getAvailableCards()
	return if (availableCards != null) {
		val requiredCards = getRequiredCardsForUpgrade(researcher)
		val missingCards = if (requiredCards <= availableCards) BigDecimal.ZERO else (requiredCards - availableCards)
		getCardCosts(researcher) * missingCards
	} else BigDecimal.ZERO
}

private fun getBoostPer1kScience(upgradeScienceCost: BigDecimal, industryBoost: BigDecimal): BigDecimal {
	return if (upgradeScienceCost > BigDecimal.ZERO) industryBoost.divide(
		upgradeScienceCost.divide(BigDecimal(1000), 6, RoundingMode.HALF_UP), 3, RoundingMode.HALF_UP
	).stripTrailingZeros() else BigDecimal.ZERO
}

private fun getUpgradeCost(researcher: Researcher) = when (researcher.rarity) {
	Common -> when (researcher.getLevel()) {
		0 -> 0
		1 -> 50
		2 -> 100
		3 -> 200
		4 -> 300
		5 -> 400
		6 -> 1_000
		7 -> 2_000
		8 -> 4_000
		9 -> 8_000
		10 -> 20_000
		11 -> 50_000
		12 -> 100_000
		13 -> 0
		else -> throw Exception("Invalid researcher level: ${researcher.getLevel()}")
	}
	Rare -> when (researcher.getLevel()) {
		0 -> 0
		1 -> 100
		2 -> 200
		3 -> 400
		4 -> 1_000
		5 -> 2_000
		6 -> 4_000
		7 -> 8_000
		8 -> 20_000
		9 -> 50_000
		10 -> 100_000
		11 -> 0
		else -> throw Exception("Invalid researcher level: ${researcher.getLevel()}")
	}
	Epic -> when (researcher.getLevel()) {
		0 -> 0
		1 -> 400
		2 -> 2_000
		3 -> 4_000
		4 -> 8_000
		5 -> 20_000
		6 -> 50_000
		7 -> 100_000
		8 -> 0
		else -> throw Exception("Invalid researcher level: ${researcher.getLevel()}")
	}
	Supreme -> when (researcher.getLevel()) {
		0 -> 0
		1 -> 5_000
		2 -> 20_000
		3 -> 50_000
		4 -> 100_000
		5 -> 0
		else -> throw Exception("Invalid researcher level: ${researcher.getLevel()}")
	}
}.let { BigDecimal(it) }

private fun getRequiredCardsForUpgrade(researcher: Researcher) = when (researcher.getLevel()) {
	0 -> BigDecimal(1)
	1 -> BigDecimal(2)
	2 -> BigDecimal(5)
	3 -> BigDecimal(10)
	4 -> BigDecimal(20)
	5 -> BigDecimal(50)
	6 -> BigDecimal(100)
	7 -> BigDecimal(200)
	8 -> BigDecimal(400)
	9 -> BigDecimal(800)
	10 -> BigDecimal(1_000)
	11 -> BigDecimal(2_000)
	12 -> BigDecimal(5_000)
	else -> BigDecimal.ZERO
}

private fun getCardCosts(researcher: Researcher) = when (researcher.rarity) {
	Common -> BigDecimal(10)
	Rare -> BigDecimal(100)
	Epic -> BigDecimal(1_000)
	Supreme -> BigDecimal(40_000)
}

private fun Industry.getDiscountFactorForIndustry() =
	Researcher.researcherLevels.filter {
		it.key.modifier == Discount && (it.key.industry == this || it.key.industry == All)
	}
		.map { it.value }
		.filterNotNull()
		.filter { it > 0 }
		.fold(BigDecimal(1)) { acc, lvl -> acc.multiply(getDiscountFactorFromLvl(lvl)) }

private fun Industry.getCommonCountForIndustry() =
	Researcher.researcherLevels.filter {
		val value = it.value // required for smart cast (not so smart, huh!)
		it.key.rarity == Common && (it.key.industry == this || this == All) && value != null && value > 0
	}.size.toBigDecimal()

private fun Industry.getBonusMultiplierForIndustry() =
	Researcher.researcherLevels.filter {
		it.key.modifier == Bonus && (it.key.industry == this || it.key.industry == All)
	}
		.map { it.value }
		.filterNotNull()
		.filter { it > 0 }
		.fold(BigDecimal(2)) { acc, lvl -> acc.multiply(BigDecimal(4).pow(lvl)) }

private fun Industry.getChanceForIndustry(simulatePlusOneLvl: Boolean = false) =
	Researcher.researcherLevels.filter {
		it.key.modifier == Chance && (it.key.industry == this || it.key.industry == All)
	}
		.mapNotNull {
			val lvl = if (simulatePlusOneLvl && (it.key.industry != All || this == All)) (it.value
				?: 0) + 1 else it.value

			val chance = if (lvl != null) getChanceFromLvl(lvl, it.key.rarity == Supreme) else null
			chance
		}
		.fold(BigDecimal("0.01")) { acc, chance -> acc + chance }

private fun getChanceFromLvl(chanceLvl: Int, isSupreme: Boolean): BigDecimal = when (isSupreme) {
	true -> when (chanceLvl) {
		1 -> BigDecimal("0.08")
		2 -> BigDecimal("0.13")
		3 -> BigDecimal("0.19")
		4 -> BigDecimal("0.26")
		5 -> BigDecimal("0.34")
		else -> BigDecimal.ZERO
	}
	false -> when (chanceLvl) {
		1 -> BigDecimal("0.05")
		2 -> BigDecimal("0.0725")
		3 -> BigDecimal("0.1")
		4 -> BigDecimal("0.1325")
		5 -> BigDecimal("0.17")
		6 -> BigDecimal("0.2125")
		7 -> BigDecimal("0.26")
		8 -> BigDecimal("0.3125")
		9 -> BigDecimal("0.37")
		10 -> BigDecimal("0.4325")
		11 -> BigDecimal("0.5")
		else -> BigDecimal.ZERO
	}
}

private fun getDiscountFactorFromLvl(discountLvl: Int) = BigDecimal(10).pow(discountLvl)

private fun BigDecimal.log(base: Int, scale: Int): BigDecimal =
	kotlin.math.log(toDouble(), base.toDouble()).toBigDecimal().setScale(scale, RoundingMode.HALF_UP)
		.stripTrailingZeros()
