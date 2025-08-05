import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import units.Distance
import units.Distance.Companion.Kilometer
import units.Distance.Companion.Millimeter
import units.Quantity

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

        @Test
        @DisplayName("A car cannot be placed in a kilometer greater than the sector's length")
        fun testACarCanNotBePlacedInAKilometerGreaterThanSectorLength() {
            val sector = Sector.turboSectorOf(10 * Kilometer)
            val outside = sector.length + 1 * Millimeter
            val car = schumacherCar()

            val exception = assertThrows(Error::class.java) {
                sector.placeAt(car, outside)
            }

            assertEquals(Sector.carCannotBePlacedOutsideErrorDescription(), exception.message)
        }
    }

    fun schumacherCar(): FormulaOneCar = FormulaOneCar.drivenBy(Schumacher)
}