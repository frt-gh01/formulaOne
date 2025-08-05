class FormulaOneCar(val driver: Driver) {
    companion object {
        fun drivenBy(driver: Driver) = FormulaOneCar(driver)
    }
}