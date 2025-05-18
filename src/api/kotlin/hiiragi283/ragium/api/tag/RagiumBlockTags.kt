package hiiragi283.ragium.api.tag

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * Ragiumが使用するブロックの[TagKey]の一覧
 */
object RagiumBlockTags {
    @JvmField
    val GLASS_BLOCKS_OBSIDIAN: TagKey<Block> =
        blockTagKey(commonId(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.OBSIDIAN))

    @JvmField
    val GLASS_BLOCKS_QUARTZ: TagKey<Block> =
        blockTagKey(commonId(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.QUARTZ))

    @JvmField
    val GLASS_BLOCKS_SOUL: TagKey<Block> =
        blockTagKey(commonId(RagiumConstantValues.GLASS_BLOCKS, RagiumConstantValues.SOUL))

    @JvmField
    val OBSIDIANS_MYSTERIOUS: TagKey<Block> = blockTagKey(commonId("obsidians/mysterious"))

    @JvmField
    val STONES_ROCK_GENERATIONS: TagKey<Block> = blockTagKey(RagiumAPI.id("stones/rock_generation"))
}
