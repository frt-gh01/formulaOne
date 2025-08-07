class GrandPrix(val track: Track, val lapsCount: Int, val cars: List<FormulaOneCar>) {
    companion object {
        fun start(track: Track, lapsCount: Int, cars: List<FormulaOneCar>): GrandPrix = GrandPrix(track, lapsCount, cars)
    }

    fun length() = this.track.length() * this.lapsCount
}