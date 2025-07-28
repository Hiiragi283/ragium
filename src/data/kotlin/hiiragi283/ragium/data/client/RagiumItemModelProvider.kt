package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Supplier

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
            
            remove(RagiumBlocks.EXP_BERRY_BUSH)
            remove(RagiumBlocks.WARPED_WART)
        }.map(Supplier<out Block>::get).forEach(::simpleBlockItem)
        RagiumBlocks.RAGINITE_ORES.addItemModels(this)
        RagiumBlocks.RAGI_CRYSTAL_ORES.addItemModels(this)

        for (sets: HTBlockSet in RagiumBlocks.DECORATIONS) {
            sets.addItemModels(this)
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.REGISTER.entries)

            removeAll(RagiumItems.Compounds.entries.map(HTItemHolderLike::holder))

            remove(RagiumItems.BLAST_CHARGE)
            removeAll(RagiumItems.ForgeHammers.entries.map(HTItemHolderLike::holder))

            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.map(Supplier<out Item>::get).forEach(::basicItem)

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

        // Armors
        RagiumItems.AZURE_STEEL_ARMORS.addItemModels(this)
        RagiumItems.DEEP_STEEL_ARMORS.addItemModels(this)
        // Tools
        RagiumItems.AZURE_STEEL_TOOLS.addItemModels(this)
        RagiumItems.DEEP_STEEL_TOOLS.addItemModels(this)

        handheldItem(RagiumItems.BLAST_CHARGE.asItem())

        RagiumItems.ForgeHammers.entries
            .map(HTItemHolderLike::id)
            .forEach(::handheldItem)
    }
}
