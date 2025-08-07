import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.times

class Track(val sectors: List<Sector>) {
    companion object {
        fun with(sectors: List<Sector>): Track {
            require(sectors.isNotEmpty()) { noSectorsErrorDescription() }

            return Track(sectors)
        }

        fun noSectorsErrorDescription() = "Track must have sectors"
        fun carCannotBePlacedOutsideErrorDescription(): String = "Cannot place the car outside the sector"
        fun carNotFoundErrorDescription(): String = "Car not found in track"
    }

    fun length(): Quantity<Distance> = this.sectors.fold(0 * Kilometer) { acc, sector -> acc + sector.length }

    fun placeAt(car: FormulaOneCar, position: Quantity<Distance>) {
        check(position <= this.length()) { carCannotBePlacedOutsideErrorDescription() }

        this.remove(car)

        var acc = 0 * Kilometer
        this.sectors.forEach {
            // Kotlin's blocks are full closure!
            if (position <= acc + it.length) return it.placeAt(car, position - acc)
            acc += it.length
        }

        // The following also works, but only exits block using return with a label
        // this.sectors.forEach eachblock@{
        //     if (position <= acc + it.length) return@eachblock it.placeAt(car, position - acc)
        //     acc += it.length
        // }
    }

    fun sectorOf(car: FormulaOneCar): Sector {
        return sectorOfIfNone(car) { throw IllegalStateException(carNotFoundErrorDescription()) }
    }

    fun positionOf(car: FormulaOneCar): Quantity<Distance> {
        var acc = 0 * Kilometer

        this.sectors.forEach {
            if (it.contains(car)) return acc + it.positionOf(car)
            acc += it.length
        }

        throw IllegalStateException(carNotFoundErrorDescription())
    }

    fun contains(car: FormulaOneCar): Boolean = this.sectors.any { it.contains(car) }

    private inline fun sectorOfIfNone(car: FormulaOneCar, ifNoneBlock: () -> Unit): Sector {
        val sector = this.sectors.firstOrNull { it.contains(car) }
        if (sector == null) ifNoneBlock()

        return sector!!
    }

    private fun remove(car: FormulaOneCar) {
        val sector = this.sectorOfIfNone(car) { return }
        sector.remove(car)
    }
}