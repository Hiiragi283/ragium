package hiiragi283.ragium.common.energy

/**
 * @param temp 0: frozen/snowy, 1: cool, 2: warm, 3: hot, 4: nether
 * @param height 0: closed area, 1: under the ocean, 2: surface, 3: hill, 4: mountain
 * @param terra 0: ocean, 1: island, 2: continent, 3: mountain, 4: underground
 * @param humidity 0: nether, 1: dried, 2: plains/forest, 3: sweaty, 4: ocean/river
 * @param eldritch 0: no hostile mobs, 1: good to live, 2: hard to live, 3: nether, 4: deep dark/the end,
 */
data class HTBiomeParameter(
    val temp: Level,
    val height: Level,
    val terra: Level,
    val humidity: Level,
    val eldritch: Level,
) {
    constructor(temp: Int, height: Int, terra: Int, humidity: Int, eldritch: Int) : this(
        Level.entries[temp],
        Level.entries[height],
        Level.entries[terra],
        Level.entries[humidity],
        Level.entries[eldritch],
    )

    enum class Level {
        LOWEST,
        LOW,
        MIDDLE,
        HIGH,
        HIGHEST,
    }
}
