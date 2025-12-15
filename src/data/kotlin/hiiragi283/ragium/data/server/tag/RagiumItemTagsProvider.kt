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
import hiiragi283.ragium.common.HTUpgradeType
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.variant.VanillaToolVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
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
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import rearth.oritech.init.ItemContent
import vectorwing.farmersdelight.common.registry.ModBlocks
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(private val blockTags: CompletableFuture<TagLookup<Block>>, context: HTDataGenContext) :
    HTTagsProvider<Item>(Registries.ITEM, context) {
    companion object {
        @JvmField
        val MATERIAL_TAG: Map<HTMaterialPrefix, TagKey<Item>> = mapOf(
            CommonMaterialPrefixes.GEM to ItemTags.BEACON_PAYMENT_ITEMS,
            CommonMaterialPrefixes.INGOT to ItemTags.BEACON_PAYMENT_ITEMS,
        ).mapKeys { (prefix: HTPrefixLike, _) -> prefix.asMaterialPrefix() }
    }

    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        copy()

        material(factory)
        food(factory)
        upgradeTargets(factory)
        upgradeExclusive(factory)
        categories(factory)

        accessories(factory)
        pneumatic(factory)
    }

    //    Copy    //

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

        for (key: HTMaterialKey in RagiumBlocks.GLASSES.columnKeys) {
            copy(CommonMaterialPrefixes.GLASS_BLOCK, key)
        }
        copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS)
        copy(Tags.Blocks.GLASS_BLOCKS_TINTED, Tags.Items.GLASS_BLOCKS_TINTED)

        copy(Tags.Blocks.OBSIDIANS, Tags.Items.OBSIDIANS)
        copy(RagiumCommonTags.Blocks.OBSIDIANS_MYSTERIOUS, RagiumCommonTags.Items.OBSIDIANS_MYSTERIOUS)

        copy(BlockTags.SLABS, ItemTags.SLABS)
        copy(BlockTags.STAIRS, ItemTags.STAIRS)
        copy(BlockTags.WALLS, ItemTags.WALLS)
        copy(RagiumModTags.Blocks.LED_BLOCKS, RagiumModTags.Items.LED_BLOCKS)

        copy(Tags.Blocks.CLUSTERS, Tags.Items.CLUSTERS)
        copy(Tags.Blocks.BUDDING_BLOCKS, Tags.Items.BUDDING_BLOCKS)
    }

    private val tagsToCopy: MutableMap<TagKey<Block>, TagKey<Item>> = mutableMapOf()

    private fun copy(prefix: HTPrefixLike) {
        copy(prefix.createCommonTagKey(Registries.BLOCK), prefix.createCommonTagKey(Registries.ITEM))
    }

    private fun copy(prefix: HTPrefixLike, material: HTMaterialLike) {
        copy(prefix.createTagKey(Registries.BLOCK, material), prefix.itemTagKey(material))
    }

    private fun copy(blockTag: TagKey<Block>, itemTag: TagKey<Item>) {
        tagsToCopy[blockTag] = itemTag
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        fromTable(factory, RagiumItems.MATERIALS)
        // Fuels
        addMaterial(factory, CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.COAL)
            .addItem(Items.COAL)
        addMaterial(factory, CommonMaterialPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL)
            .addItem(Items.CHARCOAL)
        factory
            .apply(RagiumCommonTags.Items.COAL_COKE)
            .add(RagiumItems.getMaterial(CommonMaterialPrefixes.FUEL, CommonMaterialKeys.COAL_COKE))

        addMaterial(factory, CommonMaterialPrefixes.FUEL, CommonMaterialKeys.COAL_COKE)
            .addTag(RagiumCommonTags.Items.COAL_COKE, HTTagDependType.OPTIONAL)
        // Gems
        addMaterial(factory, CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)
            .addItem(Items.ECHO_SHARD)
        // Scraps
        addMaterial(factory, CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE)
            .addItem(Items.NETHERITE_SCRAP)
        // Integration
        fromTable(factory, RagiumIntegrationItems.MATERIALS, HTTagDependType.OPTIONAL)

        // Dyes
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.WHITE)
            .addItem(Items.BONE_MEAL)
            .addItem(Items.LILY_OF_THE_VALLEY)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.ORANGE)
            .addItem(Items.ORANGE_TULIP)
            .addItem(Items.TORCHFLOWER)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.MAGENTA)
            .addItem(Items.ALLIUM)
            .addItem(Items.LILAC)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.YELLOW)
            .addItem(Items.DANDELION)
            .addItem(Items.SUNFLOWER)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.PINK)
            .addItem(Items.PINK_TULIP)
            .addItem(Items.PEONY)
            .addItem(Items.PINK_PETALS)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.LIGHT_GRAY)
            .addItem(Items.AZURE_BLUET)
            .addItem(Items.OXEYE_DAISY)
            .addItem(Items.WHITE_TULIP)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.CYAN)
            .addItem(Items.PITCHER_PLANT)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.BLUE)
            .addItem(Items.CORNFLOWER)
            .addItem(Items.LAPIS_LAZULI)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.RED)
            .addItem(Items.POPPY)
            .addItem(Items.RED_TULIP)
            .addItem(Items.ROSE_BUSH)
        addMaterial(factory, CommonMaterialPrefixes.RAW_MATERIAL_DYE, HTColorMaterial.BLACK)
            .addItem(Items.INK_SAC)
            .addItem(Items.WITHER_ROSE)
    }

    private fun fromTable(
        factory: BuilderFactory<Item>,
        table: ImmutableTable<out HTPrefixLike, out HTMaterialLike, out HTHolderLike>,
        type: HTTagDependType = HTTagDependType.REQUIRED,
    ) {
        table.forEach { (prefix: HTPrefixLike, key: HTMaterialLike, item: HTHolderLike) ->
            addMaterial(factory, prefix, key).add(item, type)
            val customTag: TagKey<Item> = MATERIAL_TAG[prefix] ?: return@forEach
            factory.apply(customTag).addTag(prefix, key, type)
        }
    }

    //    Foods    //

    private fun food(factory: BuilderFactory<Item>) {
        val foodsFruit = HTMaterialPrefix("food/fruit", "c:foods/fruit", "c:foods/%s")

        // Crop
        addMaterial(factory, CommonMaterialPrefixes.CROP, FoodMaterialKeys.WARPED_WART).add(RagiumBlocks.WARPED_WART)
        // Food
        factory
            .apply(Tags.Items.FOODS)
            .add(RagiumBlocks.WARPED_WART)
            .add(RagiumItems.AMBROSIA)
            .add(RagiumItems.CANNED_COOKED_MEAT)
            .add(RagiumItems.CHOCOLATE_BREAD)
            .add(RagiumItems.ICE_CREAM)
            .add(RagiumItems.ICE_CREAM_SODA)
            .add(RagiumItems.MELON_PIE)
            .add(RagiumItems.RAGI_CHERRY_PIE)
            .add(RagiumItems.RAGI_CHERRY_PIE_SLICE)
            .add(RagiumItems.RAGI_CHERRY_TOAST)
            .add(RagiumItems.SWEET_BERRIES_CAKE_SLICE)
            .addTag(CommonMaterialPrefixes.JAM)
            .addTag(RagiumCommonTags.Items.FOODS_DOUGH)

        factory.apply(Tags.Items.FOODS_BERRY).add(RagiumBlocks.EXP_BERRIES)
        factory.apply(Tags.Items.FOODS_GOLDEN).add(RagiumItems.FEVER_CHERRY)

        factory.apply(Tags.Items.FOODS_FRUIT).add(RagiumItems.FEVER_CHERRY)

        factory
            .apply(ItemTags.MEAT)
            .addTag(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.COOKED_MEAT)
            .addTag(CommonMaterialPrefixes.FOOD, FoodMaterialKeys.RAW_MEAT)

        factory
            .apply(RagiumModTags.Items.RAW_MEAT)
            .addTag(Tags.Items.FOODS_RAW_MEAT)
            .addTag(Tags.Items.FOODS_RAW_FISH)
            .addItem(Items.ROTTEN_FLESH)

        addMaterial(factory, CommonMaterialPrefixes.FOOD, FoodMaterialKeys.APPLE).addItem(Items.APPLE)

        addMaterial(factory, foodsFruit, FoodMaterialKeys.RAGI_CHERRY).add(RagiumItems.RAGI_CHERRY)

        addMaterial(factory, CommonMaterialPrefixes.JAM, FoodMaterialKeys.RAGI_CHERRY).add(RagiumItems.RAGI_CHERRY_JAM)

        factory
            .apply(RagiumCommonTags.Items.FOODS_DOUGH)
            .addTag(CommonMaterialPrefixes.DOUGH)
    }

    //    Upgrades    //

    private fun upgradeTargets(factory: BuilderFactory<Item>) {
        // Generator
        factory
            .apply(RagiumModTags.Items.GENERATOR_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.THERMAL_GENERATOR)
            // Advanced
            .add(RagiumBlocks.CULINARY_GENERATOR)
            .add(RagiumBlocks.MAGMATIC_GENERATOR)
            // Elite
            .add(RagiumBlocks.COMBUSTION_GENERATOR)
            .add(RagiumBlocks.SOLAR_PANEL_CONTROLLER)
            // Ultimate
            .add(RagiumBlocks.ENCHANTMENT_GENERATOR)
            .add(RagiumBlocks.NUCLEAR_REACTOR)
        // Processor
        factory
            .apply(RagiumModTags.Items.PROCESSOR_UPGRADABLE)
            // Basic
            .add(RagiumBlocks.ALLOY_SMELTER)
            .add(RagiumBlocks.BLOCK_BREAKER)
            .add(RagiumBlocks.COMPRESSOR)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.ELECTRIC_FURNACE)
            .add(RagiumBlocks.EXTRACTOR)
            .add(RagiumBlocks.PULVERIZER)
            // Advanced
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.REFINERY)
            // Elite
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.ADVANCED_MIXER)
            .add(RagiumBlocks.MULTI_SMELTER)
            .add(RagiumBlocks.PLANTER)
            // Ultimate
            .add(RagiumBlocks.ENCHANTER)
            .add(RagiumBlocks.MOB_CRUSHER)
            .add(RagiumBlocks.SIMULATOR)

        factory
            .apply(RagiumModTags.Items.EXTRA_VOIDING_UPGRADABLE)
            .add(RagiumBlocks.CUTTING_MACHINE)
            .add(RagiumBlocks.PULVERIZER)
            .add(RagiumBlocks.CRUSHER)

        factory
            .apply(RagiumModTags.Items.EFFICIENT_CRUSHING_UPGRADABLE)
            .add(RagiumBlocks.PULVERIZER)
            .add(RagiumBlocks.CRUSHER)
        // Storage
        factory
            .apply(RagiumModTags.Items.ENERGY_CAPACITY_UPGRADABLE)
            .addTag(RagiumModTags.Items.GENERATOR_UPGRADABLE)
            .addTag(RagiumModTags.Items.PROCESSOR_UPGRADABLE)
            // Storage
            .add(RagiumBlocks.BATTERY)
            .add(RagiumBlocks.BUFFER)

        factory
            .apply(RagiumModTags.Items.FLUID_CAPACITY_UPGRADABLE)
            // Generator
            .add(RagiumBlocks.CULINARY_GENERATOR)
            .add(RagiumBlocks.MAGMATIC_GENERATOR)
            .add(RagiumBlocks.COMBUSTION_GENERATOR)
            // Processor
            .add(RagiumBlocks.COMPRESSOR)
            .add(RagiumBlocks.EXTRACTOR)
            .add(RagiumBlocks.PULVERIZER)
            .add(RagiumBlocks.CRUSHER)
            .add(RagiumBlocks.MELTER)
            .add(RagiumBlocks.MIXER)
            .add(RagiumBlocks.REFINERY)
            .add(RagiumBlocks.BREWERY)
            .add(RagiumBlocks.ADVANCED_MIXER)
            .add(RagiumBlocks.PLANTER)
            .add(RagiumBlocks.ENCHANTER)
            .add(RagiumBlocks.MOB_CRUSHER)
            .add(RagiumBlocks.SIMULATOR)
            // Device
            .add(RagiumBlocks.FLUID_COLLECTOR)
            // Storage
            .add(RagiumBlocks.TANK)
            .add(RagiumBlocks.BUFFER)

        factory
            .apply(RagiumModTags.Items.ITEM_CAPACITY_UPGRADABLE)
            .add(RagiumBlocks.CRATE)
    }

    private fun upgradeExclusive(factory: BuilderFactory<Item>) {
        factory
            .apply(RagiumModTags.Items.EXTRACTOR_EXCLUSIVE)
            .add(HTUpgradeType.BIO_COMPOSTING)
            .add(HTUpgradeType.EXP_EXTRACTING)
    }

    //    Categories    //

    private fun categories(factory: BuilderFactory<Item>) {
        factory
            .apply(RagiumModTags.Items.IGNORED_IN_RECIPES)
            .add(RagiumItems.SLOT_COVER)

        factory
            .apply(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC)
            .addTag(ItemTags.SMELTS_TO_GLASS)

        factory
            .apply(RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED)
            .add(RagiumItems.MAGMA_SHARD)
            .addTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
        // Enchantments
        addTags(factory, Tags.Items.ENCHANTABLES, RagiumModTags.Items.RANGE_ENCHANTABLE)
            .add(RagiumItems.ADVANCED_MAGNET)
            .add(RagiumItems.MAGNET)

        addTags(factory, Tags.Items.ENCHANTABLES, RagiumModTags.Items.STRIKE_ENCHANTABLE)
            .addTag(ItemTags.AXES)
        // Equipments
        buildTable {
            putAll(RagiumItems.ARMORS)
            putAll(RagiumItems.TOOLS)

            putAll(RagiumIntegrationItems.TOOLS)
        }.forEach { (variant: HTToolVariant, _, item: HTHolderLike) ->
            for (tagKey: TagKey<Item> in variant.tagKeys) {
                factory.apply(tagKey).add(item)
            }
            if (variant == VanillaToolVariant.PICKAXE) {
                factory.apply(ItemTags.CLUSTER_MAX_HARVESTABLES).add(item)
            }
        }

        factory.apply(Tags.Items.TOOLS_WRENCH).add(RagiumItems.getHammer(RagiumMaterialKeys.RAGI_ALLOY))

        factory.apply(RagiumModTags.Items.TOOLS_DRILL).add(RagiumItems.DRILL)

        fun setupTool(tagKey: TagKey<Item>) {
            factory.apply(ItemTags.BREAKS_DECORATED_POTS).addTag(tagKey)
            factory.apply(ItemTags.CLUSTER_MAX_HARVESTABLES).addTag(tagKey)
            factory.apply(ItemTags.DURABILITY_ENCHANTABLE).addTag(tagKey)
            factory.apply(ItemTags.MINING_ENCHANTABLE).addTag(tagKey)
            factory.apply(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(tagKey)
            factory.apply(Tags.Items.TOOLS).addTag(tagKey)
        }

        setupTool(RagiumModTags.Items.TOOLS_DRILL)
        setupTool(RagiumModTags.Items.TOOLS_HAMMER)
        // Buckets
        for (content: HTFluidContent<*, *, *, *, *> in RagiumFluidContents.REGISTER.contents) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.bucket)
        }
        // LED
        for ((color: HTColorMaterial, block: HTHolderLike) in RagiumBlocks.LED_BLOCKS) {
            factory.apply(color.dyedTag).add(block)
        }
        // Parts
        factory.apply(Tags.Items.LEATHERS).add(RagiumItems.SYNTHETIC_LEATHER)
        factory
            .apply(Tags.Items.SLIME_BALLS)
            .add(RagiumItems.ROSIN)
            .add(RagiumItems.TAR)
        factory.apply(Tags.Items.STRINGS).add(RagiumItems.SYNTHETIC_FIBER)

        factory
            .apply(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)

        factory.apply(ItemTags.PLANKS).add(RagiumItems.getPlate(VanillaMaterialKeys.WOOD))
        // Plastics
        factory
            .apply(RagiumModTags.Items.POLYMER_RESIN)
            .add(RagiumItems.POLYMER_RESIN)
            .addItem(ItemContent.POLYMER_RESIN, HTTagDependType.OPTIONAL)

        factory.apply(RagiumCommonTags.Items.PLASTIC).add(RagiumItems.getPlate(CommonMaterialKeys.PLASTIC))
        factory
            .apply(RagiumModTags.Items.PLASTICS)
            .addTag(RagiumCommonTags.Items.PLASTIC, HTTagDependType.OPTIONAL)
            .addTag(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.PLASTIC)
            .addTag(PneumaticCraftTags.Items.PLASTIC_SHEETS, HTTagDependType.OPTIONAL)

        factory.apply(RagiumCommonTags.Items.CIRCUITS_BASIC).add(RagiumItems.BASIC_CIRCUIT)
        factory.apply(RagiumCommonTags.Items.CIRCUITS_ADVANCED).add(RagiumItems.ADVANCED_CIRCUIT)
        // Fuels
        factory
            .apply(RagiumModTags.Items.IS_NUCLEAR_FUEL)
            .add(RagiumItems.GREEN_PELLET)
            .add(MekanismItems.REPROCESSED_FISSILE_FRAGMENT.id, HTTagDependType.OPTIONAL)
            .addItem(ItemContent.SMALL_URANIUM_PELLET, HTTagDependType.OPTIONAL)
        // Soils
        factory
            .apply(RagiumModTags.Items.SOILS_DIRT)
            .addItem(Items.DIRT)
            .addItem(Items.FARMLAND)
            .addItem(ModBlocks.RICH_SOIL.get(), HTTagDependType.OPTIONAL)
            .addItem(ModBlocks.RICH_SOIL_FARMLAND.get(), HTTagDependType.OPTIONAL)

        factory
            .apply(RagiumModTags.Items.SOILS_AQUATIC)
            .addTag(ItemTags.SAND)
            .addTag(Tags.Items.GRAVELS)
        // Other
        factory
            .apply(ItemTags.PIGLIN_LOVED)
            .add(RagiumItems.FEVER_CHERRY)
            .addTag(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)

        factory.apply(Tags.Items.FERTILIZERS).add(RagiumItems.BIO_FERTILIZER)

        for (item: HTHolderLike in RagiumItems.MOLDS.values) {
            factory.apply(RagiumModTags.Items.MOLDS).add(item)
        }
    }

    //    Integration    //

    private fun accessories(factory: BuilderFactory<Item>) {
        factory
            .apply(AccessoriesTags.BACK_TAG)
            .add(RagiumItems.ECHO_STAR)
        factory
            .apply(AccessoriesTags.CHARM_TAG)
            .add(RagiumItems.ADVANCED_MAGNET)
            .add(RagiumItems.DYNAMIC_LANTERN)
            .add(RagiumItems.MAGNET)
        factory
            .apply(AccessoriesTags.FACE_TAG)
            .add(RagiumItems.NIGHT_VISION_GOGGLES)

        factory.apply(RagiumModTags.Items.BYPASS_MENU_VALIDATION).add(RagiumItems.UNIVERSAL_BUNDLE)
        factory.apply(RagiumModTags.Items.DISABLE_ACCESSORY_EQUIP).add(RagiumItems.UNIVERSAL_BUNDLE)
    }

    private fun pneumatic(factory: BuilderFactory<Item>) {
        factory.apply(PneumaticCraftTags.Items.PLASTIC_SHEETS).add(RagiumItems.getPlate(CommonMaterialKeys.PLASTIC))
    }

    //    Extensions    //

    fun HTTagBuilder<Item>.addItem(item: ItemLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> =
        this.add(item.toHolderLike(), type)

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
