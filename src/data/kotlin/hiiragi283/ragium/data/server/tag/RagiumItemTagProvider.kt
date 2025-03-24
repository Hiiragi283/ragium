package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.HTTagProvider
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>, helper: ExistingFileHelper) :
    HTTagProvider<Item>(Registries.ITEM, output, provider, helper) {
    override fun addTagsInternal(builder: HTTagBuilder<Item>, provider: HolderLookup.Provider) {
        material(builder)
        food(builder)
        // armorTags(builder)
        // toolTags(builder)
        category(builder)
        enchantment(builder)
    }

    fun HTTagBuilder<Item>.addItem(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemLike) {
        addItem(prefix.createTag(key), item)
    }

    fun HTTagBuilder<Item>.addItem(tagKey: TagKey<Item>, item: ItemLike) {
        add(tagKey, item.asItemHolder())
    }

    //    Material    //

    private fun material(builder: HTTagBuilder<Item>) {
        RagiumBlocks.RAGINITE_ORES.appendItemTags(builder)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendItemTags(builder)

        fun register(entries: List<HTMaterialItemLike>) {
            for (item: HTMaterialItemLike in entries) {
                val prefix: HTTagPrefix = item.prefix
                val materialTag: TagKey<Item> = prefix.createTag(item.key)
                builder.addTag(prefix.commonTagKey, materialTag)
                builder.addItem(materialTag, item)
            }
        }

        register(RagiumBlocks.StorageBlocks.entries)
        register(RagiumItems.Dusts.entries)
        register(RagiumItems.Ingots.entries)
        register(RagiumItems.RawResources.entries)
        register(RagiumItems.MekResources.entries)

        builder.addTag(
            HTTagPrefix.GEM.createTag(CommonMaterials.COAL_COKE),
            RagiumItemTags.COAL_COKE,
            HTTagBuilder.DependType.OPTIONAL,
        )

        builder.addItem(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.AMETHYST, Items.AMETHYST_BLOCK)
        builder.addItem(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.GLOWSTONE, Items.GLOWSTONE)
        builder.addItem(HTTagPrefix.GEM, VanillaMaterials.COAL, Items.COAL)
        builder.addItem(HTTagPrefix.GEM, VanillaMaterials.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)

        fun addMaterialTag(
            prefix: HTTagPrefix,
            material: HTMaterialKey,
            mod: IntegrationMods,
            path: String,
            type: HTTagBuilder.DependType = HTTagBuilder.DependType.OPTIONAL,
        ) {
            builder.addTag(prefix.commonTagKey, prefix.createTag(material))
            builder.add(prefix.createTag(material), mod.createItemHolder<Item>(path), type)
        }

        // EIO
        addMaterialTag(HTTagPrefix.GEAR, IntegrationMaterials.ENERGETIC_ALLOY, IntegrationMods.EIO, "energized_gear")
        addMaterialTag(HTTagPrefix.GEAR, IntegrationMaterials.VIBRANT_ALLOY, IntegrationMods.EIO, "vibrant_gear")
        // Create
        addMaterialTag(HTTagPrefix.GEM, IntegrationMaterials.ROSE_QUARTZ, IntegrationMods.CREATE, "rose_quartz")
        addMaterialTag(HTTagPrefix.INGOT, IntegrationMaterials.ANDESITE_ALLOY, IntegrationMods.CREATE, "andesite_alloy")
        // Evil Craft
        addMaterialTag(HTTagPrefix.DUST, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem_crushed")
        addMaterialTag(HTTagPrefix.GEM, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_gem")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore")
        addMaterialTag(HTTagPrefix.ORE, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_ore_deepslate")
        addMaterialTag(HTTagPrefix.STORAGE_BLOCK, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_block")
        // MI
        addMaterialTag(HTTagPrefix.GEM, CommonMaterials.COAL_COKE, IntegrationMods.MI, "coke")
    }

    //    Food    //

    private fun food(builder: HTTagBuilder<Item>) {
        // Crop
        builder.addTag(Tags.Items.CROPS, RagiumItemTags.CROPS_WARPED_WART)
        builder.add(RagiumItemTags.CROPS_WARPED_WART, RagiumItems.WARPED_WART)
        // Food
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_BUTTER)
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_CHEESE)
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_CHOCOLATE)
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_DOUGH)

        builder.add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_PIECE)
        builder.add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        builder.add(Tags.Items.FOODS, RagiumItems.CHOCOLATE_APPLE)
        builder.add(Tags.Items.FOODS, RagiumItems.CHOCOLATE_BREAD)
        builder.add(Tags.Items.FOODS, RagiumItems.CHOCOLATE_COOKIE)
        builder.add(Tags.Items.FOODS, RagiumItems.MEAT_INGOT)
        builder.add(Tags.Items.FOODS, RagiumItems.COOKED_MEAT_INGOT)
        builder.add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.MEAT_SANDWICH)
        builder.add(Tags.Items.FOODS, RagiumItems.WARPED_WART)
        builder.add(Tags.Items.FOODS, RagiumItems.AMBROSIA)

        builder.add(RagiumItemTags.FOOD_BUTTER, RagiumItems.BUTTER)

        builder.add(RagiumItemTags.FOOD_CHEESE, RagiumItems.CHEESE)

        builder.add(RagiumItemTags.FOOD_CHOCOLATE, RagiumItems.CHOCOLATE)
        builder.add(
            RagiumItemTags.FOOD_CHOCOLATE,
            ResourceLocation.fromNamespaceAndPath("create", "bar_of_chocolate"),
            HTTagBuilder.DependType.OPTIONAL,
        )

        builder.add(RagiumItemTags.FLOURS, RagiumItems.FLOUR)
        builder.add(RagiumItemTags.FOOD_DOUGH, RagiumItems.DOUGH)
    }

    //    Armor    //

    /*private fun armorTags(builder: HTTagBuilder<Item>) {
        builder.add(ItemTags.HEAD_ARMOR_ENCHANTABLE, RagiumItems.DIVING_GOGGLE)
        builder.add(ItemTags.CHEST_ARMOR_ENCHANTABLE, RagiumItems.JETPACK)
        builder.add(ItemTags.LEG_ARMOR_ENCHANTABLE, RagiumItems.CLIMBING_LEGGINGS)
        builder.add(ItemTags.FOOT_ARMOR_ENCHANTABLE, RagiumItems.SLIME_BOOTS)

        RagiumItems.AZURE_STEEL_ARMORS.appendItemTags(builder)
        RagiumItems.DURALUMIN_ARMORS.appendItemTags(builder)
    }

    //    Tool    //

    private fun toolTags(builder: HTTagBuilder<Item>) {
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.RAGI_LANTERN)
        builder.add(ItemTags.PICKAXES, RagiumItems.FEVER_PICKAXE)
        builder.add(ItemTags.PICKAXES, RagiumItems.SILKY_PICKAXE)
        builder.addTag(ForgeTool.TAG, RagiumItemTags.TOOLS_FORGE_HAMMER)
        builder.addTag(ItemTags.DURABILITY_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)
        builder.addTag(ItemTags.MINING_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)
        builder.addTag(ItemTags.MINING_LOOT_ENCHANTABLE, RagiumItemTags.TOOLS_FORGE_HAMMER)

        RagiumItems.RAGI_ALLOY_TOOLS.appendItemTags(builder)
        RagiumItems.AZURE_STEEL_TOOLS.appendItemTags(builder)
        RagiumItems.DURALUMIN_TOOLS.appendItemTags(builder)

        for (dynamite: DeferredItem<out HTThrowableItem> in RagiumItems.DYNAMITES) {
            builder.add(RagiumItemTags.DYNAMITES, dynamite)
        }
    }*/

    //    Category    //

    @Suppress("DEPRECATION")
    private fun category(builder: HTTagBuilder<Item>) {
        RagiumBlocks.RAGI_BRICK_SETS.appendItemTags(builder)
        RagiumBlocks.AZURE_TILE_SETS.appendItemTags(builder)
        RagiumBlocks.EMBER_STONE_SETS.appendItemTags(builder)
        RagiumBlocks.PLASTIC_SETS.appendItemTags(builder)
        RagiumBlocks.BLUE_NETHER_BRICK_SETS.appendItemTags(builder)

        for (block: DeferredBlock<*> in RagiumBlocks.LED_BLOCKS.values) {
            builder.addItem(RagiumItemTags.LED_BLOCKS, block)
        }

        RagiumItems.AZURE_STEEL_ARMORS.appendItemTags(builder)

        RagiumItems.RAGI_ALLOY_TOOLS.appendItemTags(builder)
        RagiumItems.AZURE_STEEL_TOOLS.appendItemTags(builder)

        builder.add(RagiumItemTags.BUCKETS_CRUDE_OIL, RagiumItems.CRUDE_OIL_BUCKET)
        builder.addTag(Tags.Items.BUCKETS, RagiumItemTags.BUCKETS_CRUDE_OIL)

        builder.add(itemTagKey(commonId("plates/plastic")), RagiumItems.PLASTIC_PLATE)
        builder.add(RagiumItemTags.PLASTICS, RagiumItems.PLASTIC_PLATE)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.SLIMEBALLS, RagiumItems.TAR)
        builder.addItem(RagiumItemTags.GLASS_BLOCKS_OBSIDIAN, RagiumBlocks.OBSIDIAN_GLASS)
        builder.addItem(RagiumItemTags.GLASS_BLOCKS_QUARTZ, RagiumBlocks.QUARTZ_GLASS)
        builder.addItem(RagiumItemTags.PAPER, Items.PAPER)
        builder.addTag(Tags.Items.GLASS_BLOCKS, RagiumItemTags.GLASS_BLOCKS_OBSIDIAN)
        builder.addTag(Tags.Items.GLASS_BLOCKS, RagiumItemTags.GLASS_BLOCKS_QUARTZ)

        // builder.add(RagiumItemTags.DIRT_SOILS, IntegrationMods.FD, "rich_soil", HTTagBuilder.DependType.OPTIONAL)
        builder.addItem(RagiumItemTags.DIRT_SOILS, Items.DIRT)
        builder.addItem(RagiumItemTags.DIRT_SOILS, Items.FARMLAND)
        builder.addItem(RagiumItemTags.DIRT_SOILS, Items.GRASS_BLOCK)
        builder.addItem(RagiumItemTags.MUSHROOM_SOILS, Items.MYCELIUM)
        builder.addItem(RagiumItemTags.NETHER_SOILS, Items.CRIMSON_NYLIUM)
        builder.addItem(RagiumItemTags.NETHER_SOILS, Items.WARPED_NYLIUM)
        builder.addTag(RagiumItemTags.END_SOILS, Tags.Items.END_STONES)

        for (mold: RagiumItems.Molds in RagiumItems.Molds.entries) {
            builder.addTag(RagiumItemTags.MOLDS, mold.tagKey)
            builder.add(mold.tagKey, mold.holder)
        }
    }

    //    Part    //

    /*private fun partTags(builder: HTTagBuilder<Item>) {
        builder.add(RagiumItemTags.MOLDS_BALL, RagiumItems.BALL_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_BLOCK, RagiumItems.BLOCK_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_GEAR, IntegrationMods.IE, "mold_gear", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLDS_GEAR, RagiumItems.GEAR_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_INGOT, RagiumItems.INGOT_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_PLATE, IntegrationMods.IE, "mold_plate", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLDS_PLATE, RagiumItems.PLATE_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_ROD, IntegrationMods.IE, "mold_rod", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLDS_ROD, RagiumItems.ROD_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLDS_WIRE, IntegrationMods.IE, "mold_wire", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLDS_WIRE, RagiumItems.WIRE_PRESS_MOLD)

        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_BALL)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_BLOCK)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_GEAR)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_INGOT)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_PLATE)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_ROD)
        builder.addTag(RagiumItemTags.MOLDS, RagiumItemTags.MOLDS_WIRE)
    }*/

    //    Enchantment    //

    private fun enchantment(builder: HTTagBuilder<Item>) {
        for (block: DeferredBlock<*> in RagiumBlocks.MACHINES) {
            builder.addItem(ItemTags.DURABILITY_ENCHANTABLE, block)
            builder.addItem(ItemTags.MINING_ENCHANTABLE, block)
            builder.addItem(ItemTags.MINING_LOOT_ENCHANTABLE, block)
            builder.addItem(RagiumItemTags.CAPACITY_ENCHANTABLE, block)
        }
    }
}
