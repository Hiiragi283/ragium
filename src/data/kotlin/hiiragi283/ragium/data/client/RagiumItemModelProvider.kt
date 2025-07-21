package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.basicItem
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.extension.itemId
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.simpleBlockItem
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem

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
        }.forEach(::simpleBlockItem)

        RagiumBlocks.RAGINITE_ORES.addItemModels(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addItemModels(this)

        for (sets: HTBlockSet in RagiumBlocks.DECORATIONS) {
            sets.addItemModels(this)
        }

        getBuilder(RagiumBlocks.COOKED_MEAT_ON_THE_BONE)
            .parent(modelFile(RagiumAPI.id("block/cooked_meat_on_the_bone_stage0")))
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.AZURE_STEEL_COMPOUND)
            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            removeAll(RagiumItems.FORGE_HAMMERS.values)

            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.forEach(::basicItem)

        getBuilder(RagiumItems.ADVANCED_RAGI_ALLOY_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/gold_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        getBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/iron_ingot")
            .texture("layer1", RagiumItems.AZURE_STEEL_COMPOUND.itemId)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getBuilder(content.bucketHolder)
                .parent(modelFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Armors
        RagiumItems.AZURE_STEEL_ARMORS.addItemModels(this)
        RagiumItems.DEEP_STEEL_ARMORS.addItemModels(this)
        // Tools
        RagiumItems.AZURE_STEEL_TOOLS.addItemModels(this)
        RagiumItems.DEEP_STEEL_TOOLS.addItemModels(this)

        RagiumItems.FORGE_HAMMERS.values
            .map(DeferredItem<*>::getId)
            .forEach(::handheldItem)
    }
}
