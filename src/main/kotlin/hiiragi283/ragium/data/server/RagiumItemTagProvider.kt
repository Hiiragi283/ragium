package hiiragi283.ragium.data.server

import blusunrize.immersiveengineering.ImmersiveEngineering
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.item.HTFuelDrillItem
import mekanism.generators.common.registries.GeneratorsItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import vectorwing.farmersdelight.common.registry.ModBlocks
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Item>(output, Registries.ITEM, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        materialTags()
        foodTags()
        toolTags()
        partTags()
    }

    //    Material    //

    private fun materialTags() {
        RagiumBlocks.ORES.forEach { (_, key: HTMaterialKey, ore: DeferredBlock<out Block>) ->
            val oreTagKey: TagKey<Item> = HTTagPrefix.ORE.createTag(key)

            tag(HTTagPrefix.ORE.commonTagKey)
                .addTag(oreTagKey)

            tag(oreTagKey)
                .addItem(ore)
        }

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            val storageTag: TagKey<Item> = HTTagPrefix.STORAGE_BLOCK.createTag(key)

            tag(HTTagPrefix.STORAGE_BLOCK.commonTagKey)
                .addTag(storageTag)

            tag(storageTag)
                .addItem(storage)
        }

        RagiumItems.MATERIAL_ITEMS.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            val tagKey: TagKey<Item> = prefix.createTag(key)

            tag(prefix.commonTagKey)
                .addTag(tagKey)

            tag(tagKey)
                .add(holder)
        }

        addMaterialTag(HTTagPrefix.DUST, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem_crushed")
        addMaterialTag(HTTagPrefix.GEM, IntegrationMaterials.DARK_GEM, "evilcraft:dark_gem")
        addMaterialTag(HTTagPrefix.GEM, VanillaMaterials.NETHERITE_SCRAP, "netherite_scrap", false)
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, "evilcraft:dark_ore_deepslate")
        addMaterialTag(HTTagPrefix.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, "evilcraft:dark_block")
    }

    private fun addMaterialTag(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        value: String,
        optional: Boolean = true,
    ) {
        tag(prefix.commonTagKey)
            .addTag(prefix.createTag(material))

        tag(prefix.createTag(material))
            .add(DeferredItem.createItem(ResourceLocation.parse(value)), optional)
    }

    //    Food    //

    private fun foodTags() {
        val foods: TagAppender<Item> = tag(Tags.Items.FOODS)
        RagiumItems.FOODS.forEach { foodItem: DeferredItem<out Item> ->
            if (foodItem.get().components().has(DataComponents.FOOD)) {
                foods.add(foodItem)
            }
        }

        tag(RagiumItemTags.DOUGH).add(RagiumItems.DOUGH)
    }

    //    Tool    //

    private fun toolTags() {
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(RagiumItems.FORGE_HAMMER)

        val pickaxe: TagAppender<Item> = tag(ItemTags.PICKAXES).add(RagiumItems.SILKY_PICKAXE)
        val shovel: TagAppender<Item> = tag(ItemTags.SHOVELS)

        RagiumItems.DRILLS.forEach { drill: DeferredItem<HTFuelDrillItem> ->
            pickaxe.add(drill)
            shovel.add(drill)
        }

        tag(
            itemTagKey(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "forge_hammer_tools")),
        ).add(RagiumItems.FORGE_HAMMER)
    }

    //    Part    //

    private fun partTags() {
        tag(Tags.Items.BUCKETS)
            .add(RagiumItems.CRUDE_OIL_BUCKET)

        tag(RagiumItemTags.BASIC_CIRCUIT)
            .add(RagiumItems.BASIC_CIRCUIT)

        tag(RagiumItemTags.ADVANCED_CIRCUIT)
            .add(RagiumItems.ADVANCED_CIRCUIT)

        tag(RagiumItemTags.ELITE_CIRCUIT)
            .add(RagiumItems.ELITE_CIRCUIT)

        tag(RagiumItemTags.ULTIMATE_CIRCUIT)
            .add(RagiumItems.ULTIMATE_CIRCUIT)

        tag(RagiumItemTags.PLASTICS)
            .add(RagiumItems.PLASTIC_PLATE)

        tag(itemTagKey(commonId("plates/plastic")))
            .add(RagiumItems.PLASTIC_PLATE)

        tag(RagiumItemTags.SLAG)
            .add(RagiumItems.SLAG)

        tag(RagiumItemTags.SOLAR_PANELS)
            .add(RagiumItems.SOLAR_PANEL)
            .add(GeneratorsItems.SOLAR_PANEL, true)

        tag(RagiumItemTags.DIRT_SOILS)
            .addItem(Items.FARMLAND)
            .addItem(ModBlocks.RICH_SOIL.get(), true)
            .addItem(ModBlocks.RICH_SOIL_FARMLAND.get(), true)
            .addTag(ItemTags.DIRT)

        tag(RagiumItemTags.MUSHROOM_SOILS)
            .addItem(Items.MYCELIUM)

        tag(RagiumItemTags.NETHER_SOILS)
            .addItem(Items.CRIMSON_NYLIUM)
            .addItem(Items.WARPED_NYLIUM)

        tag(RagiumItemTags.END_SOILS)
            .addTag(Tags.Items.END_STONES)

        tag(RagiumItemTags.GEAR_MOLDS)
            .add(RagiumItems.GEAR_PRESS_MOLD)
            .add(ImmersiveEngineering.rl("mold_gear"), true)

        tag(RagiumItemTags.PLATE_MOLDS)
            .add(RagiumItems.PLATE_PRESS_MOLD)
            .add(ImmersiveEngineering.rl("mold_plate"), true)

        tag(RagiumItemTags.ROD_MOLDS)
            .add(RagiumItems.ROD_PRESS_MOLD)
            .add(ImmersiveEngineering.rl("mold_rod"), true)

        tag(RagiumItemTags.WIRE_MOLDS)
            .add(RagiumItems.WIRE_PRESS_MOLD)
            .add(ImmersiveEngineering.rl("mold_wire"), true)

        val ledBuilder: TagAppender<Item> = tag(RagiumItemTags.LED_BLOCKS)
        RagiumBlocks.LED_BLOCKS.values.forEach(ledBuilder::addItem)
    }
}
