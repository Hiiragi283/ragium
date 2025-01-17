package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.getBuilder
import hiiragi283.ragium.data.itemTexture
import hiiragi283.ragium.data.withExistingParent
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
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

            addAll(RagiumBlocks.Drums.entries)

            addAll(RagiumBlocks.LEDBlocks.entries)
        }.map(HTBlockContent::id).forEach(::simpleBlockItem)

        buildList {
            add(RagiumBlocks.SHAFT)

            add(RagiumBlocks.PLASTIC_BLOCK)

            add(RagiumBlocks.MANUAL_GRINDER)

            add(RagiumBlocks.ENERGY_NETWORK_INTERFACE)
        }.map(DeferredBlock<*>::getId).forEach(::simpleBlockItem)

        RagiumAPI.getInstance().machineRegistry.blocks.forEach { holder: DeferredBlock<HTMachineBlock> ->
            val id: ResourceLocation = holder.id
            val modelBuilder: ItemModelBuilder = withExistingParent(id, RagiumAPI.id("block/machine_front"))
            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                val value: Float = when (tier) {
                    HTMachineTier.BASIC -> 0.4f
                    HTMachineTier.ADVANCED -> 0.6f
                    HTMachineTier.ELITE -> 0.8f
                    HTMachineTier.ULTIMATE -> 1f
                }
                modelBuilder
                    .override()
                    .predicate(RagiumAPI.id("machine_tier"), value)
                    .model(ModelFile.UncheckedModelFile(tier.getHull().blockId))
            }
        }
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.MATERIALS)

            addAll(RagiumItems.FOODS)
            remove(RagiumItems.CHOCOLATE_APPLE)

            add(RagiumItems.FORGE_HAMMER)

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
