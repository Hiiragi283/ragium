package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.itemId
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.common.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumItemModelProvider(context: HTDataGenContext) : ItemModelProvider(context.output, RagiumAPI.MOD_ID, context.fileHelper) {
    private val fileHelper: ExistingFileHelper = context.fileHelper

    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private val generated: ModelFile = ModelFile.ExistingModelFile(vanillaId("item", "generated"), fileHelper)

    private fun registerBlocks() {
        // Blocks
        buildSet {
            // Ragium
            addAll(RagiumBlocks.REGISTER.firstEntries)

            remove(RagiumBlocks.EXP_BERRIES)
            remove(RagiumBlocks.WARPED_WART)

            remove(HTGeneratorVariant.SOLAR.blockHolder)
            remove(HTGeneratorVariant.NUCLEAR_REACTOR.blockHolder)

            removeAll(RagiumBlocks.LED_BLOCKS.values)
            removeAll(RagiumBlocks.WALLS.values)
            // Delight
            addAll(RagiumDelightAddon.BLOCK_REGISTER.firstEntries)

            remove(RagiumDelightAddon.RAGI_CHERRY_PIE)
            remove(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK)
        }.forEach(::simpleBlockItem)

        for ((variant: HTDecorationVariant, wall: HTHolderLike) in RagiumBlocks.WALLS) {
            withExistingParent(wall.getPath(), vanillaId("block", "wall_inventory"))
                .texture("wall", variant.textureId)
        }
        for (block: HTSimpleDeferredBlock in RagiumBlocks.LED_BLOCKS.values) {
            withExistingParent(block.getPath(), RagiumAPI.id("block", "led_block"))
        }
    }

    private fun registerItems() {
        val tools: Collection<HTDeferredItem<*>> = RagiumItems.TOOLS.values

        buildSet {
            // Ragium
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            remove(RagiumItems.BLAST_CHARGE)
            remove(RagiumItems.MEDIUM_DRUM_UPGRADE)
            remove(RagiumItems.LARGE_DRUM_UPGRADE)
            remove(RagiumItems.HUGE_DRUM_UPGRADE)
            removeAll(tools)
            // Delight
            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)

            removeAll(RagiumDelightAddon.KNIFE_MAP.values)
            remove(RagiumDelightAddon.RAGI_CHERRY_TOAST) // TODO
            // Mekanism
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.forEach(::basicItem)

        basicItem(RagiumBlocks.EXP_BERRIES)
        basicItem(RagiumBlocks.WARPED_WART)

        basicItem(RagiumDelightAddon.RAGI_CHERRY_PIE)
        // basicItem(RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCk)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND.getPath())
            .parent(generated)
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getBuilder(content.getId().withSuffix("_bucket").path)
                .parent(ModelFile.ExistingModelFile(RagiumConst.NEOFORGE.toId("item", "bucket"), fileHelper))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        buildList {
            addAll(tools)
            add(RagiumItems.BLAST_CHARGE)

            addAll(RagiumDelightAddon.KNIFE_MAP.values)
        }.forEach(::handheldItem)
    }

    //    Extensions    //

    private fun simpleBlockItem(block: HTHolderLike): ItemModelBuilder = simpleBlockItem(block.getId())

    private fun basicItem(item: HTHolderLike): ItemModelBuilder = basicItem(item.getId())

    private fun basicItem(block: HTDeferredBlock<*, *>): ItemModelBuilder = basicItem(block.itemHolder)

    private fun handheldItem(item: HTHolderLike): ItemModelBuilder = handheldItem(item.getId())
}
