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
    }

    fun length(): Quantity<Distance> = this.sectors.fold(0 * Kilometer) { acc, sector -> acc + sector.length }
}