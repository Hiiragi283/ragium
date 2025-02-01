package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object RagiumBlockTags {
    @JvmField
    val COOLING_SOURCES: TagKey<Block> = blockTagKey(RagiumAPI.id("sources/cooling"))

    @JvmField
    val HEATING_SOURCES: TagKey<Block> = blockTagKey(RagiumAPI.id("sources/heating"))
}
