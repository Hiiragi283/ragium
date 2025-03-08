package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * Ragiumが使用するブロックの[TagKey]の一覧
 */
object RagiumBlockTags {
    @JvmField
    val COOLING_SOURCES: TagKey<Block> = blockTagKey(RagiumAPI.id("sources/cooling"))

    @JvmField
    val HEATING_SOURCES: TagKey<Block> = blockTagKey(RagiumAPI.id("sources/heating"))

    @JvmField
    val MINEABLE_WITH_HAMMER: TagKey<Block> = blockTagKey(RagiumAPI.id("mineable/forge_hammer"))
}
