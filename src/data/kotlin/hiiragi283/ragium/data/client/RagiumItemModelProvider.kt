package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock

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
            addAll(RagiumBlocks.RAGINITE_ORES.oreBlocks)
            addAll(RagiumBlocks.RAGI_CRYSTAL_ORES.oreBlocks)

            removeAll(RagiumBlocks.RAGI_BRICK_SETS.blocks)
            removeAll(RagiumBlocks.PLASTIC_SETS.blocks)
            removeAll(RagiumBlocks.BLUE_NETHER_BRICK_SETS.blocks)

            remove(RagiumBlocks.CRUDE_OIL)
        }.forEach(::simpleBlockItem)

        RagiumBlocks.RAGI_BRICK_SETS.generateModels(this)
        RagiumBlocks.PLASTIC_SETS.generateModels(this)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.generateModels(this)
        // Machine
        for (holder: DeferredBlock<*> in HTMachineType.getBlocks()) {
            getBuilder(holder).parent(ModelFile.UncheckedModelFile(holder.blockId))
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.CHOCOLATE_APPLE)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.STEEL_COMPOUND)

            removeAll(RagiumItems.RAGI_ALLOY_TOOLS.tools)
            removeAll(RagiumItems.DURALUMIN_TOOLS.tools)

            remove(RagiumItems.JETPACK) // TODO
        }.forEach(::basicItem)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("apple"))
            .itemTexture("layer1", RagiumItems.CHOCOLATE_APPLE.id)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("copper_ingot"))
            .itemTexture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.id)

        getBuilder(RagiumItems.STEEL_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("iron_ingot"))
            .itemTexture("layer1", RagiumItems.STEEL_COMPOUND.id)
    }
}
