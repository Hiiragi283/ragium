package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.data.tag.HTTagBuilder
import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class RagiumBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        RagiumBlockItemTagsProvider { (block: TagKey<Block>, _) ->
            HTBlockItemTagsProvider.forBlock(builder(block))
        }.run()

        val pickaxe: HTTagBuilder<Block> = builder(BlockTags.MINEABLE_WITH_PICKAXE)
        // Mineable
        sequence {
            yieldAll(RagiumBlocks.MATERIAL_BLOCKS.values)
            yieldAll(RagiumBlocks.MACHINES.values)
        }.forEach { pickaxe.add(it.block) }
    }
}
