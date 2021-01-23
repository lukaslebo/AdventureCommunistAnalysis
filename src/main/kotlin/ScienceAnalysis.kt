import Rarity.*
import java.math.BigDecimal

// 23.01.2021
// Science required = 1.298M
// Science required for max = 34.435M
// Science required for max without supremes = 27.265M
// Science spent in upgrades = 525.9K
// Science spent with cards cost = 5.172M

fun main() {
	loadGameState(true)
	var requiredScience = BigDecimal.ZERO
	var requiredScienceForMax = BigDecimal.ZERO
	var requiredScienceForMaxWithoutSupremes = BigDecimal.ZERO
	var scienceSpent = BigDecimal.ZERO
	var scienceSpentWithCardsCost = BigDecimal.ZERO
	for (researcher in Researcher.values()) {
		val lvl = researcher.getLevel()
		val cards = researcher.getAvailableCards()
		val rarity = researcher.rarity
		requiredScience += getScienceToSpend(lvl, cards, rarity)
		val scienceUntilMax = getScienceToSpendUntilMaxLvl(lvl, cards, rarity)
		requiredScienceForMax += scienceUntilMax
		requiredScienceForMaxWithoutSupremes += if (rarity == Supreme) BigDecimal.ZERO else scienceUntilMax

		scienceSpent += getScieneSpent(lvl, rarity)
		scienceSpentWithCardsCost += getScieneSpentWithCardCosts(lvl, cards, rarity)
	}
	println("Science required = ${requiredScience.toHumanReadable()}")
	println("Science required for max = ${requiredScienceForMax.toHumanReadable()}")
	println("Science required for max without supremes = ${requiredScienceForMaxWithoutSupremes.toHumanReadable()}")
	println()
	println("Science spent in upgrades = ${scienceSpent.toHumanReadable()}")
	println("Science spent with cards cost = ${scienceSpentWithCardsCost.toHumanReadable()}")
}

fun getScieneSpent(lvl: Int, rarity: Rarity): BigDecimal {
	var science = BigDecimal.ZERO
	var currentLvl = 0
	while (currentLvl < lvl) {
		science += getUpgradeCost(rarity, currentLvl)
		currentLvl++
	}
	return science
}

fun getScieneSpentWithCardCosts(lvl: Int, cards: BigDecimal?, rarity: Rarity): BigDecimal {
	var science = BigDecimal.ZERO
	var totalCards = cards ?: BigDecimal.ZERO
	val cardsCost = getCardCosts(rarity)
	var currentLvl = 0
	while (currentLvl < lvl) {
		science += getUpgradeCost(rarity, currentLvl)
		totalCards += getRequiredCardsForUpgrade(currentLvl)
		currentLvl++
	}
	return science + cardsCost * totalCards
}

fun getScienceToSpend(lvl: Int, cards: BigDecimal?, rarity: Rarity): BigDecimal {
	val maxLvl = getMaxLvl(rarity)
	if (lvl in 1 until maxLvl && cards != null && cards > BigDecimal.ZERO) {
		var science = BigDecimal.ZERO
		var currentLvl = lvl
		var requiredCards = getRequiredCardsForUpgrade(currentLvl)
		var remainingCards: BigDecimal = cards
		while (remainingCards >= requiredCards) {
			science += getUpgradeCost(rarity, currentLvl)
			currentLvl++
			remainingCards -= requiredCards
			requiredCards = getRequiredCardsForUpgrade(currentLvl)
		}
		return science
	}
	return BigDecimal.ZERO
}

fun getScienceToSpendUntilMaxLvl(lvl: Int, cards: BigDecimal?, rarity: Rarity): BigDecimal {
	var science = BigDecimal.ZERO
	var currentLvl = lvl
	val maxLvl = getMaxLvl(rarity)
	var remainingCards: BigDecimal = cards ?: BigDecimal.ZERO
	val cardCost = getCardCosts(rarity)
	while (currentLvl < maxLvl) {
		val requiredCards = getRequiredCardsForUpgrade(currentLvl)
		val upgradeCost = getUpgradeCost(rarity, currentLvl)
		science += upgradeCost
		if (remainingCards >= requiredCards) {
			remainingCards -= remainingCards
		} else {
			science += (requiredCards - remainingCards) * cardCost
			remainingCards = BigDecimal.ZERO
		}
		currentLvl++
	}
	return science
}

private fun getUpgradeCost(rarity: Rarity, lvl: Int) = when (rarity) {
	Common -> when (lvl) {
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
		else -> throw Exception("Invalid researcher level: $lvl")
	}
	Rare -> when (lvl) {
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
		else -> throw Exception("Invalid researcher level: $lvl")
	}
	Epic -> when (lvl) {
		0 -> 0
		1 -> 400
		2 -> 2_000
		3 -> 4_000
		4 -> 8_000
		5 -> 20_000
		6 -> 50_000
		7 -> 100_000
		8 -> 0
		else -> throw Exception("Invalid researcher level: $lvl")
	}
	Supreme -> when (lvl) {
		0 -> 0
		1 -> 5_000
		2 -> 20_000
		3 -> 50_000
		4 -> 100_000
		5 -> 0
		else -> throw Exception("Invalid researcher level: $lvl")
	}
}.let { BigDecimal(it) }

private fun getRequiredCardsForUpgrade(lvl: Int) = when (lvl) {
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

private fun getCardCosts(rarity: Rarity) = when (rarity) {
	Common -> BigDecimal(10)
	Rare -> BigDecimal(100)
	Epic -> BigDecimal(1_000)
	Supreme -> BigDecimal(40_000)
}

private fun getMaxLvl(rarity: Rarity) = when (rarity) {
	Common -> 13
	Rare -> 11
	Epic -> 8
	Supreme -> 5
}
