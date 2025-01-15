package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
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
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)

            addAll(RagiumBlocks.Drums.entries)
        }.map(HTBlockContent::id).forEach(::simpleBlockItem)

        RagiumAPI.getInstance().machineRegistry.blocks.forEach { holder: DeferredBlock<HTMachineBlock> ->
            val id: ResourceLocation = holder.id
            val modelBuilder: ItemModelBuilder = withExistingParent(id.toString(), RagiumAPI.id("block/machine_front"))
            HTMachineTier.entries.forEach { tier: HTMachineTier ->
                val value: Float = when (tier) {
                    HTMachineTier.PRIMITIVE -> 0.2f
                    HTMachineTier.SIMPLE -> 0.4f
                    HTMachineTier.BASIC -> 0.6f
                    HTMachineTier.ADVANCED -> 0.8f
                    HTMachineTier.ELITE -> 1f
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

            addAll(RagiumItems.Circuits.entries)

            addAll(RagiumItems.FOODS)

            addAll(RagiumItems.Circuits.entries)
            addAll(RagiumItems.PressMolds.entries)
            addAll(RagiumItems.Catalysts.entries)
            addAll(RagiumItems.INGREDIENTS)
            addAll(RagiumItems.Radioactives.entries)
        }.map(ItemLike::asItem)
            .forEach(::basicItem)
    }
}
