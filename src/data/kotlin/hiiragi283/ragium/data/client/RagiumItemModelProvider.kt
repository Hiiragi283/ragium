package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluidContents
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private val generated: ModelFile = modelFile(vanillaId("item/generated"))

    private fun registerBlocks() {
        // Blocks
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            remove(RagiumBlocks.LILY_OF_THE_ENDER)
        }.forEach(::simpleBlockItem)

        RagiumBlocks.RAGINITE_ORES.addItemModels(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addItemModels(this)

        RagiumBlocks.RAGI_STONE_SETS.addItemModels(this)
        RagiumBlocks.RAGI_STONE_SQUARE_SETS.addItemModels(this)
        RagiumBlocks.AZURE_TILE_SETS.addItemModels(this)
        RagiumBlocks.EMBER_STONE_SETS.addItemModels(this)
        RagiumBlocks.PLASTIC_SETS.addItemModels(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addItemModels(this)

        getBuilder(RagiumBlocks.LILY_OF_THE_ENDER)
            .parent(generated)
            .texture("layer0", RagiumBlocks.LILY_OF_THE_ENDER.blockId)

        getBuilder(RagiumDelightAddon.COOKED_MEAT_ON_THE_BONE)
            .parent(modelFile(RagiumAPI.id("block/cooked_meat_on_the_bone_stage0")))
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.AZURE_STEEL_COMPOUND)

            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.forEach(::basicItem)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        getBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/gold_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        getBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/iron_ingot")
            .texture("layer1", RagiumItems.AZURE_STEEL_COMPOUND.itemId)

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getBuilder(content.bucketHolder)
                .parent(modelFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addItemModels(this)
        RagiumItems.AZURE_STEEL_TOOLS.addItemModels(this)
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addItemModels(this)
    }
}
