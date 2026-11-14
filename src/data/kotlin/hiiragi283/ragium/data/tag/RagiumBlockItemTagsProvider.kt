package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockItemTagBuilder
import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.resource.BlockItemKey
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.BlockItemTag
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTBlockPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import net.neoforged.neoforge.common.Tags

class RagiumBlockItemTagsProvider(factory: (BlockItemTag) -> HTBlockItemTagBuilder) : HTBlockItemTagsProvider(factory) {
    override fun run() {
        // Material
        RagiumBlocks.MATERIAL_BLOCKS
            .forEach { (part: HTBlockPart, material: RagiumMaterial, block: HTSimpleDeferredBlockAndItem) ->
                builder(part.tagPrefix, material).add(block)
            }

        builder(RagiumTags.BlockItems.QUARTZ_BLOCKS)
            .add(BlockItemKey(vanillaId("chiseled_quartz_block")))
            .add(BlockItemKey(vanillaId("quartz_block")))
            .add(BlockItemKey(vanillaId("quartz_bricks")))
            .add(BlockItemKey(vanillaId("quartz_pillar")))

        // Decoration
        builder(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS).addTag(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ)
        builder(HTCommonTags.BlockItems.GLASS_BLOCKS_QUARTZ).add(RagiumBlocks.QUARTZ_GLASS)

        builder(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES).addTag(HTCommonTags.BlockItems.GLASS_PANES_QUARTZ)
        builder(HTCommonTags.BlockItems.GLASS_PANES_QUARTZ).add(RagiumBlocks.QUARTZ_GLASS_PANE)

        RagiumBlocks.ALL_DECORATIONS.forEach { it.appendTags(::builder) }
        // Generator
        for ((machineType: HTMachineType, generators: List<HTSimpleDeferredBlockAndItem>) in RagiumBlocks.GENERATORS.entries) {
            for (generator: HTSimpleDeferredBlockAndItem in generators) {
                builder(HTMachineType.GENERATOR_PREFIX, machineType).add(generator)
            }
        }
        // Processor
        for ((machineType: HTMachineType, processors: List<HTSimpleDeferredBlockAndItem>) in RagiumBlocks.PROCESSORS.entries) {
            for (processor: HTSimpleDeferredBlockAndItem in processors) {
                builder(HTMachineType.PROCESSOR_PREFIX, machineType).add(processor)
            }
        }
        // Storage
        builder(RagiumTags.BlockItems.STORAGES_CREATIVE)
            .add(RagiumBlocks.CREATIVE_BATTERY)
            .add(RagiumBlocks.CREATIVE_TANK)
        // Machine Decoration
        builder(RagiumTags.BlockItems.DECORATIONS_MACHINE).apply {
            RagiumBlocks.DECORATION_CASINGS.forEach(this::add)
            RagiumBlocks.DECORATION_HULLS.values.forEach(this::add)
        }
    }
}
