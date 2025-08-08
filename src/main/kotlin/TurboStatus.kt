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

    abstract fun next(): TurboStatus
    abstract fun isActivated(): Boolean
    abstract fun speedUp(speed: Speed): Speed
}

class TurboActivated(car: FormulaOneCar): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboActivated(car)
    }

    val ratios=listOf(0.20, 0.10, 0.05)
    var timesUsed = 0

    override fun next(): TurboStatus = this
    override fun isActivated(): Boolean = true
    override fun speedUp(speed: Speed): Speed {
        val turboSpeed = speed + speed * ratios[timesUsed]
        timesUsed += 1
        return turboSpeed
    }
}

class TurboDeactivated(car: FormulaOneCar): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboDeactivated(car)
    }

    override fun next(): TurboStatus = TurboActivated(car)
    override fun isActivated(): Boolean = false
    override fun speedUp(speed: Speed): Speed = speed
}