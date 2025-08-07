class Track(val sectors: List<Sector>) {
    companion object {
        fun with(sectors: List<Sector>) {
            require(sectors.size > 0) { noSectorsErrorDescription() }
        }

        fun noSectorsErrorDescription() = "Track must have sectors"
    }
}