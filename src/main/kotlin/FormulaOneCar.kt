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
    }

    var turboStatus = TurboDeactivated.on(this)
    var sector: Sector? = null
    var speed: Speed = 0 * (Kilometer / Hour)

    fun atSector(sector: Sector) {
        this.sector = sector
    }

    fun speed(speed: Speed) {
        this.speed = speed
    }

    fun activateTurbo() {
        this.turboStatus.activateTurbo()
    }

    /**
     * Double dispatch with TurboStatus callbacks
     */
    fun activeTurboWhenTurboDeactivated() {
        this.sector?.carActivatingTurbo(this)
    }

    /**
     * Double dispatch with Sector callbacks
     */
    fun activateTurboInTurboSector() {
        this.turboStatus = this.turboStatus.next()
    }

    fun isTurboActivated(): Boolean = this.turboStatus.isActivated()
}

typealias Speed = Quantity<QuotientUnit<Distance, Time>>