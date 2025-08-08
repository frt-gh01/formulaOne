interface TurboStatusCompanion {
    fun on(car: FormulaOneCar): TurboStatus
}

abstract class TurboStatus(val car: FormulaOneCar) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus {
            throw Error("Should be implemented in the subclasses")
        }
    }

    abstract fun activateTurbo()
    abstract fun deactivateTurbo()
    abstract fun next(): TurboStatus
    abstract fun isActivated(): Boolean
    abstract fun speedUp(speed: Speed): Speed
}

class TurboActivated(car: FormulaOneCar, val timesUsed: Int = 0): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        val ratios=listOf(0.20, 0.10, 0.05, 0.0)

        override fun on(car: FormulaOneCar): TurboStatus = TurboActivated(car)
    }

    override fun activateTurbo() = this.car.activeTurboWhenTurboActivated()
    override fun deactivateTurbo() = this.car.deactivateTurboWhenTurboActivated()

    override fun next(): TurboStatus {
        return when (val nextTimeUsed = this.timesUsed + 1) {
            in 0 until ratios.size -> TurboActivated(this.car, nextTimeUsed)
            else -> TurboActivated(this.car, ratios.lastIndex)
        }
    }

    override fun isActivated(): Boolean = true
    override fun speedUp(speed: Speed): Speed = speed + speed * ratios[timesUsed]
}

class TurboDeactivated(car: FormulaOneCar, val nextActivatedStatus: TurboActivated): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboDeactivated(car, TurboActivated(car))
    }

    override fun activateTurbo() = this.car.activeTurboWhenTurboDeactivated()
    override fun deactivateTurbo() = this.car.deactiveTurboWhenTurboDeactivated()
    override fun next(): TurboStatus = nextActivatedStatus
    override fun isActivated(): Boolean = false
    override fun speedUp(speed: Speed): Speed = speed
}