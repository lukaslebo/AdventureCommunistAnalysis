import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap

const val gameStateFileName = "game-state.properties"
const val devPath = "src/main/resources/"

private val LOG = KotlinLogging.logger {}
private val props: Properties = Properties()
private val researcherLevels = HashMap<Researcher, Int?>()
private val availableCards = HashMap<Researcher, Int?>()

fun loadGameState(devMode: Boolean = false): Boolean {
	try {
		val gameStateProps = File((if (devMode) devPath else "") + gameStateFileName)
		if (!gameStateProps.exists()) {
			return false
		}
		val fis = FileInputStream(gameStateProps)
		props.load(fis)
		parseProps()
		LOG.info { "Loaded game state from \"$gameStateFileName\"" }
		return true
	} catch (e: Throwable) {
		LOG.error(e) { "Unable to load \"$gameStateFileName\"" }
		return false
	}
}

fun parseProps() {
	Researcher.values().forEach {
		val config = props.getProperty(it.name)
		if (config?.contains(';') == true) {
			val splits = config.split(';').map { split -> split.trim() }
			val lvl = splits.first().toIntOrNull()
			val cards = if (splits.size > 1) splits[1].toIntOrNull() else null
			researcherLevels[it] = lvl
			availableCards[it] = cards
		} else {
			val lvl = props.getProperty(it.name)?.toIntOrNull()
			researcherLevels[it] = lvl
		}
	}

}

fun getResearcherLevels(): Map<Researcher, Int?> {
	return researcherLevels
}

fun getAvailableCards(): Map<Researcher, Int?> {
	return availableCards
}
