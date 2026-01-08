package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumTags
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
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, _) ->
            copy(prefix, key)
        }
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        RagiumItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix == HCMaterialPrefixes.GEM || prefix == HCMaterialPrefixes.INGOT) {
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
        addTags(factory, Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN)
            .add(RagiumItems.FISH_CAN)
            .add(RagiumItems.FRUIT_CAN)
            .add(RagiumItems.MEAT_CAN)
            .add(RagiumItems.SOUP_CAN)
        factory
            .apply(Tags.Items.FOODS_RAW_MEAT)
            .add(RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.MEAT))
        factory
            .apply(Tags.Items.FOODS_COOKED_MEAT)
            .add(RagiumItems.MATERIALS.getOrThrow(HCMaterialPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT))
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
            // Advanced
            .add(RagiumBlocks.DRYER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.PYROLYZER)
        factory
            .apply(RagiumTags.Items.DEVICE_UPGRADABLE)
            // Basic
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
            // Device
            .add(RagiumBlocks.PLANTER)
            // Storage
            .add(RagiumBlocks.TANK)
        factory
            .apply(RagiumTags.Items.ITEM_CAPACITY_UPGRADABLE)
            .add(RagiumBlocks.CRATE)
    }
}
