package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

class RagiumModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private fun registerBlocks() {
        // Blocks
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            remove(RagiumBlocks.CRUDE_OIL)
        }.forEach(::simpleBlockItem)
        // Machine
        HTMachineType.getBlocks().forEach { holder: DeferredBlock<*> ->
            withUncheckedParent(holder, holder.blockId)
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.CHOCOLATE_APPLE)

            remove(RagiumItems.MACHINE_CASING)
            remove(RagiumItems.CHEMICAL_MACHINE_CASING)
            remove(RagiumItems.PRECISION_MACHINE_CASING)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.forEach(::basicItem)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("apple"))
            .itemTexture("layer1", RagiumItems.CHOCOLATE_APPLE.id)

        getBuilder(RagiumItems.POTION_CAN)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", RagiumItems.POTION_CAN.id)
            .itemTexture("layer1", RagiumItems.POTION_CAN.id.withSuffix("_overlay"))

        listOf(
            RagiumItems.MACHINE_CASING,
            RagiumItems.CHEMICAL_MACHINE_CASING,
            RagiumItems.PRECISION_MACHINE_CASING,
        ).forEach { holder: DeferredItem<Item> ->
            val path: String = holder.id.path
            withExistingParent(path, RagiumAPI.id("block/machine_casing"))
                .texture("side", RagiumAPI.id("block/${path}_side"))
                .texture("bottom", RagiumAPI.id("block/${path}_bottom"))
                .texture("top", RagiumAPI.id("block/${path}_top"))
        }

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("copper_ingot"))
            .itemTexture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.id)
    }
}
