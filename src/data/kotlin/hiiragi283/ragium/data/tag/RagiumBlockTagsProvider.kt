package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTTagBuilder
import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.block.RagiumBlocks
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class RagiumBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        val pickaxe: HTTagBuilder<Block> = builder(BlockTags.MINEABLE_WITH_PICKAXE)
        // Material
        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: HTMaterial, block: HTKeyLike<Block>) ->
            tags(part.tagPrefix, material).add(block)
            pickaxe.add(block)
        }

        setOf(
            HTMaterial.Mineral.GLOWSTONE to Blocks.GLOWSTONE,
            HTMaterial.Gem.QUARTZ to Blocks.QUARTZ_BLOCK,
            HTMaterial.Gem.AMETHYST to Blocks.AMETHYST_BLOCK,
        ).forEach { (material: HTMaterial, block: Block) -> tags(CommonTagPrefixes.STORAGE_BLOCK, material).add(block.asSupplier()) }
        // Machine
        pickaxe
            // Mechanical
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            // Heat
            .add(RagiumBlocks.FREEZER)
            .add(RagiumBlocks.MELTER)
    }
}
