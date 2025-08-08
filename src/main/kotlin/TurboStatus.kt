interface TurboStatusCompanion {
    fun on(car: FormulaOneCar): TurboStatus
}

abstract class TurboStatus(val car: FormulaOneCar) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus {
            throw Error("Should be implemented in the subclasses")
        }
    }

    fun activateTurbo() {
        this.car.activeTurboWhenTurboDeactivated()
    }

    fun deactivateTurbo() {
        this.car.deactivateTurboWhenTurboActivated()
    }

    abstract fun next(): TurboStatus
    abstract fun isActivated(): Boolean
    abstract fun speedUp(speed: Speed): Speed
}

class TurboActivated(car: FormulaOneCar, val timesUsed: Int = 0): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        val ratios=listOf(0.20, 0.10, 0.05)

        override fun on(car: FormulaOneCar): TurboStatus = TurboActivated(car)
    }

    override fun next(): TurboStatus = TurboActivated(this.car, this.timesUsed + 1)
    override fun isActivated(): Boolean = true
    override fun speedUp(speed: Speed): Speed = speed + speed * ratios[timesUsed]
}

class TurboDeactivated(car: FormulaOneCar, val nextActivatedStatus: TurboActivated): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboDeactivated(car, TurboActivated(car))
    }

    override fun next(): TurboStatus = nextActivatedStatus
    override fun isActivated(): Boolean = false
    override fun speedUp(speed: Speed): Speed = speed
}