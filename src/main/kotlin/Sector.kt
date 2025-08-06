import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.times

abstract class Sector(val length: Quantity<Distance>) {
    companion object {
        fun turboSectorOf(distance: Quantity<Distance>): Sector {
            requirePositiveLength(distance)
            return TurboSector(distance)
        }

        fun noTurboSectorOf(distance: Quantity<Distance>): Sector {
            requirePositiveLength(distance)
            return NoTurboSector(distance)
        }

        private fun requirePositiveLength(distance: Quantity<Distance>) {
            require(distance > 0 * Kilometer) { invalidLengthErrorDescription() }
        }

        fun invalidLengthErrorDescription(): String = "Invalid Sector's length. It should be positive"
        fun carCannotBePlacedOutsideErrorDescription(): String = "Cannot place the car outside the sector"
        fun turboNotAllowedErrorDescription(): String = "Turbo is not allowed in this sector"
    }

    private val cars: MutableMap<FormulaOneCar, Quantity<Distance>> = mutableMapOf<FormulaOneCar, Quantity<Distance>>()

    fun placeAt(car: FormulaOneCar, position: Quantity<Distance>) {
        check(position <= this.length) { carCannotBePlacedOutsideErrorDescription() }

        this.cars[car] = position
        car.atSector(this)
    }

    abstract fun carActivatingTurbo(car: FormulaOneCar)
}

class TurboSector(distance: Quantity<Distance>): Sector(distance) {
    override fun carActivatingTurbo(car: FormulaOneCar) {
        car.activateTurboInTurboSector()
    }
}

class NoTurboSector(distance: Quantity<Distance>): Sector(distance) {
    override fun carActivatingTurbo(car: FormulaOneCar) {
        throw IllegalStateException(turboNotAllowedErrorDescription())
    }
}
