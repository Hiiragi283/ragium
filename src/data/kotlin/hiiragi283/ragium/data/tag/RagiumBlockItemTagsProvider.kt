package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockItemTagBuilder
import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.resource.BlockItemKey
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks

class RagiumBlockItemTagsProvider(factory: (BlockItemTag) -> HTBlockItemTagBuilder) : HTBlockItemTagsProvider(factory) {
    override fun run() {
        // Material
        RagiumBlocks.MATERIAL_BLOCKS
            .forEach { (part: HTBlockPart, material: RagiumMaterial, block: HTSimpleDeferredBlockAndItem) ->
                builder(part.tagPrefix, material).add(block)
            }

        builder(RagiumTags.BlockItem.QUARTZ_BLOCKS)
            .add(BlockItemKey(vanillaId("chiseled_quartz_block")))
            .add(BlockItemKey(vanillaId("quartz_block")))
            .add(BlockItemKey(vanillaId("quartz_bricks")))
            .add(BlockItemKey(vanillaId("quartz_pillar")))
        // Machine
        for (machineType: HTMachineType in HTMachineType.entries) {
            for (block: HTSimpleDeferredBlockAndItem in RagiumBlocks.MACHINES[machineType]) {
                builder(HTMachineType.PREFIX, machineType).add(block)
            }
        }
    }
}
