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
            yield(RagiumBlocks.FLUORITE_BLOCK)
            yield(RagiumBlocks.FLUORITE_SLAB)
            yield(RagiumBlocks.FLUORITE_STAIRS)
            yield(RagiumBlocks.CRYOLITE_BLOCK)
            yield(RagiumBlocks.CRYOLITE_SLAB)
            yield(RagiumBlocks.CRYOLITE_STAIRS)

            yieldAll(RagiumBlocks.MACHINES.values.flatten())
            yield(RagiumBlocks.MACHINE_CASING)
            yieldAll(RagiumBlocks.MACHINE_CASINGS.values)

            yield(RagiumBlocks.TANK)
            yield(RagiumBlocks.VOID_TANK)
            yield(RagiumBlocks.CREATIVE_BATTERY)
            yield(RagiumBlocks.CREATIVE_TANK)
        }.forEach(pickaxe::add)
        // Other
        builder(BlockTags.SLABS)
            .add(RagiumBlocks.FLUORITE_SLAB)
            .add(RagiumBlocks.CRYOLITE_SLAB)
        builder(BlockTags.STAIRS)
            .add(RagiumBlocks.FLUORITE_STAIRS)
            .add(RagiumBlocks.CRYOLITE_STAIRS)
    }
}
