import units.Distance
import units.Distance.Companion.Kilometer
import units.Distance.Companion.Meter
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
        fun turboNoCarAheadErrorDescription(): String = "Turbo cannot be activated because no car ahead"
        fun turboCarAheadFarAwayErrorDescription(): String = "Turbo cannot be activated because opponent car is far ahead"
    }

    protected val cars: MutableMap<FormulaOneCar, Quantity<Distance>> = mutableMapOf<FormulaOneCar, Quantity<Distance>>()

    fun placeAt(car: FormulaOneCar, position: Quantity<Distance>) {
        check(position <= this.length) { carCannotBePlacedOutsideErrorDescription() }

        this.cars[car] = position
        car.atSector(this)
    }

    fun contains(car: FormulaOneCar) = this.cars.containsKey(car)
    fun locationOf(car: FormulaOneCar) = this.cars.getValue(car)
    fun remove(car: FormulaOneCar) = this.cars.remove(car)

    abstract fun carActivatingTurbo(car: FormulaOneCar)
}

class TurboSector(distance: Quantity<Distance>): Sector(distance) {
    override fun carActivatingTurbo(car: FormulaOneCar) {
        val frontCar = carAheadIfNone(car) { throw IllegalStateException(turboNoCarAheadErrorDescription()) }
        if (distanceBetween(car, frontCar) > 50 * Meter) { throw IllegalStateException(turboCarAheadFarAwayErrorDescription()) }

        car.activateTurboInTurboSector()
    }

    private fun carAheadIfNone(car: FormulaOneCar, ifNoneBlock: () -> Unit): FormulaOneCar {
        val carPosition = this.cars.getValue(car)
        val carWithPosition: Pair<FormulaOneCar, Quantity<Distance>>? = this.cars.toList()
            .sortedBy { it.second }
            .firstOrNull { it.second > carPosition }

        if (carWithPosition == null) {
            ifNoneBlock()
        }

        return carWithPosition!!.first
    }

    private fun distanceBetween(car: FormulaOneCar, otherCar: FormulaOneCar): Quantity<Distance> {
        val carPosition = this.cars.getValue(car)
        val otherCarPosition = this.cars.getValue(otherCar)

        return (carPosition - otherCarPosition).abs()
    }
}

class NoTurboSector(distance: Quantity<Distance>): Sector(distance) {
    override fun carActivatingTurbo(car: FormulaOneCar) {
        throw IllegalStateException(turboNotAllowedErrorDescription())
    }
}
