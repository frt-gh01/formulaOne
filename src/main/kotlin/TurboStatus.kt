class TurboStatus(val car: FormulaOneCar) {
    companion object {
        fun on(car: FormulaOneCar) = TurboStatus(car)
    }

    fun activateTurbo() {
        this.car.activeTurboWhenTurboDeactivated()
    }

    fun next(): TurboStatus {
        return this
    }

    fun isActivated(): Boolean = false
}