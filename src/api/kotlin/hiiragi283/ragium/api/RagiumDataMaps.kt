package hiiragi283.ragium.api

import hiiragi283.ragium.api.data.interaction.HTBlockInteraction
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * @see [net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps]
 */
object RagiumDataMaps {
    @JvmField
    val BLOCK_INTERACTION: DataMapType<Block, HTBlockInteraction> =
        DataMapType
            .builder(RagiumAPI.id("block_interaction"), Registries.BLOCK, HTBlockInteraction.CODEC)
            .synced(HTBlockInteraction.CODEC, false)
            .build()
}
