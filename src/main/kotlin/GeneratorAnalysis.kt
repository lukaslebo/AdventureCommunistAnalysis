import Generator.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// val resource = Resource.Potatoes
// val targetResourceAmount = fromHumanReadable("3.7 OOO")
// val generatorAmounts = listOf(
// 	"3.36 III",
// 	"14.95 DDD",
// 	"106.07 YY",
// 	"1.25 UU",
// 	"25.42 PP",
// 	"903.57 KK",
// 	"56.52 GG",
// 	"6.12 CC",
// 	"26.00 B",
// )

val resource = Resource.Land
val targetResourceAmount = fromHumanReadable("7.3 MMM")
val generatorAmounts = listOf(
	"1.09 GGG",
	"2.73 BBB",
	"11.23 WW",
	"77.17 RR",
	"903.02 MM",
	"18.05 II",
	"583.85 DD",
	"911.77 B",
	"52",
)

@ExperimentalTime
fun main() {
	val executionDuration = measureTime {
		loadGameState(true)
		initGenerators()
		setGeneratorAmounts(resource, generatorAmounts)
		val resources = ResourceAmount(resource)
		val generators = getAllActiveGenerators(resource, generatorAmounts.size)
		val deltaT = BigDecimal(1)
		var time = deltaT
		while (resources <= targetResourceAmount) {
			incrementGenerators(generators, time, deltaT, resources)
			time += deltaT
		}
		generators.forEach { println("${it.name} ${it.amount?.toHumanReadable()}") }
		println("Resource: ${resources.toHumanReadable()}")
		println("Time: ${time.toHumanReadableTime()}")
	}
	println("Execution took $executionDuration")
}

private fun BigDecimal.toHumanReadableTime(): String {
	val sixty = BigDecimal(60)
	val seconds = remainder(sixty).toPlainString().padStart(2, '0')
	val minutes = divide(sixty, 0, RoundingMode.DOWN).remainder(sixty).toPlainString().padStart(2, '0')
	val hours = divide(BigDecimal(3600), 0, RoundingMode.DOWN).toPlainString().padStart(2, '0')
	return "$hours:$minutes:$seconds"
}

fun incrementGenerators(generators: List<Generator>, time: BigDecimal, deltaT: BigDecimal, resources: ResourceAmount) {
	generators.forEach {
		if (it.produces is Resource) {
			resources += it.averageOutputPerSecond?.times(it.amount)?.times(deltaT) ?: BigDecimal.ZERO
		} else if (it.produces is Generator) {
			it.produces.amount += it.averageOutputPerSecond?.times(it.amount)?.times(deltaT) ?: BigDecimal.ZERO
		}
	}
}

fun getAllActiveGenerators(resource: Resource, generatorNum: Int): List<Generator> {
	val generators = mutableListOf<Generator>()
	var gen = getFirstGeneratorForResource(resource)
	var n = generatorNum
	while (n > 0) {
		generators += gen
		val next = gen.producedBy
		if (next != null) {
			gen = next
		}
		n--
	}
	return generators.reversed()
}

fun setGeneratorAmounts(resource: Resource, amounts: List<String>) {
	var generator = getFirstGeneratorForResource(resource)
	for (amount in amounts) {
		generator.amount = fromHumanReadable(amount)

		val next = generator.producedBy
		if (next != null) {
			generator = next
		}
	}
}

fun getFirstGeneratorForResource(resource: Resource) = when (resource) {
	Resource.Potatoes -> Farmers
	Resource.Land -> Workers
	Resource.Ore -> Miner
	Resource.Bullets -> Soldier
	Resource.Placebos -> Nurse
}

class ResourceAmount(
	val resource: Resource,
) {
	private var amount = BigDecimal.ZERO

	operator fun plusAssign(add: BigDecimal) {
		amount += add
	}

	operator fun compareTo(right: BigDecimal) = amount.compareTo(right)

	fun toHumanReadable() = this.amount.toHumanReadable()
}
