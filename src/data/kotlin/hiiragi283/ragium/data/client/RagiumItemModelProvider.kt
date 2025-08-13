package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.extension.itemId
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.material.RagiumMaterialType
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

    private fun registerItems() {
        val compounds: Map<HTMaterialType, DeferredItem<*>> = RagiumItems.MATERIALS.row(HTMaterialVariant.COMPOUND)
        val tools: Collection<DeferredItem<*>> = RagiumItems.TOOLS.values

        buildList {
            addAll(RagiumItems.REGISTER.entries)

            removeAll(compounds.values)

            remove(RagiumItems.BLAST_CHARGE)
            removeAll(tools)

            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.map(DeferredItem<*>::getId).forEach(::basicItem)

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
            getBuilder(content.bucketHolder)
                .parent(modelFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        handheldItem(RagiumItems.BLAST_CHARGE.asItem())

        tools.map(DeferredItem<*>::getId).forEach(::handheldItem)
    }
}
