package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.basicItem
import hiiragi283.ragium.api.extension.handheldItem
import hiiragi283.ragium.api.extension.itemId
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.simpleBlockItem
import hiiragi283.ragium.api.extension.textureId
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

class RagiumItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private val generated: ModelFile = modelFile("item/generated")

    private fun registerBlocks() {
        // Blocks
        buildSet {
            // Ragium
            addAll(RagiumBlocks.REGISTER.firstEntries)

            remove(RagiumBlocks.EXP_BERRIES)
            remove(RagiumBlocks.WARPED_WART)

            removeAll(RagiumBlocks.GENERATORS.values)
            removeAll(RagiumBlocks.LED_BLOCKS.values)
            removeAll(RagiumBlocks.WALLS.values)
            // Delight
            addAll(RagiumDelightAddon.BLOCK_REGISTER.firstEntries)

            remove(RagiumDelightAddon.RAGI_CHERRY_PIE)
        }.forEach(::simpleBlockItem)

        for ((variant: HTDecorationVariant, wall: DeferredHolder<Block, *>) in RagiumBlocks.WALLS) {
            withExistingParent(wall.id.path, vanillaId("block/wall_inventory"))
                .texture("wall", variant.textureId)
        }
        for (block: HTSimpleDeferredBlockHolder in RagiumBlocks.LED_BLOCKS.values) {
            withExistingParent(block.id.path, RagiumAPI.id("block/led_block"))
        }
    }

    private fun registerItems() {
        val compounds: Map<HTMaterialType, DeferredItem<*>> = RagiumItems.MATERIALS.row(HTMaterialVariant.COMPOUND)
        val tools: Collection<DeferredItem<*>> = RagiumItems.TOOLS.values

        buildSet {
            // Ragium
            addAll(RagiumItems.REGISTER.entries)

            removeAll(compounds.values)

            remove(RagiumItems.BLAST_CHARGE)
            remove(RagiumItems.MEDIUM_DRUM_UPGRADE)
            remove(RagiumItems.LARGE_DRUM_UPGRADE)
            remove(RagiumItems.HUGE_DRUM_UPGRADE)
            removeAll(tools)
            // Delight
            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            // Mekanism
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.forEach(::basicItem)

        basicItem(RagiumBlocks.EXP_BERRIES)
        basicItem(RagiumBlocks.WARPED_WART)

        basicItem(RagiumDelightAddon.RAGI_CHERRY_PIE)

        for ((material: HTMaterialType, compound: DeferredItem<*>) in compounds) {
            val baseId: String = when (material) {
                RagiumMaterialType.RAGI_ALLOY -> "copper_ingot"
                RagiumMaterialType.ADVANCED_RAGI_ALLOY -> "gold_ingot"
                RagiumMaterialType.AZURE_STEEL -> "iron_ingot"
                else -> continue
            }
            val layerId: ResourceLocation = when (material) {
                RagiumMaterialType.ADVANCED_RAGI_ALLOY -> compounds[RagiumMaterialType.RAGI_ALLOY]!!
                else -> compound
            }.itemId
            getBuilder(compound.id.path)
                .parent(generated)
                .texture("layer0", "minecraft:item/$baseId")
                .texture("layer1", layerId)
        }

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getBuilder(content.id.withSuffix("_bucket").path)
                .parent(modelFile("neoforge", "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        buildList {
            add(RagiumItems.BLAST_CHARGE)
            addAll(tools)
        }.forEach(::handheldItem)
    }
}
