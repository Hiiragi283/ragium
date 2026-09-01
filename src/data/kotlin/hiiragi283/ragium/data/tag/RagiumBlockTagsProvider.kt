package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockTagsProvider
import hiiragi283.lib.data.tag.HTTagBuilder
import hiiragi283.lib.resource.HTSimpleKeyOrValue
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlocks
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class RagiumBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTBlockTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        val pickaxe: HTTagBuilder<Block> = builder(BlockTags.MINEABLE_WITH_PICKAXE)
        // Material
        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: RagiumMaterial, block: HTSimpleKeyOrValue<Block>) ->
            builder(part.tagPrefix, material).add(block)
            pickaxe.add(block)
        }

        setOf(
            RagiumMaterial.Mineral.GLOWSTONE to Blocks.GLOWSTONE,
            RagiumMaterial.Gem.QUARTZ to Blocks.QUARTZ_BLOCK,
            RagiumMaterial.Gem.AMETHYST to Blocks.AMETHYST_BLOCK,
        ).forEach { (material: RagiumMaterial, block: Block) -> builder(CommonTagPrefixes.STORAGE_BLOCK, material).addBlock(block) }
        // Machine
        for (machineType: HTMachineType in HTMachineType.entries) {
            createEmptyTag(createTag(HTMachineType.PREFIX, machineType)) // TODO
            for (block: HTSimpleKeyOrValue<Block> in RagiumBlocks.MACHINES[machineType]) {
                builder(HTMachineType.PREFIX, machineType).add(block)
            }
        }

        RagiumBlocks.MACHINES.values.forEach(pickaxe::add)
    }
}
