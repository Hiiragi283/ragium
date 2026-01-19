package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumTags
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTItemTagsProvider(RagiumAPI.MOD_ID, blockTags, context) {
    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        copyTags()

        material(factory)
        misc(factory)
    }

    //    Copy    //

    private fun copyTags() {
        // Material
        HTMaterialContentsAccess.INSTANCE.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, _) ->
            if (key.getNamespace() != modId) return@forEach
            copy(prefix, key)
        }
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        HTMaterialContentsAccess.INSTANCE.getItemTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTIdLike) ->
            if (key.getNamespace() != modId) return@forEach
            addMaterial(factory, prefix, key).add(item)
            if (prefix == CommonTagPrefixes.GEM || prefix == CommonTagPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
        }
    }

    //    Misc    //

    private fun misc(factory: BuilderFactory<Item>) {
        // Foods
        factory
            .apply(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)
        val foodsCan: HTTagBuilder<Item> = addTags(factory, Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN)
        HTFoodCanType.entries.forEach(foodsCan::add)
        factory
            .apply(Tags.Items.FOODS_RAW_MEAT)
            .add(HTMaterialContentsAccess.INSTANCE.getItemOrThrow(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT))
        factory
            .apply(Tags.Items.FOODS_COOKED_MEAT)
            .add(HTMaterialContentsAccess.INSTANCE.getItemOrThrow(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT))
        // Others
        RagiumItems.MOLDS.values.forEach(factory.apply(RagiumTags.Items.MOLDS)::add)

        upgradeTargets(factory)
    }

    private fun upgradeTargets(factory: BuilderFactory<Item>) {
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
            .add(RagiumBlocks.FORMING_PRESS)
            // Advanced
            .add(RagiumBlocks.DRYER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.SOLIDIFIER)
        factory
            .apply(RagiumTags.Items.DEVICE_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.FERMENTER)
            .add(RagiumBlocks.PLANTER)

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
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.DRYER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.PYROLYZER)
            .add(RagiumBlocks.SOLIDIFIER)
            // Device
            .add(RagiumBlocks.FERMENTER)
            .add(RagiumBlocks.PLANTER)
            // Storage
            .add(RagiumBlocks.TANK)
        factory
            .apply(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE)
            .add(RagiumBlocks.CRATE)
    }
}
