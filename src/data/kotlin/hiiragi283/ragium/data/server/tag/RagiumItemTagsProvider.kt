package hiiragi283.ragium.data.server.tag

import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.data.tag.HTTagBuilder
import hiiragi283.ragium.api.data.tag.HTTagDependType
import hiiragi283.ragium.api.data.tag.HTTagsProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.variant.HTToolVariant
import hiiragi283.ragium.common.integration.RagiumCreateAddon
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.integration.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.integration.RagiumMekanismAddon
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.HTKitchenKnifeToolVariant
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.common.variant.HTSandPaperToolVariant
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import io.wispforest.accessories.api.data.AccessoriesTags
import me.desht.pneumaticcraft.api.data.PneumaticCraftTags
import mekanism.common.registries.MekanismItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagBuilder
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import rearth.oritech.init.ItemContent
import vectorwing.farmersdelight.common.tag.ModTags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(private val blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTTagsProvider<Item>(Registries.ITEM, context) {
    override fun addTags(builder: HTTagBuilder<Item>) {
        copy()

        material(builder)
        food(builder)
        categories(builder)

        accessories(builder)
        create(builder)
        pneumatic(builder)
    }

    //    Copy    //

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    private fun copy() {
        copy(Tags.Blocks.ORES, Tags.Items.ORES)
        copy(Tags.Blocks.ORES_IN_GROUND_STONE, Tags.Items.ORES_IN_GROUND_STONE)
        copy(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, Tags.Items.ORES_IN_GROUND_DEEPSLATE)
        copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK)
        copy(RagiumCommonTags.Blocks.ORES_IN_GROUND_END_STONE, RagiumCommonTags.Items.ORES_IN_GROUND_END_STONE)

        for (key: HTMaterialKey in RagiumBlocks.ORES.columnKeys) {
            copy(CommonMaterialPrefixes.ORE, key)
        }
        copy(RagiumCommonTags.Blocks.ORES_DEEP_SCRAP, RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        RagiumBlocks.MATERIALS.rowKeys.forEach(::copy)
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, _) ->
            copy(prefix, key)
        }
        for (key: HTMaterialKey in RagiumBlockTagsProvider.VANILLA_STORAGE_BLOCKS.keys) {
            copy(CommonMaterialPrefixes.STORAGE_BLOCK, key)
        }

        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
        copy(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)

        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)
        copy(RagiumModTags.Blocks.LED_BLOCKS, RagiumModTags.Items.LED_BLOCKS)

        copy(Tags.Blocks.CLUSTERS, Tags.Items.CLUSTERS)
        copy(Tags.Blocks.BUDDING_BLOCKS, Tags.Items.BUDDING_BLOCKS)

        copy(RagiumModTags.Blocks.WIP, RagiumModTags.Items.WIP)
    }

    private fun copy(prefix: HTPrefixLike) {
        copy(prefix.createCommonTagKey(Registries.BLOCK), prefix.createCommonTagKey(Registries.ITEM))
    }

    private fun copy(prefix: HTPrefixLike, material: HTMaterialLike) {
        copy(prefix.blockTagKey(material), prefix.itemTagKey(material))
    }

    private fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Material    //

    private fun material(builder: HTTagBuilder<Item>) {
        fromTable(builder, RagiumItems.MATERIALS)
        fromMap(builder, CommonMaterialPrefixes.CIRCUIT, RagiumItems.CIRCUITS)
        // Fuels
        builder.addMaterial(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL, Items.COAL.toHolderLike())
        builder.addMaterial(CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, Items.CHARCOAL.toHolderLike())
        builder.addMaterial(
            CommonMaterialPrefixes.FUEL,
            CommonMaterialKeys.COAL_COKE,
            RagiumCommonTags.Items.COAL_COKE,
            HTTagDependType.OPTIONAL,
        )
        // Gems
        builder.addMaterial(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO, Items.ECHO_SHARD.toHolderLike())

        // Scraps
        builder.addMaterial(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, Items.NETHERITE_SCRAP.toHolderLike())
        // Mekanism Addon
        fromTable(builder, RagiumMekanismAddon.MATERIAL_ITEMS, HTTagDependType.OPTIONAL)
    }

    companion object {
        @JvmField
        val MATERIAL_TAG: Map<HTMaterialPrefix, TagKey<Item>> = mapOf(
            CommonMaterialPrefixes.GEM to ItemTags.BEACON_PAYMENT_ITEMS,
            CommonMaterialPrefixes.INGOT to ItemTags.BEACON_PAYMENT_ITEMS,
            CommonMaterialPrefixes.FUEL to ItemTags.COALS,
        ).mapKeys { (prefix: CommonMaterialPrefixes, _) -> prefix.asMaterialPrefix() }
    }

    private fun fromMap(builder: HTTagBuilder<Item>, prefix: HTPrefixLike, map: Map<out HTMaterialLike, HTHolderLike>) {
        for ((material: HTMaterialLike, item: HTHolderLike) in map) {
            builder.addMaterial(prefix, material, item)
            val customTag: TagKey<Item> = MATERIAL_TAG[prefix] ?: continue
            builder.addTag(customTag, prefix, material)
        }
    }

    private fun fromTable(
        builder: HTTagBuilder<Item>,
        table: ImmutableTable<out HTPrefixLike, out HTMaterialLike, out HTHolderLike>,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ) {
        table.forEach { (prefix: HTPrefixLike, key: HTMaterialLike, item: HTHolderLike) ->
            builder.addMaterial(prefix, key, item, type)
            val customTag: TagKey<Item> = MATERIAL_TAG[prefix] ?: return@forEach
            builder.addTag(customTag, prefix, key, type)
        }
    }

    //    Foods    //

    private fun food(builder: HTTagBuilder<Item>) {
        val foodsFruit = HTMaterialPrefix("food/fruit", "c:foods/fruit", "c:foods/%s")

        fun ingot(key: HTMaterialLike): TagKey<Item> = builder.createTag(CommonMaterialPrefixes.INGOT, key)

        // Crop
        builder.addMaterial(CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART, RagiumBlocks.WARPED_WART)
        // Food
        builder.addTag(Tags.Items.FOODS, CommonMaterialPrefixes.JAM)
        builder.add(Tags.Items.FOODS, RagiumItems.AMBROSIA)
        builder.add(Tags.Items.FOODS, RagiumItems.CANNED_COOKED_MEAT)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM)
        builder.add(Tags.Items.FOODS, RagiumItems.ICE_CREAM_SODA)
        builder.add(Tags.Items.FOODS, RagiumItems.MELON_PIE)
        builder.add(Tags.Items.FOODS, RagiumItems.SWEET_BERRIES_CAKE_SLICE)
        builder.add(Tags.Items.FOODS, RagiumBlocks.WARPED_WART)

        builder.add(Tags.Items.FOODS_BERRY, RagiumBlocks.EXP_BERRIES)
        builder.add(Tags.Items.FOODS_GOLDEN, RagiumItems.FEVER_CHERRY)

        builder.add(Tags.Items.FOODS_FRUIT, RagiumItems.FEVER_CHERRY)

        builder.addTag(ItemTags.MEAT, CommonMaterialPrefixes.INGOT, FoodMaterialKeys.COOKED_MEAT)
        builder.addTag(ItemTags.MEAT, CommonMaterialPrefixes.INGOT, FoodMaterialKeys.RAW_MEAT)

        builder.addTag(RagiumModTags.Items.RAW_MEAT, Tags.Items.FOODS_RAW_MEAT)
        builder.addTag(RagiumModTags.Items.RAW_MEAT, Tags.Items.FOODS_RAW_FISH)
        builder.add(RagiumModTags.Items.RAW_MEAT, Items.ROTTEN_FLESH.toHolderLike())

        builder.addMaterial(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.APPLE, Items.APPLE.toHolderLike())
        builder.addMaterial(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.CHOCOLATE, ingot(FoodMaterialKeys.CHOCOLATE))
        builder.addMaterial(foodsFruit, FoodMaterialKeys.RAGI_CHERRY, RagiumItems.RAGI_CHERRY)
        builder.addMaterial(foodsFruit, FoodMaterialKeys.RAGI_CHERRY, RagiumItems.RAGI_CHERRY_PULP)

        builder.addMaterial(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAW_MEAT, ingot(FoodMaterialKeys.RAW_MEAT))
        builder.addMaterial(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.COOKED_MEAT, ingot(FoodMaterialKeys.COOKED_MEAT))
        // Delight
        builder.add(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_PIE, HTTagDependType.OPTIONAL)
        builder.add(Tags.Items.FOODS_EDIBLE_WHEN_PLACED, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK, HTTagDependType.OPTIONAL)

        builder.addMaterial(CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY, RagiumItems.RAGI_CHERRY_JAM)
        builder.add(ModTags.MEALS, RagiumItems.RAGI_CHERRY_TOAST)
        builder.add(ModTags.FEASTS, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK, HTTagDependType.OPTIONAL)
    }

    //    Categories    //

    private fun categories(builder: HTTagBuilder<Item>) {
        builder.add(RagiumModTags.Items.IGNORED_IN_RECIPES, RagiumItems.SLOT_COVER)

        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC, ItemTags.SMELTS_TO_GLASS)

        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.CINNABAR)
        builder.addTag(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED, ItemTags.SOUL_FIRE_BASE_BLOCKS)

        // Enchantments
        buildList {
            addAll(RagiumBlocks.CRATES.values)
            addAll(RagiumBlocks.DRUMS.values)
        }.forEach { variant: HTHolderLike ->
            builder.add(RagiumModTags.Items.CAPACITY_ENCHANTABLE, variant)
        }
        builder.add(RagiumModTags.Items.CAPACITY_ENCHANTABLE, RagiumItems.DRILL)
        builder.add(RagiumModTags.Items.CAPACITY_ENCHANTABLE, RagiumItems.TELEPORT_KEY)
        builder.add(RagiumModTags.Items.RANGE_ENCHANTABLE, RagiumItems.ADVANCED_MAGNET)
        builder.add(RagiumModTags.Items.RANGE_ENCHANTABLE, RagiumItems.MAGNET)
        builder.addTag(RagiumModTags.Items.STRIKE_ENCHANTABLE, ItemTags.AXES)

        builder.addTag(Tags.Items.ENCHANTABLES, RagiumModTags.Items.CAPACITY_ENCHANTABLE)
        builder.addTag(Tags.Items.ENCHANTABLES, RagiumModTags.Items.RANGE_ENCHANTABLE)
        builder.addTag(Tags.Items.ENCHANTABLES, RagiumModTags.Items.STRIKE_ENCHANTABLE)
        // Equipments
        buildTable {
            putAll(RagiumItems.ARMORS)
            putAll(RagiumItems.TOOLS)
        }.forEach { (variant: HTToolVariant, _, item: HTHolderLike) ->
            for (tagKey: TagKey<Item> in variant.tagKeys) {
                builder.add(tagKey, item)
            }
            if (variant == VanillaToolVariant.PICKAXE) {
                builder.add(ItemTags.CLUSTER_MAX_HARVESTABLES, item)
            }
        }

        builder.add(Tags.Items.TOOLS_WRENCH, RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY))

        builder.add(RagiumModTags.Items.TOOLS_DRILL, RagiumItems.DRILL)

        builder.addTool(HTKitchenKnifeToolVariant, RagiumKaleidoCookeryAddon.KNIFE_MAP.values)
        builder.addTool(HTKnifeToolVariant, RagiumDelightAddon.KNIFE_MAP.values)

        fun setupTool(tagKey: TagKey<Item>) {
            builder.addTag(ItemTags.BREAKS_DECORATED_POTS, tagKey)
            builder.addTag(ItemTags.CLUSTER_MAX_HARVESTABLES, tagKey)
            builder.addTag(ItemTags.DURABILITY_ENCHANTABLE, tagKey)
            builder.addTag(ItemTags.MINING_ENCHANTABLE, tagKey)
            builder.addTag(ItemTags.MINING_LOOT_ENCHANTABLE, tagKey)
            builder.addTag(Tags.Items.TOOLS, tagKey)
        }

        setupTool(RagiumModTags.Items.TOOLS_DRILL)
        setupTool(RagiumModTags.Items.TOOLS_HAMMER)
        // Buckets
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            builder.addTags(Tags.Items.BUCKETS, content.bucketTag, content.getBucket().toHolderLike())
        }
        // LED
        for ((color: HTColorMaterial, block: HTHolderLike) in RagiumBlocks.LED_BLOCKS) {
            builder.add(color.dyedTag, block)
        }
        // Parts
        builder.add(Tags.Items.LEATHERS, RagiumItems.SYNTHETIC_LEATHER)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.RESIN)
        builder.add(Tags.Items.SLIME_BALLS, RagiumItems.TAR)
        builder.add(Tags.Items.STRINGS, RagiumItems.SYNTHETIC_FIBER)

        listOf(Items.GHAST_TEAR, Items.PHANTOM_MEMBRANE, Items.WIND_CHARGE)
            .map(Item::toHolderLike)
            .forEach { holder: HTHolderLike -> builder.add(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, holder) }

        builder.add(RagiumModTags.Items.POLYMER_RESIN, RagiumItems.POLYMER_RESIN)
        builder.add(RagiumModTags.Items.POLYMER_RESIN, ItemContent.POLYMER_RESIN.toHolderLike(), HTTagDependType.OPTIONAL)

        builder.add(RagiumCommonTags.Items.PLASTIC, RagiumItems.getPlate(CommonMaterialKeys.PLASTIC))
        builder.addTag(RagiumModTags.Items.PLASTICS, CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC)
        builder.addTag(RagiumModTags.Items.PLASTICS, RagiumCommonTags.Items.PLASTIC, HTTagDependType.OPTIONAL)
        builder.addTag(
            RagiumModTags.Items.PLASTICS,
            PneumaticCraftTags.Items.PLASTIC_SHEETS,
            HTTagDependType.OPTIONAL,
        )
        // Fuels
        builder.add(RagiumModTags.Items.IS_NUCLEAR_FUEL, RagiumItems.GREEN_PELLET)
        builder.add(RagiumModTags.Items.IS_NUCLEAR_FUEL, MekanismItems.REPROCESSED_FISSILE_FRAGMENT.id, HTTagDependType.OPTIONAL)
        builder.add(
            RagiumModTags.Items.IS_NUCLEAR_FUEL,
            ItemContent.SMALL_URANIUM_PELLET.toHolderLike(),
            HTTagDependType.OPTIONAL,
        )
        // Other
        builder.addTag(ItemTags.PIGLIN_LOVED, CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
        builder.add(ItemTags.PIGLIN_LOVED, RagiumItems.FEVER_CHERRY)

        builder.add(RagiumModTags.Items.BUDDING_AZURE_ACTIVATOR, RagiumItems.BLUE_KNOWLEDGE)
        // WIP
        builder.add(RagiumModTags.Items.WIP, RagiumDelightAddon.RAGI_CHERRY_TOAST_BLOCK, HTTagDependType.OPTIONAL)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.BOTTLED_BEE)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.DRILL)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.SLOT_COVER)
        builder.add(RagiumModTags.Items.WIP, RagiumItems.SOLAR_PANEL)
    }

    //    Integration    //

    private fun accessories(builder: HTTagBuilder<Item>) {
        builder.add(AccessoriesTags.BACK_TAG, RagiumItems.ECHO_STAR)
        builder.add(AccessoriesTags.BELT_TAG, RagiumItems.UNIVERSAL_BUNDLE)
        builder.add(AccessoriesTags.CHARM_TAG, RagiumItems.ADVANCED_MAGNET)
        builder.add(AccessoriesTags.CHARM_TAG, RagiumItems.DYNAMIC_LANTERN)
        builder.add(AccessoriesTags.CHARM_TAG, RagiumItems.MAGNET)
        builder.add(AccessoriesTags.FACE_TAG, RagiumItems.NIGHT_VISION_GOGGLES)

        builder.add(RagiumModTags.Items.BYPASS_MENU_VALIDATION, RagiumItems.UNIVERSAL_BUNDLE)
    }

    private fun create(builder: HTTagBuilder<Item>) {
        builder.addTool(HTSandPaperToolVariant, RagiumCreateAddon.SAND_PAPER_MAP.values)
    }

    private fun pneumatic(builder: HTTagBuilder<Item>) {
        builder.add(PneumaticCraftTags.Items.PLASTIC_SHEETS, RagiumItems.getPlate(CommonMaterialKeys.PLASTIC))
    }

    //    Extensions    //

    private fun HTTagBuilder<Item>.addTool(variant: HTToolVariant, holders: Iterable<HTHolderLike>) {
        for (tagKey: TagKey<Item> in variant.tagKeys) {
            for (holder: HTHolderLike in holders) {
                this.add(tagKey, holder, HTTagDependType.OPTIONAL)
            }
        }
    }

    override fun createContentsProvider(): CompletableFuture<HolderLookup.Provider> = super
        .createContentsProvider()
        .thenCombine(blockTags) { provider: HolderLookup.Provider, lookup: TagLookup<Block> ->
            for ((blockTag: TagKey<Block>, itemTag: TagKey<Item>) in tagsToCopy) {
                val builder: TagBuilder = getOrCreateRawBuilder(itemTag)
                lookup
                    .apply(blockTag)
                    .orElseThrow { error("Missing block tag ${itemTag.location}") }
                    .build()
                    .forEach(builder::add)
            }
            provider
        }
}
