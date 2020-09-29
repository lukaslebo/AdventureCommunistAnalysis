import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap

const val gameStateFileName = "game-state.properties"
const val devPath = "src/main/resources/"

private val LOG = KotlinLogging.logger {}
private val props: Properties = Properties()

fun loadGameState(devMode: Boolean = false): Boolean {
	try {
		val gameStateProps = File((if (devMode) devPath else "") + gameStateFileName)
		if (!gameStateProps.exists()) {
			return false
		}
		val fis = FileInputStream(gameStateProps)
		props.load(fis)
		LOG.info { "Loaded game state from \"$gameStateFileName\"" }
		return true
	} catch (e: Throwable) {
		LOG.error(e) { "Unable to load \"$gameStateFileName\"" }
		return false
	}
}

fun getResearcherLevels(): Map<Researcher, Int?> {
	val researcherLevels = HashMap<Researcher, Int?>()
	Researcher.values().forEach {
		val lvl = props.getProperty(it.name)?.toIntOrNull()
		researcherLevels[it] = lvl
	}
	return researcherLevels
}

fun getAvailableCards(): Map<Researcher, Int?> {
	val availableCards = HashMap<Researcher, Int?>()
	Researcher.values().forEach {
		val cards = props.getProperty("AvailableCards.${it.name}")?.toIntOrNull()
		availableCards[it] = cards
	}
	return availableCards
}
