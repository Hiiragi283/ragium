package hiiragi283.ragium.data.server

import aztech.modern_industrialization.items.ForgeTool
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagEntry
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Item>(output, Registries.ITEM, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    lateinit var builder: HTTagBuilder<Item>

    override fun addTags(provider: HolderLookup.Provider) {
        builder = HTTagBuilder(provider.itemLookup())

        materialTags()
        foodTags()
        armorTags()
        toolTags()
        partTags()
        enchantmentTags()

        builder.build { tagKey: TagKey<Item>, entry: TagEntry ->
            tag(tagKey).add(entry)
        }
    }

    fun HTTagBuilder<Item>.addItem(prefix: HTTagPrefix, key: HTMaterialKey, item: Item) {
        addItem(prefix.createTag(key), item)
    }

    fun HTTagBuilder<Item>.addItem(tagKey: TagKey<Item>, item: Item) {
        add(tagKey, item.asHolder())
    }    
    
    //    Material    //

    private fun materialTags() {
        RagiumBlocks.RAGINITE_ORES.appendTags(builder)
        RagiumBlocks.RAGI_CRYSTAL_ORES.appendTags(builder)

        RagiumBlocks.STORAGE_BLOCKS.forEach { (key: HTMaterialKey, storage: DeferredBlock<Block>) ->
            val storageTag: TagKey<Item> = HTTagPrefix.BLOCK.createTag(key)
            builder.addTag(HTTagPrefix.BLOCK.commonTagKey, storageTag)
            builder.add(storageTag, storage.asHolder())
        }

        RagiumItems.MATERIAL_ITEMS.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, holder: DeferredItem<out Item>) ->
            val tagKey: TagKey<Item> = prefix.createTag(key)
            builder.addTag(prefix.commonTagKey, tagKey)
            builder.add(tagKey, holder)
            if (prefix == HTTagPrefix.INGOT) {
                builder.addTag(ItemTags.BEACON_PAYMENT_ITEMS, tagKey)
            }
        }

        builder.addTag(
            HTTagPrefix.GEM.createTag(CommonMaterials.COAL_COKE),
            RagiumItemTags.COAL_COKE,
            HTTagBuilder.DependType.OPTIONAL,
        )

        builder.addItem(HTTagPrefix.BLOCK, VanillaMaterials.AMETHYST, Items.AMETHYST_BLOCK)
        builder.addItem(HTTagPrefix.BLOCK, VanillaMaterials.GLOWSTONE, Items.GLOWSTONE)
        builder.addItem(HTTagPrefix.GEM, VanillaMaterials.COAL, Items.COAL)
        builder.addItem(HTTagPrefix.GEM, VanillaMaterials.NETHERITE_SCRAP, Items.NETHERITE_SCRAP)

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
        addMaterialTag(HTTagPrefix.BLOCK, IntegrationMaterials.DARK_GEM, IntegrationMods.EVC, "dark_block")
        // IE
        addMaterialTag(HTTagPrefix.COIL, CommonMaterials.ELECTRUM, IntegrationMods.IE, "wirecoil_electrum")
        addMaterialTag(HTTagPrefix.COIL, CommonMaterials.STEEL, IntegrationMods.IE, "wirecoil_steel")
        addMaterialTag(HTTagPrefix.COIL, VanillaMaterials.COPPER, IntegrationMods.IE, "wirecoil_copper")
        // MI
        addMaterialTag(HTTagPrefix.GEM, CommonMaterials.COAL_COKE, IntegrationMods.MI, "coke")
    }

    private fun addMaterialTag(
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        mod: IntegrationMods,
        path: String,
        type: HTTagBuilder.DependType = HTTagBuilder.DependType.OPTIONAL,
    ) {
        builder.addTag(prefix.commonTagKey, prefix.createTag(material))
        builder.add(prefix.createTag(material), mod.createItemHolder<Item>(path), type)
    }

    //    Food    //

    private fun foodTags() {
        RagiumItems.FOODS.forEach { foodItem: DeferredItem<out Item> ->
            if (foodItem.get().components().has(DataComponents.FOOD)) {
                builder.add(Tags.Items.FOODS, foodItem)
            }
        }

        builder.addTag(Tags.Items.CROPS, RagiumItemTags.CROPS_WARPED_WART)
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_CHOCOLATE)
        builder.addTag(Tags.Items.FOODS, RagiumItemTags.FOOD_DOUGH)

        builder.add(RagiumItemTags.FOOD_CHOCOLATE, RagiumItems.CHOCOLATE)
        builder.add(
            RagiumItemTags.FOOD_CHOCOLATE,
            ResourceLocation.fromNamespaceAndPath("create", "bar_of_chocolate"),
            HTTagBuilder.DependType.OPTIONAL,
        )

        builder.add(RagiumItemTags.FLOURS, RagiumItems.FLOUR)
        builder.add(RagiumItemTags.FOOD_DOUGH, RagiumItems.DOUGH)

        builder.add(RagiumItemTags.CROPS_WARPED_WART, RagiumItems.WARPED_WART)
    }

    //    Armor    //

    private fun armorTags() {
        builder.add(ItemTags.HEAD_ARMOR_ENCHANTABLE, RagiumItems.DIVING_GOGGLE)
        builder.add(ItemTags.CHEST_ARMOR_ENCHANTABLE, RagiumItems.JETPACK)

        RagiumItems.EMBER_ALLOY_ARMORS.appendTags(builder::add)
        RagiumItems.STEEL_ARMORS.appendTags(builder::add)
    }

    //    Tool    //

    private fun toolTags() {
        builder.add(ForgeTool.TAG, RagiumItems.FORGE_HAMMER)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.FORGE_HAMMER)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.RAGI_LANTERN)
        builder.add(ItemTags.DURABILITY_ENCHANTABLE, RagiumItems.RAGI_SHEARS)
        builder.add(ItemTags.MINING_ENCHANTABLE, RagiumItems.RAGI_SHEARS)
        builder.add(ItemTags.PICKAXES, RagiumItems.FEVER_PICKAXE)
        builder.add(ItemTags.PICKAXES, RagiumItems.SILKY_PICKAXE)
        builder.add(Tags.Items.TOOLS_SHEAR, RagiumItems.RAGI_SHEARS)

        RagiumItems.EMBER_ALLOY_TOOLS.appendTags(builder::add)
        RagiumItems.STEEL_TOOLS.appendTags(builder::add)
    }

    //    Part    //

    @Suppress("DEPRECATION")
    private fun partTags() {
        builder.add(Tags.Items.BUCKETS, RagiumItems.CRUDE_OIL_BUCKET)
        builder.add(Tags.Items.BUCKETS, RagiumItems.HONEY_BUCKET)

        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.SLIMEBALLS, RagiumItems.TAR)

        builder.addItem(RagiumItemTags.PAPER, Items.PAPER)

        builder.add(RagiumItemTags.PLASTICS, RagiumItems.PLASTIC_PLATE)
        builder.add(itemTagKey(commonId("plates/plastic")), RagiumItems.PLASTIC_PLATE)

        builder.add(RagiumItemTags.CIRCUIT_BASIC, RagiumItems.BASIC_CIRCUIT)
        builder.add(RagiumItemTags.CIRCUIT_ADVANCED, RagiumItems.ADVANCED_CIRCUIT)
        builder.add(RagiumItemTags.CIRCUIT_ELITE, RagiumItems.ELITE_CIRCUIT)
        builder.add(RagiumItemTags.CIRCUIT_ULTIMATE, RagiumItems.ULTIMATE_CIRCUIT)

        builder.add(RagiumItemTags.SLAG, RagiumItems.SLAG)

        builder.addItem(RagiumItemTags.DIRT_SOILS, Items.FARMLAND)
        builder.add(RagiumItemTags.DIRT_SOILS, IntegrationMods.FD, "rich_soil", HTTagBuilder.DependType.OPTIONAL)
        // builder.add(RagiumItemTags.DIRT_SOILS, ModBlocks.RICH_SOIL_FARMLAND.get(), HTTagBuilder.DependType.OPTIONAL)
        builder.addTag(RagiumItemTags.DIRT_SOILS, ItemTags.DIRT)

        builder.addItem(RagiumItemTags.MUSHROOM_SOILS, Items.MYCELIUM)

        builder.addItem(RagiumItemTags.NETHER_SOILS, Items.CRIMSON_NYLIUM)
        builder.addItem(RagiumItemTags.NETHER_SOILS, Items.WARPED_NYLIUM)

        builder.addTag(RagiumItemTags.END_SOILS, Tags.Items.END_STONES)

        builder.add(RagiumItemTags.MOLD_BALL, RagiumItems.BALL_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_BLOCK, RagiumItems.BLOCK_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_GEAR, IntegrationMods.IE, "mold_gear", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLD_GEAR, RagiumItems.GEAR_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_INGOT, RagiumItems.INGOT_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_PLATE, IntegrationMods.IE, "mold_plate", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLD_PLATE, RagiumItems.PLATE_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_ROD, IntegrationMods.IE, "mold_rod", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLD_ROD, RagiumItems.ROD_PRESS_MOLD)
        builder.add(RagiumItemTags.MOLD_WIRE, IntegrationMods.IE, "mold_wire", HTTagBuilder.DependType.OPTIONAL)
        builder.add(RagiumItemTags.MOLD_WIRE, RagiumItems.WIRE_PRESS_MOLD)

        RagiumBlocks.LED_BLOCKS.values.forEach { builder.add(RagiumItemTags.LED_BLOCKS, it.asHolder()) }
    }

    //    Enchantment    //

    private fun enchantmentTags() {
        buildList {
            add(RagiumBlocks.MANUAL_GRINDER)
            add(RagiumBlocks.PRIMITIVE_BLAST_FURNACE)

            addAll(HTMachineType.getBlocks())
        }.map(ItemLike::asHolder)
            .forEach { holder: Holder.Reference<Item> ->
                builder.add(ItemTags.DURABILITY_ENCHANTABLE, holder)
                builder.add(ItemTags.MINING_ENCHANTABLE, holder)
                builder.add(ItemTags.MINING_LOOT_ENCHANTABLE, holder)
                builder.add(RagiumItemTags.CAPACITY_ENCHANTABLE, holder)
            }

        for (block: DeferredBlock<out HTEntityBlock> in buildList {
            addAll(RagiumBlocks.CRATES.values)
            addAll(RagiumBlocks.DRUMS.values)
        }) {
            builder.add(RagiumItemTags.CAPACITY_ENCHANTABLE, block.asHolder())
        }
    }
}
