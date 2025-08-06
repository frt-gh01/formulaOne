class FormulaOneCar(val driver: Driver) {
    companion object {
        fun drivenBy(driver: Driver) = FormulaOneCar(driver)
    }

    var turboStatus = TurboStatus.on(this)
    var sector: Sector? = null

    fun atSector(sector: Sector) {
        this.sector = sector
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