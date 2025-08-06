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
import kotlin.test.assertFalse

class FormulaOneTest {
    @Test
    @DisplayName("Sector's length must be positive")
    fun testSectorLengthMustBePositive() {
        var exception = assertThrows(IllegalArgumentException::class.java) {
            Sector.turboSectorOf(0 * Kilometer)
        }

        assertEquals(Sector.invalidLengthErrorDescription(), exception.message)

        exception = assertThrows(IllegalArgumentException::class.java) {
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

        val exception = assertThrows(IllegalStateException::class.java) {
            sector.placeAt(car, outside)
        }

        assertEquals(Sector.carCannotBePlacedOutsideErrorDescription(), exception.message)
    }

    @Test
    @DisplayName("A car cannot activate turbo in non turbo sector")
    fun testACarCanNotActivateTurboInNonTurboSector() {
        val sector = Sector.noTurboSectorOf(10 * Kilometer)
        val car = schumacherCar()

        sector.placeAt(car, 2 * Kilometer)

        val exception = assertThrows(java.lang.IllegalStateException::class.java) {
            car.activateTurbo()
        }

        assertEquals(Sector.turboNotAllowedErrorDescription(), exception.message)
        assertFalse { car.isTurboActivated() }
    }

    @Test
    @DisplayName("A car cannot activate turbo when no car ahead")
    fun testACarCanNotActivateTurboWhenNoCarAhead() {
        val sector = Sector.turboSectorOf(10 * Kilometer)
        val schumacher = schumacherCar()
        val hamilton = hamiltonCar()

        sector.placeAt(schumacher, 2 * Kilometer)
        sector.placeAt(hamilton, 4 * Kilometer)

        val exception = assertThrows(java.lang.IllegalStateException::class.java) {
            hamilton.activateTurbo()
        }

        assertEquals(Sector.turboNoCarAheadErrorDescription(), exception.message)
    }

    fun schumacherCar(): FormulaOneCar = FormulaOneCar.drivenBy(Schumacher)
    fun hamiltonCar(): FormulaOneCar = FormulaOneCar.drivenBy(Hamilton)
}