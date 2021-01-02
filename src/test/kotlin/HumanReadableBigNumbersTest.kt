import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class HumanReadableBigNumbersTest {

	@Test
	fun `toHumanReadable should abbreviate large numbers with correct units`() {
		verifyHumanReadableNumber("12", "12")
		verifyHumanReadableNumber("123", "123")
		verifyHumanReadableNumber("1.234K", "1234")
		verifyHumanReadableNumber("12.345K", "12345")
		verifyHumanReadableNumber("123.456K", "123456")
		verifyHumanReadableNumber("1.235M", "1234567")
		verifyHumanReadableNumber("1.234M", "1234467")

		verifyHumanReadableNumber("1.234M", "1_234_012")
		verifyHumanReadableNumber("1.234B", "1_234_012_345")
		verifyHumanReadableNumber("1.234T", "1_234_012_345_678")
		verifyHumanReadableNumber("1.234AA", "1_234_012_345_678_901")
		verifyHumanReadableNumber("1.234BB", "1_234_012_345_678_901_234")
		verifyHumanReadableNumber("1.234CC", "1_234_012_345_678_901_234_567")

		verifyHumanReadableNumber("123.4CC", "123_400_123_456_789_012_345_678")
	}

	@Test
	fun `fromHumanReadable should parse abbreviated number to BigDecimal`() {
		verifyBigDecimalFromHumanReadable("123", "123")
		verifyBigDecimalFromHumanReadable("123_050", "123.05K")
		verifyBigDecimalFromHumanReadable("73_182_000_000_000_000", "73.182AA")
	}

	private fun verifyBigDecimalFromHumanReadable(expected: String, testNum: String) {
		assertEquals(expected.replace("_", ""), fromHumanReadable(testNum.replace("_", "")).toPlainString())
	}

	private fun verifyHumanReadableNumber(expected: String, testNum: String) {
		assertEquals(expected, BigDecimal(testNum.replace("_", "")).toHumanReadable())
	}
}
