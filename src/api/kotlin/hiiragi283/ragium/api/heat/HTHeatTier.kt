package hiiragi283.ragium.api.heat

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability

enum class HTHeatTier : StringRepresentable {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
    ;

    companion object {
        @JvmField
        val BLOCK_CAPABILITY: BlockCapability<HTHeatTier, Direction?> =
            BlockCapability.createSided(RagiumAPI.id("heat"), HTHeatTier::class.java)

        @JvmStatic
        fun getHeatTierFromIndex(index: Int): HTHeatTier? = entries.firstOrNull { it.ordinal == index }

        @JvmStatic
        fun getHeatTier(level: Level, pos: BlockPos, direction: Direction): HTHeatTier =
            level.getCapability(BLOCK_CAPABILITY, pos, direction) ?: NONE
    }

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
