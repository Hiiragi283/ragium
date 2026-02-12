package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTItemTagsProvider(RagiumAPI.MOD_ID, blockTags, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
        // Foods
        factory
            .apply(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        val foodsCan: HTTagBuilder<Item> = addTags(factory, Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN)
        HTFoodCanType.entries.forEach(foodsCan::add)

        val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
        factory
            .apply(Tags.Items.FOODS_RAW_MEAT)
            .add(contents.getItem(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT)!!)
        factory
            .apply(Tags.Items.FOODS_COOKED_MEAT)
            .add(contents.getItem(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT)!!)
        // Others
        RagiumItems.MOLDS.values.forEach(factory.apply(RagiumTags.Items.MOLDS)::add)

        upgradeTargets(factory)
    }

    private fun upgradeTargets(factory: HTTagsProvider.BuilderFactory<Item>) {
        // Group
        factory
            .apply(RagiumTags.Items.GENERATOR_UPGRADABLE)
            .addItem(Items.BARRIER) // TODO
        factory
            .apply(RagiumTags.Items.PROCESSOR_UPGRADABLE)
            .addTag(RagiumTags.Items.MACHINE_UPGRADABLE)
            .addTag(RagiumTags.Items.DEVICE_UPGRADABLE)
        factory
            .apply(RagiumTags.Items.MACHINE_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
            .add(RagiumBlocks.FORMING_PRESS)
            // Heat
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.PYROLYZER)
            // Cool
            .add(RagiumBlocks.SOLIDIFIER)
            // Chemical
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.WASHER)
        factory
            .apply(RagiumTags.Items.DEVICE_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.PLANTER)
            // Enchanting
            .add(RagiumBlocks.ENCHANTER)

        // Specific
        factory
            .apply(RagiumTags.Items.EXTRA_VOIDING_UPGRADABLE)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.CRUSHER)
        factory
            .apply(RagiumTags.Items.EFFICIENT_CRUSHING_UPGRADABLE)
            .add(RagiumBlocks.CRUSHER)
        factory
            .apply(RagiumTags.Items.SMELTING_UPGRADABLE)

        // Storage
        factory
            .apply(RagiumTags.Items.ENERGY_CAPACITY_UPGRADABLE)
            .addTag(RagiumTags.Items.GENERATOR_UPGRADABLE)
            .addTag(RagiumTags.Items.MACHINE_UPGRADABLE)
            // Storage
            .add(RagiumBlocks.BATTERY)
        factory
            .apply(RagiumTags.Items.FLUID_CAPACITY_UPGRADABLE)
            // Generator
            // Machine
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.SOLIDIFIER)
            .add(RagiumBlocks.WASHER)
            // Device
            .add(RagiumBlocks.PLANTER)
            // Storage
            .add(RagiumBlocks.TANK)
        factory
            .apply(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE)
            .add(RagiumBlocks.CRATE)
    }
}
