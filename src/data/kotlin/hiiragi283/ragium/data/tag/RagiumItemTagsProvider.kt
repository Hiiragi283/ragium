package hiiragi283.ragium.data.tag

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(fileHelper, output, lookupProvider, RagiumAPI.MOD_ID, blockTags) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> = HiiragiCoreAccess.INSTANCE.registeredContents.items
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.getBucket())
        }
        // Foods
        factory
            .apply(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        val foodsCan: HTTagBuilder<Item> = addTags(factory, Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN)
        HTFoodCanType.entries.forEach(foodsCan::add)

        factory
            .apply(Tags.Items.FOODS_RAW_MEAT)
            .add(items.getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.MEAT))
        factory
            .apply(Tags.Items.FOODS_COOKED_MEAT)
            .add(items.getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.COOKED_MEAT))
        // Others
        factory
            .apply(HiiragiCoreTags.Items.SILICON)
            .add(RagiumItems.CRUDE_SILICON)

        upgradeTargets(factory)
    }

    private fun upgradeTargets(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Group
        factory
            .apply(RagiumTags.Items.GENERATOR_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.BOILER)
        factory
            .apply(RagiumTags.Items.PROCESSOR_UPGRADABLE)
            .addTag(RagiumTags.Items.MACHINE_UPGRADABLE)
            .addTag(RagiumTags.Items.DEVICE_UPGRADABLE)
        factory
            .apply(RagiumTags.Items.MACHINE_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.ASSEMBLER)
            .add(RagiumBlocks.AUTO_CHISEL)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
            .add(RagiumBlocks.PLANTER)
            // Advanced
            .add(RagiumBlocks.FREEZER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.REFINERY)
            .add(RagiumBlocks.WASHER)
            // Elite
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.CHEMICAL_WASHER)
            .add(RagiumBlocks.FLUID_MIXER)
            .add(RagiumBlocks.MIXER)
            // Ultimate
            .add(RagiumBlocks.FLUID_DUPLICATOR)
        factory
            .apply(RagiumTags.Items.DEVICE_UPGRADABLE)
            .add(RagiumBlocks.ENCHANTER)
    }
}
