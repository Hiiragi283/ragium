package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
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

        RagiumBlocks.RAGI_BRICK_SETS.addItemModels(this)
        RagiumBlocks.AZURE_TILE_SETS.addItemModels(this)
        RagiumBlocks.EMBER_STONE_SETS.addItemModels(this)
        RagiumBlocks.PLASTIC_SETS.addItemModels(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addItemModels(this)

        getBuilder(RagiumBlocks.LILY_OF_THE_ENDER)
            .parent(generated)
            .texture("layer0", RagiumBlocks.LILY_OF_THE_ENDER.blockId)
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            removeAll(RagiumItems.Buckets.items)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            remove(RagiumItems.CHOCOLATE_APPLE)
        }.forEach(::basicItem)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(generated)
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(generated)
            .texture("layer0", "minecraft:item/apple")
            .texture("layer1", RagiumItems.CHOCOLATE_APPLE.itemId)

        for (bucket in RagiumItems.Buckets.entries) {
            getBuilder(bucket.holder)
                .parent(modelFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(bucket.fluid.get())
        }

        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addItemModels(this)
        RagiumItems.AZURE_STEEL_TOOLS.addItemModels(this)
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addItemModels(this)
    }
}
