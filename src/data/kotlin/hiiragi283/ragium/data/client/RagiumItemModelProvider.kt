package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import kotlin.collections.forEach

class RagiumItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private fun registerBlocks() {
        // Blocks
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            // remove(RagiumBlocks.CRUDE_OIL)
        }.forEach(::simpleBlockItem)

        RagiumBlocks.RAGINITE_ORES.addItemModels(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addItemModels(this)

        RagiumBlocks.RAGI_BRICK_SETS.addItemModels(this)
        RagiumBlocks.AZURE_TILE_SETS.addItemModels(this)
        RagiumBlocks.PLASTIC_SETS.addItemModels(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.addItemModels(this)
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.CHOCOLATE_APPLE)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.AZURE_STEEL_COMPOUND)
        }.forEach(::basicItem)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", "minecraft:item/apple")
            .texture("layer1", RagiumItems.CHOCOLATE_APPLE.id.withPrefix("item/"))

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.id.withPrefix("item/"))

        getBuilder(RagiumItems.AZURE_STEEL_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", "minecraft:item/iron_ingot")
            .texture("layer1", RagiumItems.AZURE_STEEL_COMPOUND.id.withPrefix("item/"))

        // Tool
        RagiumItems.RAGI_ALLOY_TOOLS.addItemModels(this)
        RagiumItems.AZURE_STEEL_TOOLS.addItemModels(this)
        // Armor
        RagiumItems.AZURE_STEEL_ARMORS.addItemModels(this)
    }
}
