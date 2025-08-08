import units.Distance.Companion.Kilometer
import units.Quantity
import units.Time
import units.times

class GrandPrix(val track: Track, val lapsCount: Int, val cars: List<FormulaOneCar>) {
    companion object {
        fun start(track: Track, lapsCount: Int, cars: List<FormulaOneCar>): GrandPrix = GrandPrix(track, lapsCount, cars)
    }

    fun length() = this.track.length() * this.lapsCount

    fun locationOf(car: FormulaOneCar) = car.traveledDistance()

    fun advanceTime(timeLapse: Quantity<Time>) {
        this.cars.forEach { this.relocateAfter(it, timeLapse) }
    }

    private fun relocateAfter(car: FormulaOneCar, timeLapse: Quantity<Time>) {
        val distance = car.speed() * timeLapse

        // TODO: implement Unit simplication
        //  Until then, we return Kilometers
        car.travel(distance.amount * Kilometer)
    }
}