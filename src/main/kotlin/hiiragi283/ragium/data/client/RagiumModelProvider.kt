package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.getBuilder
import hiiragi283.ragium.data.itemTexture
import hiiragi283.ragium.data.withUncheckedParent
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock

class RagiumModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private fun registerBlocks() {
        buildList {
            addAll(RagiumBlocks.Ores.entries)
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(HTBlockContent::id).forEach(::simpleBlockItem)

        RagiumBlocks.Decorations.entries.forEach { decoration: RagiumBlocks.Decorations ->
            withUncheckedParent(decoration, decoration.parent.blockId)
        }

        buildList {
            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SHAFT)
            add(RagiumBlocks.CHEMICAL_GLASS)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)

            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
        }.map(DeferredBlock<*>::getId).forEach(::simpleBlockItem)

        // Machine
        RagiumAPI.machineRegistry.entryMap.forEach { (key: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            val modelId: ResourceLocation =
                entry.getOrDefault(HTMachinePropertyKeys.ITEM_MODEL_MAPPER).apply(key, false)
            withUncheckedParent(entry, modelId)
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.MATERIALS)
            addAll(RagiumItems.OTHER_DUSTS)
            addAll(RagiumItems.OTHER_RESOURCES)
            addAll(RagiumItems.OTHER_INGOTS)

            addAll(RagiumItems.FOODS)
            remove(RagiumItems.CHOCOLATE_APPLE)

            add(RagiumItems.FORGE_HAMMER)
            add(RagiumItems.DYNAMITE)
            add(RagiumItems.SLOT_LOCK)

            addAll(RagiumItems.Circuits.entries)
            addAll(RagiumItems.PRESS_MOLDS)
            addAll(RagiumItems.CATALYSTS)
            addAll(RagiumItems.Plastics.entries)
            addAll(RagiumItems.Radioactives.entries)

            addAll(RagiumItems.INGREDIENTS)
            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.map(ItemLike::asItem)
            .forEach(::basicItem)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("apple"))
            .itemTexture("layer1", RagiumItems.CHOCOLATE_APPLE.id)

        getBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("copper_ingot"))
            .itemTexture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.id)
    }
}
