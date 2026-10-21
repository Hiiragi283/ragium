package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockItemTagBuilder
import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import net.minecraft.references.BlockItemIds
import net.minecraft.tags.BlockItemTagId
import net.neoforged.neoforge.common.Tags

class RagiumBlockItemTagsProvider(factory: (BlockItemTagId) -> HTBlockItemTagBuilder) :
    HTBlockItemTagsProvider(factory) {
    override fun run() {
        // Material
        RagiumBlocks.MATERIAL_BLOCKS
            .forEach { (part: HTBlockPart, material: RagiumMaterial, block: HTSimpleDeferredBlockAndItem) ->
                builder(part.tagPrefix, material).add(block)
            }

        builder(RagiumTags.BlockItems.QUARTZ_BLOCKS)
            .add(BlockItemIds.CHISELED_QUARTZ_BLOCK)
            .add(BlockItemIds.QUARTZ_BLOCK)
            .add(BlockItemIds.QUARTZ_BRICKS)
            .add(BlockItemIds.QUARTZ_PILLAR)

        // Decoration
        builder(Tags.BlockItems.GLASS_BLOCKS).addTag(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ)
        builder(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ).add(RagiumBlocks.QUARTZ_GLASS)

        builder(Tags.BlockItems.GLASS_PANES).addTag(HTCommonTags.BlockItems.GLASS_PANES_QUARTZ)
        builder(HTCommonTags.BlockItems.GLASS_PANES_QUARTZ).add(RagiumBlocks.QUARTZ_GLASS_PANE)

        RagiumBlocks.ALL_DECORATIONS.forEach { it.appendTags(::builder) }
        // Machine
        for ((machineType: HTMachineType, machines) in RagiumBlocks.MACHINES.entries) {
            for (machine: HTSimpleDeferredBlockAndItem in machines) {
                builder(HTMachineType.PREFIX, machineType).add(machine)
            }
        }
        // Storage
        builder(RagiumTags.BlockItems.STORAGES_CREATIVE)
            .add(RagiumBlocks.CREATIVE_BATTERY)
            .add(RagiumBlocks.CREATIVE_TANK)
    }
}
