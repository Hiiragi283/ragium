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
    val STONES_ROCK_GENERATIONS: TagKey<Block> = blockTagKey(RagiumAPI.id("stones/rock_generation"))
}
