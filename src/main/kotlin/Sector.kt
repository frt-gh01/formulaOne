import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.times

class Sector(val length: Quantity<Distance>) {
    companion object {
        fun turboSectorOf(distance: Quantity<Distance>): Sector {
            if (distance <= 0 * Kilometer) throw Error(invalidLengthErrorDescription())

            return Sector(distance)
        }

        fun invalidLengthErrorDescription(): String = "Invalid Sector's length. It should be positive"
        fun carCannotBePlacedOutsideErrorDescription(): String = "Cannot place the car outside the sector"
    }

    private val cars: MutableMap<FormulaOneCar, Quantity<Distance>> = mutableMapOf<FormulaOneCar, Quantity<Distance>>()

    fun placeAt(car: FormulaOneCar, position: Quantity<Distance>) {
        if (position > this.length) throw Error(carCannotBePlacedOutsideErrorDescription())

        this.cars[car] = position
    }
}