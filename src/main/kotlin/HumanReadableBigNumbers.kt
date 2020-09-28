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

private val unitsMapping = getUnitMappings()

private fun getUnitMappings(): List<String> {
	val abc = (65..90).map { it.toChar().toString() }
	val mapping = mutableListOf("", "K", "M", "B", "T")
	(2..5).forEach { r -> abc.forEach { char -> mapping.add(char.repeat(r)) } }
	return mapping
}
