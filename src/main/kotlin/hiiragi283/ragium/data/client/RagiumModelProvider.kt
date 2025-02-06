package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.extension.itemTexture
import hiiragi283.ragium.api.extension.withUncheckedParent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.property.getOrDefault
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
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
        buildList {
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Burners.entries)

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(HTBlockContent::id).forEach(::simpleBlockItem)

        buildList {
            addAll(RagiumBlocks.ORES.values)
            addAll(RagiumBlocks.STORAGE_BLOCKS.values)

            add(RagiumBlocks.SOUL_MAGMA_BLOCK)

            add(RagiumBlocks.SLAG_BLOCK)

            add(RagiumBlocks.SHAFT)
            addAll(RagiumBlocks.GLASSES)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)

            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            addAll(RagiumBlocks.ADDONS)
        }.map(DeferredBlock<*>::getId).forEach(::simpleBlockItem)

        // Machine
        RagiumAPI.machineRegistry.forEachEntries { key: HTMachineKey, content: HTBlockContent?, property: HTPropertyHolder ->
            if (content == null) return@forEachEntries
            val modelId: ResourceLocation =
                key.getProperty().getOrDefault(HTMachinePropertyKeys.MODEL_MAPPER)(key)
            withUncheckedParent(content, modelId)
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.MATERIAL_ITEMS.values)
            add(RagiumItems.BEE_WAX)
            addAll(RagiumItems.OTHER_GEMS)
            addAll(RagiumItems.OTHER_RESOURCES)

            addAll(RagiumItems.FOODS)
            remove(RagiumItems.CHOCOLATE_APPLE)

            add(RagiumItems.FORGE_HAMMER)
            add(RagiumItems.SILKY_PICKAXE)

            add(RagiumItems.DYNAMITE)
            add(RagiumItems.SLOT_LOCK)

            addAll(RagiumItems.CIRCUITS)
            addAll(RagiumItems.PRESS_MOLDS)
            addAll(RagiumItems.RADIOACTIVES)

            addAll(RagiumItems.REAGENTS)
            addAll(RagiumItems.INGREDIENTS)
            remove(RagiumItems.MACHINE_CASING)
            remove(RagiumItems.CHEMICAL_MACHINE_CASING)
            remove(RagiumItems.PRECISION_MACHINE_CASING)
            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.map(ItemLike::asItem)
            .forEach(::basicItem)

        getBuilder(RagiumItems.CHOCOLATE_APPLE)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .itemTexture("layer0", ResourceLocation.withDefaultNamespace("apple"))
            .itemTexture("layer1", RagiumItems.CHOCOLATE_APPLE.id)

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
