import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import units.Distance.Companion.Kilometer
import units.Distance.Companion.Meter
import units.Distance.Companion.Millimeter
import units.Time.Companion.Hour
import units.div

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

    @Test
    @DisplayName("A car can activate turbo when 50 meters behind another car")
    fun testACarCanActivateTurboWhenOneSecondBehindAnotherCar() {
        val sector = Sector.turboSectorOf(10 * Kilometer)
        val schumacher = schumacherCar()
        val hamilton = hamiltonCar()

        schumacher.speed(300 * (Kilometer / Hour))
        sector.placeAt(schumacher, 2 * Kilometer)

        hamilton.speed(300 * (Kilometer / Hour))
        sector.placeAt(hamilton, 2 * Kilometer + 50 * Meter)

        schumacher.activateTurbo()

        assert(schumacher.isTurboActivated())
    }

    @Test
    @DisplayName("A car cannot activate turbo when more than 50 meters behind another car")
    fun testACarCanActivateTurboWhenMoreThanOneSecondBehindAnotherCar() {
        val sector = Sector.turboSectorOf(10 * Kilometer)
        val schumacher = schumacherCar()
        val hamilton = hamiltonCar()

        schumacher.speed(300 * (Kilometer / Hour))
        sector.placeAt(schumacher, 2 * Kilometer)

        hamilton.speed(300 * (Kilometer / Hour))
        sector.placeAt(hamilton, 2 * Kilometer + 50 * Meter + 1 * Millimeter)

        val exception = assertThrows(java.lang.IllegalStateException::class.java) {
            schumacher.activateTurbo()
        }

        assertEquals(Sector.turboCarAheadFarAwayErrorDescription(), exception.message)
        assertFalse { schumacher.isTurboActivated() }
    }

    @Test
    @DisplayName("A track must have sectors")
    fun testATrackMustHaveSectors() {
        val exception = assertThrows(java.lang.IllegalArgumentException::class.java) {
            Track.with(listOf<Sector>())
        }

        assertEquals(Track.noSectorsErrorDescription(), exception.message)
    }

    @Test
    @DisplayName("The length of the track is the sum of its sectors")
    fun testTrackLengthShouldBeSumOfItsSectors() {
        val track = Track.with(listOf<Sector>(
            Sector.turboSectorOf(10 * Kilometer),
            Sector.noTurboSectorOf(2 * Kilometer),
            Sector.turboSectorOf(30 * Kilometer)
        ))

        assertEquals(42 * Kilometer, track.length())
    }

    @Test
    @DisplayName("A car cannot be placed in a kilometer greater than the track's length")
    fun testACarCanNotBePlacedInAKilometerGreaterThanTrackLength() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val outside = track.length() + 1 * Millimeter
        val car = schumacherCar()

        val exception = assertThrows(IllegalStateException::class.java) {
            track.placeAt(car, outside)
        }

        assertEquals(Track.carCannotBePlacedOutsideErrorDescription(), exception.message)
    }

    @Test
    @DisplayName("A car should be placed in track's sector")
    fun testACarShouldBePlacedInTrackSector() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val car = schumacherCar()

        val carPosition = (sector1.length + sector2.length) * 2
        track.placeAt(car, carPosition)

        assertEquals(sector3, track.sectorOf(car))
        assertEquals(carPosition, track.locationOf(car))

        assertFalse(sector1.contains(car))
        assertFalse(sector2.contains(car))
        assert(sector3.contains(car))
    }

    @Test
    @DisplayName("Track should know car is running")
    fun testTrackShouldKnowCars() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val schumacher = schumacherCar()
        track.placeAt(schumacher, 4 * Kilometer)

        val hamilton = hamiltonCar()
        track.placeAt(hamilton, 11 * Kilometer)

        val verstappen = verstappenCar()
        track.placeAt(verstappen, 15 * Kilometer)

        assert(track.contains(schumacher))
        assert(track.contains(hamilton))
        assert(track.contains(verstappen))

        assert(sector1.contains(schumacher))
        assert(sector2.contains(hamilton))
        assert(sector3.contains(verstappen))
    }

    @Test
    @DisplayName("Car in Track cannot be in more than one sector")
    fun testCarCannotBeInMoreThanASectorInTrack() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val schumacher = schumacherCar()
        track.placeAt(schumacher, 1 * Kilometer)
        track.placeAt(schumacher, track.length() - 1 * Kilometer)

        assertEquals(sector3, track.sectorOf(schumacher))

        assertFalse(sector1.contains(schumacher))
        assertFalse(sector2.contains(schumacher))
        assert(sector3.contains(schumacher))
    }

    @Test
    @DisplayName("Two cars can be at the same position")
    fun testTwoCarsCanBeAtTheSamePosition() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val schumacher = schumacherCar()
        track.placeAt(schumacher, 24 * Kilometer)

        val hamilton = hamiltonCar()
        track.placeAt(hamilton, 24 * Kilometer)

        assertEquals(24 * Kilometer, track.locationOf(schumacher))
        assertEquals(24 * Kilometer, track.locationOf(hamilton))
    }

    @Test
    @DisplayName("Grand Prix length should be the track length times number of laps")
    fun testGrandPrixLengthShouldBeTrackLengthTimesLapsCount() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val lapsCount = 100

        val grandPrix = GrandPrix.start(
            track = track,
            lapsCount = lapsCount,
            cars = listOf<FormulaOneCar>()
        )

        assertEquals(track.length() * lapsCount, grandPrix.length())
    }

    @Test
    @DisplayName("Two cars can be at the same position from the start of the GrandPrix")
    fun testTwoCarsCanBeAtTheSamePositionFromTheStart() {
        val sector1 = Sector.turboSectorOf(10 * Kilometer)
        val sector2 = Sector.noTurboSectorOf(2 * Kilometer)
        val sector3 = Sector.noTurboSectorOf(30 * Kilometer)
        val track = Track.with(listOf<Sector>(sector1, sector2, sector3))

        val lapsCount = 100

        val schumacher = schumacherCar()
        val hamilton = hamiltonCar()

        val grandPrix = GrandPrix.start(
            track = track,
            lapsCount = lapsCount,
            cars = listOf<FormulaOneCar>(schumacher, hamilton)
        )

        assertEquals(0 * Kilometer, grandPrix.locationOf(schumacher))
        assertEquals(0 * Kilometer, grandPrix.locationOf(hamilton))
    }

    fun schumacherCar(): FormulaOneCar = FormulaOneCar.drivenBy(Schumacher)
    fun hamiltonCar(): FormulaOneCar = FormulaOneCar.drivenBy(Hamilton)
    fun verstappenCar(): FormulaOneCar = FormulaOneCar.drivenBy(Verstappen)

}