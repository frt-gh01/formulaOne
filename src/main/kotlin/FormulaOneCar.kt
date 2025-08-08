import units.Distance
import units.Distance.Companion.Kilometer
import units.Quantity
import units.QuotientUnit
import units.Time
import units.Time.Companion.Hour
import units.div
import units.times

class FormulaOneCar(val driver: Driver) {
    companion object {
        fun drivenBy(driver: Driver) = FormulaOneCar(driver)

        fun turboAlreadyActivatedErrorDescription(): String = "Turbo already activated"
        fun turboAlreadyDeactivatedErrorDescription(): String = "Turbo already deactivated"
    }

    var turboStatus = TurboDeactivated.on(this)
    var sector: Sector? = null
    private var speed: Speed = 0 * (Kilometer / Hour)
    var traveledDistance: Quantity<Distance> = 0 * Kilometer

    fun atSector(sector: Sector) {
        this.sector = sector
    }

    fun speed(speed: Speed) {
        this.speed = speed
    }

    fun speed(): Speed {
        return turboStatus.speedUp(this.speed)
    }

    fun traveledDistance(): Quantity<Distance> = this.traveledDistance

    fun travel(distance: Quantity<Distance>) {
        this.traveledDistance += distance
    }

    fun activateTurbo() {
        this.turboStatus.activateTurbo()
    }

    fun deactivateTurbo() {
        this.turboStatus.deactivateTurbo()
    }

    /**
     * Double dispatch with TurboStatus callbacks
     */
    fun activeTurboWhenTurboDeactivated() {
        this.sector?.carActivatingTurbo(this)
    }

    fun deactivateTurboWhenTurboActivated() {
        this.turboStatus = TurboDeactivated(this, this.turboStatus.next() as TurboActivated)
    }

    fun activeTurboWhenTurboActivated() {
        throw IllegalStateException(turboAlreadyActivatedErrorDescription())
    }

    fun deactiveTurboWhenTurboDeactivated() {
        throw IllegalStateException(turboAlreadyDeactivatedErrorDescription())
    }

    /**
     * Double dispatch with Sector callbacks
     */
    fun activateTurboInTurboSector() {
        this.turboStatus = this.turboStatus.next()
    }

    fun isTurboActivated(): Boolean = this.turboStatus.isActivated()
}

typealias SpeedUnit = QuotientUnit<Distance, Time>
typealias Speed = Quantity<SpeedUnit>
