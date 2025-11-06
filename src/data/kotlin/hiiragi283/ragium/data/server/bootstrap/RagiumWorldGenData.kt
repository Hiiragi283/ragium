package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTWorldGenData
import net.minecraft.data.worldgen.placement.OrePlacements

object RagiumWorldGenData {
    /**
     * @see OrePlacements.ORE_REDSTONE
     */
    @JvmField
    val ORE_RAGINITE: HTWorldGenData = HTWorldGenData("ore_${RagiumConst.RAGINITE}")

    /**
     * @see OrePlacements.ORE_REDSTONE_LOWER
     */
    @JvmField
    val ORE_RAGINITE_LOWER: HTWorldGenData = HTWorldGenData(ORE_RAGINITE, "ore_${RagiumConst.RAGINITE}_lower")

    /**
     * @see OrePlacements.ORE_ANCIENT_DEBRIS_SMALL
     */
    @JvmField
    val ORE_RESONANT_DEBRIS: HTWorldGenData = HTWorldGenData("ore_resonant_debris")
}
