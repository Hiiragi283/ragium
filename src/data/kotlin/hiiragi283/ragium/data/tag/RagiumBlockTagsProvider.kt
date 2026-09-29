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

        // Mineable
        val pickaxe: HTTagBuilder<Block> = builder(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(RagiumBlocks.MATERIAL_ORES.values)
            yieldAll(RagiumBlocks.STORAGE_BLOCKS.values)
            yield(RagiumBlocks.ECHO_BLOCK)
            yieldAll(RagiumBlocks.ALL_DECORATIONS.flatMap { it.all })

            yieldAll(RagiumBlocks.MACHINES.values.flatten())
            yield(RagiumBlocks.MACHINE_CASING)
            yieldAll(RagiumBlocks.MACHINE_CASINGS.values)

            yield(RagiumBlocks.FLUID_OUTPUT_BUS)

            yieldAll(RagiumBlocks.TANKS)
            yield(RagiumBlocks.CREATIVE_BATTERY)
        }.forEach(pickaxe::add)
        // Other
        for ((_, slab, stairs, wall) in RagiumBlocks.ALL_DECORATIONS) {
            builder(BlockTags.SLABS).add(slab)
            stairs?.let(builder(BlockTags.STAIRS)::add)
            wall?.let(builder(BlockTags.WALLS)::add)
        }
    }
}
