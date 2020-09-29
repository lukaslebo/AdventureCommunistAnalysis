import mu.KotlinLogging
import java.io.File
import java.math.BigDecimal
import kotlin.system.exitProcess

private val LOG = KotlinLogging.logger {}

fun main(args: Array<String>) {
	val devMode = args.contains("dev")
	val success = loadGameState(devMode)

	val sb = StringBuilder()
	if (success) {
		Industry.values().forEach { sb.append(analyzeIndustry(it)) }
		sb.append(analyzeTradeResearchers())
		LOG.info { sb }
	} else {
		LOG.error { "Could not load game state from \"$gameStateFileName\"" }
		exitProcess(1)
	}

	if (!devMode) {
		val outFile = File("analysis.txt")
		outFile.printWriter().apply { print(sb.toString()) }.flush()
	}
}

fun analyzeIndustry(industry: Industry): StringBuilder {
	if (industry == Industry.All) {
		return analyzeSupremes()
	}

	val sb = StringBuilder("\n\n")
	val title = "$industry Analysis"
	sb.appendLine("".padEnd(8 + title.length, '#'))
	sb.appendLine("### $title ###")
	sb.appendLine("".padEnd(8 + title.length, '#'))
	sb.appendLine("Industry Power: ${industry.getIndustryPower().toHumanReadable()}")

	Researcher.getAllByIndustry(industry)
		.filter { it.modifier != Modifier.Trade }
		.map { it.analyze() as PowerAnalysis }
		.filter { it.canUpgrade }
		.sortedByDescending { it.boostPer1kScience }
		.forEach {
			val lineSb = StringBuilder()
			lineSb.append("\n${it.researcher}".padEnd(41))
			val upgradeCostFormatted =
				"${it.upgradeCost.toHumanReadable()}" +
						if (it.upgradeCardCost > BigDecimal.ZERO) " + ${it.upgradeCardCost.toHumanReadable()}"
						else ""
			lineSb.append("Upgrade Cost: $upgradeCostFormatted".padEnd(25))
			lineSb.append("Boost: ${it.industryBoost.toPlainString()}".padEnd(15))
			lineSb.append("Boost per 1k Science: ${it.boostPer1kScience}")
			sb.append(lineSb)
		}

	return sb
}

fun analyzeSupremes(): StringBuilder {
	val sb = StringBuilder("\n\n")
	val title = "Supreme Analysis"
	sb.appendLine("".padEnd(8 + title.length, '#'))
	sb.appendLine("### $title ###")
	sb.appendLine("".padEnd(8 + title.length, '#'))

	Researcher.getAllByIndustry(Industry.All)
		.filter { it.modifier != Modifier.Trade }
		.map { it.analyze() as PowerAnalysis }
		.sortedByDescending { it.boostPer1kScience }
		.forEach {
			val lineSb = StringBuilder()
			lineSb.append("\n${it.researcher}".padEnd(41))
			val upgradeCostFormatted =
				"${it.upgradeCost.toHumanReadable()}" +
						if (it.upgradeCardCost > BigDecimal.ZERO) " + ${it.upgradeCardCost.toHumanReadable()}"
						else ""
			lineSb.append("Upgrade Cost: $upgradeCostFormatted".padEnd(25))
			lineSb.append("Boost: ${it.industryBoost.toPlainString()}".padEnd(15))
			lineSb.append("Boost per 1k Science: ${it.boostPer1kScience}")
			sb.append(lineSb)
		}

	return sb
}

fun analyzeTradeResearchers(): StringBuilder {
	val sb = StringBuilder("\n\n")
	val title = "Comrade Trades Analysis"
	sb.appendLine("".padEnd(8 + title.length, '#'))
	sb.appendLine("### $title ###")
	sb.appendLine("".padEnd(8 + title.length, '#'))

	Researcher.values()
		.filter { it.modifier == Modifier.Trade }
		.map { it.analyze() as TradeAnalysis }
		.filter { it.canUpgrade }
		.sortedByDescending { it.boostPer1kScience }
		.forEach {
			val lineSb = StringBuilder()
			lineSb.append("\n${it.researcher}".padEnd(41))
			val upgradeCostFormatted =
				"${it.upgradeCost.toHumanReadable()}" +
						if (it.upgradeCardCost > BigDecimal.ZERO) " + ${it.upgradeCardCost.toHumanReadable()}"
						else ""
			lineSb.append("Upgrade Cost: $upgradeCostFormatted".padEnd(25))
			lineSb.append("Trade Boost: ${it.tradeBoost.toPlainString()}".padEnd(23))
			lineSb.append("Boost per 1k Science: ${it.boostPer1kScience.toPlainString()}")
			sb.append(lineSb)
		}

	return sb
}

