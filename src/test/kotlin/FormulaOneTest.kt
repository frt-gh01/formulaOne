import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import units.Distance.Companion.Kilometer

import units.times
import kotlin.test.assertEquals

class FormulaOneTest {
    @Nested
    @DisplayName("Sector")
    inner class SectorTests {
        @Test
        @DisplayName("Sector's length must be positive")
        fun testSectorLengthMustBePositive() {
            var exception = assertThrows(Error::class.java) {
                Sector.turboSectorOf(0 * Kilometer)
            }

            assertEquals(Sector.invalidLengthErrorDescription(), exception.message)

            exception = assertThrows(Error::class.java) {
                Sector.turboSectorOf((-1) * Kilometer)
            }

            assertEquals(Sector.invalidLengthErrorDescription(), exception.message)
        }
    }
}