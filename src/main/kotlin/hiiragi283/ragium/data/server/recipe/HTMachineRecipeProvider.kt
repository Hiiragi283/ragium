package hiiragi283.ragium.data.server.recipe

import com.mojang.authlib.properties.PropertyMap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.buildCompPatch
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.recipe.HTSimpleItemResult
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.chat.Component
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import java.util.*

object HTMachineRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        assembler(output)
        brewery(output)
        compressor(output)
        enchanter(output, holderLookup.lookupOrThrow(Registries.ENCHANTMENT))
        extractor(output)
        growthChamber(output)
        laser(output)
        refinery(output)
    }

    //    Assembler    //

    private fun assembler(output: RecipeOutput) {
        // Circuit Board
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItemTags.PLASTICS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .save(output)

        // Hiiragi283's Head
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM, 64)
            .itemOutput(
                HTSimpleItemResult(
                    Items.PLAYER_HEAD,
                    1,
                    buildCompPatch {
                        set(
                            DataComponents.PROFILE,
                            ResolvableProfile(
                                Optional.of("Russell_283"),
                                Optional.empty(),
                                PropertyMap(),
                            ),
                        )
                        set(DataComponents.RARITY, Rarity.EPIC)
                        set(DataComponents.ITEM_NAME, Component.literal("Hiiragi 283"))
                    },
                ),
            ).save(output, RagiumAPI.id("hiiragi_head"))
    }

    //    brewery    //

    private fun brewery(output: RecipeOutput) {
        fun register(potion: Holder<Potion>, input: ItemLike) {
            HTBreweryRecipeBuilder()
                .itemInput(Tags.Items.CROPS_NETHER_WART)
                .itemInput(input)
                .potionOutput(potion)
                .save(output)
        }

        fun registerFermented(potion: Holder<Potion>, input: ItemLike) {
            HTBreweryRecipeBuilder()
                .itemInput(Tags.Items.CROPS_NETHER_WART)
                .itemInput(input)
                .itemInput(Items.FERMENTED_SPIDER_EYE)
                .potionOutput(potion)
                .save(output)
        }

        register(Potions.FIRE_RESISTANCE, Items.MAGMA_CREAM)
        register(Potions.HEALING, Items.GLISTERING_MELON_SLICE)
        register(Potions.INFESTED, Items.STONE)
        register(Potions.LEAPING, Items.RABBIT_FOOT)
        register(Potions.LUCK, Items.EMERALD)
        register(Potions.NIGHT_VISION, Items.GOLDEN_CARROT)
        register(Potions.OOZING, Items.SLIME_BLOCK)
        register(Potions.POISON, Items.SPIDER_EYE)
        register(Potions.REGENERATION, Items.GHAST_TEAR)
        register(Potions.SLOW_FALLING, Items.PHANTOM_MEMBRANE)
        register(Potions.STRENGTH, Items.BLAZE_POWDER)
        register(Potions.SWIFTNESS, Items.SUGAR)
        register(Potions.TURTLE_MASTER, Items.TURTLE_HELMET)
        register(Potions.WATER_BREATHING, Items.PUFFERFISH)
        register(Potions.WEAVING, Items.COBWEB)
        register(Potions.WIND_CHARGED, Items.BREEZE_ROD)
        registerFermented(Potions.HARMING, Items.GLISTERING_MELON_SLICE)
        registerFermented(Potions.INVISIBILITY, Items.GOLDEN_CARROT)
        registerFermented(Potions.SLOWNESS, Items.SUGAR)
        registerFermented(Potions.WEAKNESS, Items.BLAZE_POWDER)
    }

    //    Compressor    //

    private fun compressor(output: RecipeOutput) {
        // Shaft
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 2)
            .catalyst(RagiumBlocks.SHAFT)
            .itemOutput(RagiumBlocks.SHAFT)
            .saveSuffixed(output, "_from_steel")

        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .catalyst(RagiumBlocks.SHAFT)
            .itemOutput(RagiumBlocks.SHAFT)
            .saveSuffixed(output, "_from_deep_steel")
        // Pulp -> Oak Planks
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.WOOD)
            .catalyst(ItemTags.PLANKS)
            .itemOutput(Items.OAK_PLANKS)
            .save(output)
    }

    //    Enchanter    //

    private fun enchanter(output: RecipeOutput, lookup: HolderLookup.RegistryLookup<Enchantment>) {
        // for Armors
        // for Swords
        HTEnchanterRecipeBuilder(lookup, Enchantments.SHARPNESS)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.QUARTZ, 64)
            .save(output)

        HTEnchanterRecipeBuilder(lookup, Enchantments.BANE_OF_ARTHROPODS)
            .itemInput(Items.SPIDER_EYE, 16)
            .save(output)

        HTEnchanterRecipeBuilder(lookup, Enchantments.LOOTING)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.EMERALD, 16)
            .save(output)
        // for Mining Tools
        // for Bows
        // for Tridents
        HTEnchanterRecipeBuilder(lookup, Enchantments.LOYALTY)
            .itemInput(Items.LEAD, 8)
            .save(output)

        HTEnchanterRecipeBuilder(lookup, Enchantments.IMPALING)
            .itemInput(RagiumItems.PRISMARINE_REAGENT, 64)
            .save(output)

        HTEnchanterRecipeBuilder(lookup, Enchantments.RIPTIDE)
            .itemInput(Items.HEART_OF_THE_SEA)
            .save(output)

        HTEnchanterRecipeBuilder(lookup, Enchantments.CHANNELING)
            .itemInput(Items.LIGHTNING_ROD, 64)
            .save(output)
        // for Crossbows
        // for Maces
    }

    //    Extractor    //

    private fun extractor(output: RecipeOutput) {
        // Water
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.BUCKETS_WATER)
            .itemOutput(Items.BUCKET)
            .waterOutput()
            .save(output, RagiumAPI.id("water"))

        // Lava
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.BUCKETS_LAVA)
            .itemOutput(Items.BUCKET)
            .fluidOutput(Fluids.LAVA)
            .save(output, RagiumAPI.id("lava"))

        // Milk
        HTExtractorRecipeBuilder()
            .itemInput(Items.MILK_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(NeoForgeMod.MILK)
            .save(output, RagiumAPI.id("milk"))

        // Slime
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.SLIME_BALLS)
            .fluidOutput(RagiumFluids.SLIME)
            .save(output, RagiumAPI.id("slime"))

        // Crude Oil
        HTExtractorRecipeBuilder()
            .itemInput(RagiumItems.CRUDE_OIL_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(RagiumFluids.CRUDE_OIL)
            .save(output, RagiumAPI.id("crude_oil"))

        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.COALS, 8)
            .fluidOutput(RagiumFluids.CRUDE_OIL)
            .save(output, RagiumAPI.id("crude_oil_from_coal"))

        // Obsidian Tier
        HTExtractorRecipeBuilder()
            .itemInput(Tags.Items.OBSIDIANS_CRYING)
            .itemOutput(RagiumItems.OBSIDIAN_TEAR, 4)
            .save(output)
    }

    //    Growth Chamber    //

    private fun growthChamber(output: RecipeOutput) {
        fun register(
            seed: ItemLike,
            soil: TagKey<Item>,
            crop: ItemLike,
            count: Int,
            water: Int = 100,
        ) {
            HTGrowthChamberRecipeBuilder()
                .itemInput(seed)
                .itemInput(soil)
                .water(water)
                .itemOutput(crop, count)
                .save(output)
        }

        fun register(
            seed: TagKey<Item>,
            soil: TagKey<Item>,
            crop: ItemLike,
            count: Int,
            water: Int = 100,
        ) {
            HTGrowthChamberRecipeBuilder()
                .itemInput(seed)
                .itemInput(soil)
                .water(water)
                .itemOutput(crop, count)
                .save(output)
        }

        // Amethyst
        register(Tags.Items.GEMS_AMETHYST, Tags.Items.BUDDING_BLOCKS, Items.AMETHYST_SHARD, 1, 0)
        // Dirt
        register(Items.BAMBOO, RagiumItemTags.DIRT_SOILS, Items.BAMBOO, 6)
        register(Items.GLOW_BERRIES, RagiumItemTags.DIRT_SOILS, Items.GLOW_BERRIES, 2)
        register(Items.SWEET_BERRIES, RagiumItemTags.DIRT_SOILS, Items.SWEET_BERRIES, 2)
        register(Tags.Items.CROPS_CARROT, RagiumItemTags.DIRT_SOILS, Items.CARROT, 4)
        register(Tags.Items.CROPS_COCOA_BEAN, ItemTags.JUNGLE_LOGS, Items.COCOA_BEANS, 3)
        register(Tags.Items.CROPS_POTATO, RagiumItemTags.DIRT_SOILS, Items.POTATO, 4)
        register(Tags.Items.SEEDS_BEETROOT, RagiumItemTags.DIRT_SOILS, Items.BEETROOT, 2)
        register(Tags.Items.SEEDS_MELON, RagiumItemTags.DIRT_SOILS, Items.MELON, 1)
        register(Tags.Items.SEEDS_PUMPKIN, RagiumItemTags.DIRT_SOILS, Items.PUMPKIN, 1)
        register(Tags.Items.SEEDS_WHEAT, RagiumItemTags.DIRT_SOILS, Items.WHEAT, 2)
        // End
        register(Items.CHORUS_FLOWER, RagiumItemTags.END_SOILS, Items.CHORUS_FLOWER, 1, 0)
        register(Items.CHORUS_FRUIT, RagiumItemTags.END_SOILS, Items.CHORUS_FRUIT, 4, 0)
        // Mushroom Soil
        register(Items.BROWN_MUSHROOM, RagiumItemTags.MUSHROOM_SOILS, Items.BROWN_MUSHROOM, 2, 200)
        register(Items.RED_MUSHROOM, RagiumItemTags.MUSHROOM_SOILS, Items.RED_MUSHROOM, 2, 200)

        register(Items.CRIMSON_FUNGUS, RagiumItemTags.NETHER_SOILS, Items.CRIMSON_FUNGUS, 2, 0)
        register(Items.WARPED_FUNGUS, RagiumItemTags.NETHER_SOILS, Items.WARPED_FUNGUS, 2, 0)
        // Sand
        register(Tags.Items.CROPS_CACTUS, ItemTags.SAND, Items.CACTUS, 4, 0)
        register(Tags.Items.CROPS_SUGAR_CANE, ItemTags.SAND, Items.SUGAR_CANE, 4)
        // Soul Sand
        register(Tags.Items.CROPS_NETHER_WART, ItemTags.SOUL_FIRE_BASE_BLOCKS, Items.NETHER_WART, 3, 0)
    }

    //    Laser Assembly    //

    private fun laser(output: RecipeOutput) {
        // Gilded Blackstone
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Items.BLACKSTONE)
            .catalyst(RagiumItems.GLOW_LENS)
            .itemOutput(Items.GILDED_BLACKSTONE)
            .save(output)

        // Enchanted Golden Apply
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Items.GOLDEN_APPLE, 8)
            .catalyst(RagiumItems.GLOW_LENS)
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .save(output)

        // Crying Obsidian
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.CRYING_OBSIDIAN)
            .save(output)

        // Budding Amethyst
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Items.AMETHYST_BLOCK, 4)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.BUDDING_AMETHYST)
            .save(output)

        // Heavy Core
        HTSingleItemRecipeBuilder
            .laser()
            .itemInput(Tags.Items.STORAGE_BLOCKS_NETHERITE)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.HEAVY_CORE)
            .save(output)
    }

    //    Refinery    //

    private fun refinery(output: RecipeOutput) {
        // Soul XX -> Crude Oil
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluids.CRUDE_OIL, FluidType.BUCKET_VOLUME / 2)
            .save(output, RagiumAPI.id("crude_oil_from_soul"))
        // Crude Oil -> Naphtha
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumFluids.CRUDE_OIL)
            .fluidOutput(RagiumVirtualFluids.NAPHTHA)
            .save(output)
        // Naphtha -> Polymer Resin + Fuel
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.NAPHTHA)
            .itemOutput(RagiumItems.POLYMER_RESIN, 2)
            .fluidOutput(RagiumVirtualFluids.FUEL)
            .save(output)

        // Polymer Resin -> XX
        mapOf(
            RagiumItemTags.PLATE_MOLDS to RagiumItems.PLASTIC_PLATE,
            Tags.Items.STRINGS to Items.STRING,
            Tags.Items.LEATHERS to Items.LEATHER,
            Tags.Items.GLASS_BLOCKS to Items.GLASS,
        ).forEach { (catalyst: TagKey<Item>, result: ItemLike) ->
            HTSingleItemRecipeBuilder
                .compressor()
                .itemInput(RagiumItems.POLYMER_RESIN)
                .catalyst(catalyst)
                .itemOutput(result)
                .saveSuffixed(output, "_from_polymer")
        }

        // Biomass -> Alcohol
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.BIOMASS)
            .fluidOutput(RagiumVirtualFluids.ETHANOL)
            .save(output)
        // Alcohol + Plant Oil -> Bio Fuel + Glycerol
        HTMixerRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.ETHANOL, FluidType.BUCKET_VOLUME * 4)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL)
            .fluidOutput(RagiumVirtualFluids.BIODIESEL, FluidType.BUCKET_VOLUME * 4)
            // .fluidOutput(RagiumFluids.GLYCEROL)
            .save(output)

        // XX Log -> Sap + Pulp
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)
        // Sap -> Slimeball
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.SAP)
            .itemOutput(Items.SLIME_BALL)
            .save(output)

        // Crimson Stem -> Crimson Sap
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.CRIMSON_STEMS)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.CRIMSON_SAP)
            .savePrefixed(output, "crimson_")
        // Crimson Sap -> Crimson Crystal + Sap
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.CRIMSON_SAP)
            .itemOutput(RagiumItems.CRIMSON_CRYSTAL)
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)

        // Warped Stem -> Warped Sap
        HTExtractorRecipeBuilder()
            .itemInput(ItemTags.WARPED_STEMS)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.WOOD, 4)
            .fluidOutput(RagiumVirtualFluids.WARPED_SAP)
            .savePrefixed(output, "warped_")
        // Warped Sap -> Warped Crystal + Sap
        HTRefineryRecipeBuilder()
            .fluidInput(RagiumVirtualFluids.WARPED_SAP)
            .itemOutput(RagiumItems.WARPED_CRYSTAL)
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)
    }
}
