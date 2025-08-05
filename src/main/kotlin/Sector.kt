import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.times

class Sector(val distance: Quantity<Distance>) {
    companion object {
        fun turboSectorOf(distance: Quantity<Distance>): Sector {
            if (distance <= 0 * Kilometer) {
                throw Error(invalidLengthErrorDescription())
            }
            return Sector(distance)
        }

        fun invalidLengthErrorDescription(): String = "Invalid Sector's length. It should be positive"
    }
}