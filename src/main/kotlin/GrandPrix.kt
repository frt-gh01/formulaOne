import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.Time
import units.times

class GrandPrix(val track: Track, val lapsCount: Int, val cars: List<FormulaOneCar>) {
    companion object {
        fun start(track: Track, lapsCount: Int, cars: List<FormulaOneCar>): GrandPrix = GrandPrix(track, lapsCount, cars)
    }

    init {
        this.cars.forEach { this.track.placeAtStart(it) }
    }

    fun length() = this.track.length() * this.lapsCount

    fun locationOf(car: FormulaOneCar) = car.traveledDistance()

    fun sectorOf(car: FormulaOneCar) = this.track.sectorOf(car)

    fun advanceTime(timeLapse: Quantity<Time>) {
        this.cars.forEach { this.relocateAfter(it, timeLapse) }
    }

    private fun relocateAfter(car: FormulaOneCar, timeLapse: Quantity<Time>) {
        val distance = car.speed() * timeLapse

        // TODO: implement Unit simplication
        //  Until then, we return Kilometers
        car.travel(distance.amount * Kilometer)

        // TODO: implement Unit `%`
        //  Until then, we return Kilometers
        val distanceInTrack = car.traveledDistance().amount % track.length().amount
        track.placeAt(car, distanceInTrack * Kilometer)
    }
}