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
}

class TurboActivated(car: FormulaOneCar): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboActivated(car)
    }

    override fun next(): TurboStatus = this
    override fun isActivated(): Boolean = true
}

class TurboDeactivated(car: FormulaOneCar): TurboStatus(car) {
    companion object: TurboStatusCompanion {
        override fun on(car: FormulaOneCar): TurboStatus = TurboDeactivated(car)
    }

    override fun next(): TurboStatus = TurboActivated(car)
    override fun isActivated(): Boolean = false
}