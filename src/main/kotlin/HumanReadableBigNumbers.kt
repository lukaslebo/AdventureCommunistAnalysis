import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toHumanReadable(): String {
	val powerOfThree = (precision() - scale() - 1) / 3
	val num = movePointLeft(powerOfThree * 3)
		.setScale(3, RoundingMode.HALF_UP)
		.stripTrailingZeros()
		.toPlainString()
	return "$num${unitsMapping[powerOfThree]}"
}

val HUMAN_READABLE_NUMBER = Regex("(?<number>[0-9]+\\.?[0-9]*)[ ]?(?<unit>[A-Z]{0,4})")

fun fromHumanReadable(number: String): BigDecimal {
	val groups = HUMAN_READABLE_NUMBER.find(number)?.groups
	val num = groups?.get("number")?.value ?: "0"
	val unit = groups?.get("unit")?.value ?: ""
	val unitIndex = unitsMapping.indexOf(unit)
	return BigDecimal(num).movePointRight(unitIndex * 3)
}

private val unitsMapping = getUnitMappings()

private fun getUnitMappings(): List<String> {
	val abc = (65..90).map { it.toChar().toString() }
	val mapping = mutableListOf("", "K", "M", "B", "T")
	(2..5).forEach { r -> abc.forEach { char -> mapping.add(char.repeat(r)) } }
	return mapping
}
