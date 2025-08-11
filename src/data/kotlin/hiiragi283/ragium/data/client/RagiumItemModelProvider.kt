package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
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
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import kotlin.enums.enumEntries

class RagiumItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private val generated: ModelFile = modelFile(vanillaId("item/generated"))

    inline fun <reified I> MutableList<DeferredBlock<*>>.removeBlocks() where I : HTBlockHolderLike, I : Enum<I> {
        removeAll { holder: DeferredBlock<*> ->
            for (entries: I in enumEntries<I>()) {
                if (entries.key?.let(holder::`is`) ?: false) {
                    return@removeAll true
                }
            }
            false
        }
    }

    private fun registerBlocks() {
        // Blocks
        buildList {
            addAll(RagiumBlocks.REGISTER.entries)

            remove(RagiumBlocks.EXP_BERRY_BUSH)
            remove(RagiumBlocks.WARPED_WART)

            removeBlocks<RagiumBlocks.Walls>()
            removeBlocks<RagiumBlocks.Dynamos>()
        }.map(DeferredBlock<*>::getId).forEach(::simpleBlockItem)

        for (wall: RagiumBlocks.Walls in RagiumBlocks.Walls.entries) {
            withExistingParent(wall.id.path, vanillaId("block/wall_inventory"))
                .texture("wall", wall.variant.textureName)
        }
    }

    inline fun <reified I> MutableList<DeferredItem<*>>.removeItems() where I : HTItemHolderLike, I : Enum<I> {
        removeAll { holder: DeferredItem<*> ->
            for (entries: I in enumEntries<I>()) {
                if (entries.key?.let(holder::`is`) ?: false) {
                    return@removeAll true
                }
            }
            false
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            removeItems<RagiumItems.Compounds>()

            remove(RagiumItems.BLAST_CHARGE)
            removeItems<RagiumItems.ForgeHammers>()
            removeItems<RagiumItems.AzureSteelTools>()
            removeItems<RagiumItems.DeepSteelTools>()

            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.map(DeferredItem<*>::getId).forEach(::basicItem)

        for (compound: RagiumItems.Compounds in RagiumItems.Compounds.entries) {
            val baseId: String = when (compound) {
                RagiumItems.Compounds.RAGI_ALLOY -> "copper_ingot"
                RagiumItems.Compounds.ADVANCED_RAGI_ALLOY -> "gold_ingot"
                RagiumItems.Compounds.AZURE_STEEL -> "iron_ingot"
            }
            val layerId: ResourceLocation = when (compound) {
                RagiumItems.Compounds.ADVANCED_RAGI_ALLOY -> RagiumItems.Compounds.RAGI_ALLOY
                else -> compound
            }.itemId
            getBuilder(compound.id.path)
                .parent(generated)
                .texture("layer0", "minecraft:item/$baseId")
                .texture("layer1", layerId)
        }

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getBuilder(content.bucketHolder)
                .parent(modelFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        handheldItem(RagiumItems.BLAST_CHARGE.asItem())

        buildList {
            addAll(RagiumItems.ForgeHammers.entries)
            addAll(RagiumItems.AzureSteelTools.entries)
            addAll(RagiumItems.DeepSteelTools.entries)
        }.map(HTItemHolderLike::id).forEach(::handheldItem)
    }
}
