package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTBlockTagsProvider
import hiiragi283.lib.data.tag.HTTagBuilder
import hiiragi283.lib.data.tag.builders
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.material.VanillaMaterialKeys
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
        setOf(
            VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE,
            VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK,
            VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK,
        ).forEach { (material: HTMaterialKey, block: Block) -> builders(CommonTagPrefixes.STORAGE_BLOCK, material).addBlock(block) }
        // Machine
        for (machineType: HTMachineType in HTMachineType.entries) {
            createEmptyTag(HTMachineType.PREFIX.blockTagKey(machineType)) // TODO
            for (block: HTKeyLike<Block> in RagiumBlocks.MACHINES[machineType]) {
                builders(HTMachineType.PREFIX, machineType).add(block)
            }
        }

        RagiumBlocks.MACHINES.values.forEach(pickaxe::add)
    }
}
