package hiiragi283.ragium.data.server.recipe

import com.mojang.authlib.properties.PropertyMap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.item.HTItemStackBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.recipe.HTBreweryRecipe
import hiiragi283.ragium.api.recipe.HTEnchanterRecipe
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import java.util.*

object HTMachineRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        assembler(output)
        brewery(output)
        compressor(output)
        crusher(output)
        enchanter(output, holderLookup.lookupOrThrow(Registries.ENCHANTMENT))
        extractor(output)
        infuser(output)
        growthChamber(output)
        laser(output)
        mixer(output)
        refinery(output)
        solidifier(output)
    }

    //    Assembler    //

    private fun assembler(output: RecipeOutput) {
        // Circuit Board
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(RagiumItemTags.PLASTICS)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.QUARTZ)
            .itemOutput(RagiumItems.CIRCUIT_BOARD)
            .save(output)
        // Hiiragi283's Head
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM, 64)
            .itemOutput(
                HTItemStackBuilder(Items.PLAYER_HEAD, 1)
                    .put(
                        DataComponents.PROFILE,
                        ResolvableProfile(
                            Optional.of("Russell_283"),
                            Optional.empty(),
                            PropertyMap(),
                        ),
                    ).put(DataComponents.RARITY, Rarity.EPIC)
                    .put(DataComponents.ITEM_NAME, Component.literal("Hiiragi 283"))
                    .build(),
            ).save(output, RagiumAPI.id("hiiragi_head"))
    }

    //    brewery    //

    private fun brewery(output: RecipeOutput) {
        fun register(potion: Holder<Potion>, input: ItemLike) {
            output.accept(
                potion.idOrThrow.withPrefix("brewery/"),
                HTBreweryRecipe(
                    "",
                    HTItemIngredient.of(Tags.Items.CROPS_NETHER_WART),
                    HTItemIngredient.of(input),
                    Optional.empty(),
                    potion,
                ),
                null,
            )
        }

        fun registerFermented(potion: Holder<Potion>, input: ItemLike) {
            output.accept(
                potion.idOrThrow.withPrefix("brewery/"),
                HTBreweryRecipe(
                    "",
                    HTItemIngredient.of(Tags.Items.CROPS_NETHER_WART),
                    HTItemIngredient.of(input),
                    Optional.of(HTItemIngredient.of(Items.FERMENTED_SPIDER_EYE)),
                    potion,
                ),
                null,
            )
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
            .compressor(lookup)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 2)
            .catalyst(RagiumBlocks.SHAFT)
            .itemOutput(RagiumBlocks.SHAFT)
            .saveSuffixed(output, "_from_steel")

        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .catalyst(RagiumBlocks.SHAFT)
            .itemOutput(RagiumBlocks.SHAFT)
            .saveSuffixed(output, "_from_deep_steel")
        // Pulp -> Oak Planks
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(HTTagPrefix.DUST, CommonMaterials.WOOD)
            .catalyst(ItemTags.PLANKS)
            .itemOutput(Items.OAK_PLANKS)
            .save(output)
        // Clay -> 4x Clay Ball
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(Items.CLAY)
            .catalyst(RagiumItems.BALL_PRESS_MOLD)
            .itemOutput(Items.CLAY_BALL, 4)
            .save(output)
    }

    //    Crusher    //

    private fun crusher(output: RecipeOutput) {
        // Granite
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.GRANITE, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, VanillaMaterials.COPPER), 16)
            .save(output, RagiumAPI.id("granite"))
        // Diorite
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.DIORITE, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, VanillaMaterials.QUARTZ), 16)
            .save(output, RagiumAPI.id("diorite"))
        // Andesite
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.ANDESITE, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, VanillaMaterials.IRON), 16)
            .save(output, RagiumAPI.id("andesite"))

        // Deepslate
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.COBBLED_DEEPSLATE, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.NIOBIUM), 8)
            .itemOutput(Items.DIAMOND, 4)
            .save(output, RagiumAPI.id("deepslate"))
        // Tuff
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.TUFF, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.FLUORITE), 16)
            .itemOutput(Items.EMERALD, 4)
            .save(output, RagiumAPI.id("tuff"))

        // Clay
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.CLAY, 64)
            .itemOutput(Items.CLAY_BALL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.SILICON), 16)
            .save(output, RagiumAPI.id("clay"))
        // Sandstone
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS, 64)
            .itemOutput(Items.SAND, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.SALTPETER), 16)
            .save(output, RagiumAPI.id("sandstone"))
        // Red Sandstone
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Tags.Items.SANDSTONE_RED_BLOCKS, 64)
            .itemOutput(Items.RED_SAND, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.BAUXITE), 16)
            .itemOutput(Items.REDSTONE, 8)
            .save(output, RagiumAPI.id("red_sandstone"))

        // Netherrack
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Tags.Items.NETHERRACKS, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.SULFUR), 16)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.BAUXITE), 8)
            .save(output, RagiumAPI.id("netherrack"))
        // Blackstone
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.BLACKSTONE, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.RAW_MATERIAL, CommonMaterials.PYRITE), 8)
            .save(output, RagiumAPI.id("blackstone"))
        // Basalt
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.BASALT, 64)
            .itemOutput(Items.GRAVEL, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.ASH), 16)
            .save(output, RagiumAPI.id("basalt"))

        // End Stone
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.END_STONE, 64)
            .itemOutput(Items.SAND, 32)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, VanillaMaterials.AMETHYST), 8)
            .save(output, RagiumAPI.id("end_stone"))
        // Obsidian
        HTFluidOutputRecipeBuilder
            .crusher(lookup)
            .itemInput(Items.OBSIDIAN, 64)
            .itemOutput(Items.GRAVEL, 32)
    }

    //    Enchanter    //

    private fun enchanter(output: RecipeOutput, lookup: HolderLookup.RegistryLookup<Enchantment>) {
        fun register(enchantment: ResourceKey<Enchantment>, input: HTItemIngredient) {
            output.accept(
                enchantment.location().withPrefix("enchanter/"),
                HTEnchanterRecipe(
                    "",
                    input,
                    Optional.empty(),
                    lookup.getOrThrow(enchantment),
                ),
                null,
            )
        }

        // for Armors
        // for Swords
        register(Enchantments.SHARPNESS, HTItemIngredient.of(HTTagPrefix.GEM, VanillaMaterials.QUARTZ, 64))
        register(Enchantments.BANE_OF_ARTHROPODS, HTItemIngredient.of(Items.SPIDER_EYE, 16))
        register(Enchantments.LOOTING, HTItemIngredient.of(HTTagPrefix.GEM, VanillaMaterials.EMERALD, 16))
        // for Mining Tools
        // for Bows
        // for Tridents
        register(Enchantments.LOYALTY, HTItemIngredient.of(Items.LEAD, 8))
        register(Enchantments.IMPALING, HTItemIngredient.of(RagiumItems.PRISMARINE_REAGENT, 64))
        register(Enchantments.RIPTIDE, HTItemIngredient.of(Items.HEART_OF_THE_SEA))
        register(Enchantments.CHANNELING, HTItemIngredient.of(Items.LIGHTNING_ROD, 64))
        // for Crossbows
        // for Maces
    }

    //    Extractor    //

    private fun extractor(output: RecipeOutput) {
        // Wet Sponge -> Sponge + Water
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.WET_SPONGE)
            .itemOutput(Items.SPONGE)
            .waterOutput()
            .save(output)
    }

    //    Infuser    //

    private fun infuser(output: RecipeOutput) {
        // Concrete Powder -> Concrete
        for (color: DyeColor in DyeColor.entries) {
            val powder: Item =
                BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("${color}_concrete_powder"))
            val concrete: Item = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("${color}_concrete"))
            HTFluidOutputRecipeBuilder
                .infuser(lookup)
                .itemInput(powder)
                .waterInput(100)
                .itemOutput(concrete)
                .save(output)
        }
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
            HTGrowthChamberRecipeBuilder(lookup)
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
            HTGrowthChamberRecipeBuilder(lookup)
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
        register(RagiumItemTags.CROPS_WARPED_WART, ItemTags.SOUL_FIRE_BASE_BLOCKS, RagiumItems.WARPED_WART, 3, 0)
    }

    //    Laser Assembly    //

    private fun laser(output: RecipeOutput) {
        // Gilded Blackstone
        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Items.BLACKSTONE)
            .catalyst(RagiumItems.GLOW_LENS)
            .itemOutput(Items.GILDED_BLACKSTONE)
            .save(output)

        // Enchanted Golden Apply
        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Items.GOLDEN_APPLE, 8)
            .catalyst(RagiumItems.GLOW_LENS)
            .itemOutput(Items.ENCHANTED_GOLDEN_APPLE)
            .save(output)

        // Crying Obsidian
        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.CRYING_OBSIDIAN)
            .save(output)

        // Budding Amethyst
        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Items.AMETHYST_BLOCK, 4)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.BUDDING_AMETHYST)
            .save(output)

        // Heavy Core
        HTSingleItemRecipeBuilder
            .laser(lookup)
            .itemInput(Tags.Items.STORAGE_BLOCKS_NETHERITE)
            .catalyst(RagiumItems.MAGICAL_LENS)
            .itemOutput(Items.HEAVY_CORE)
            .save(output)
    }

    //    Mixer    //

    private fun mixer(output: RecipeOutput) {
        // Obsidian
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .waterInput()
            .fluidInput(Tags.Fluids.LAVA)
            .itemOutput(Items.OBSIDIAN)
            .save(output)
    }

    //    Refinery    //

    private fun refinery(output: RecipeOutput) {
        // Coal -> Crude Oil
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.COAL)
            .fluidOutput(RagiumFluids.CRUDE_OIL, 125)
            .save(output, RagiumAPI.id("crude_oil_from_coal"))
        // Soul XX -> Crude Oil
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .itemOutput(Items.SAND)
            .fluidOutput(RagiumFluids.CRUDE_OIL, 250)
            .save(output, RagiumAPI.id("crude_oil_from_soul"))
        // Crude Oil -> Naphtha + Aromatic Compound
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumFluids.CRUDE_OIL, 100)
            .fluidOutput(RagiumVirtualFluids.AROMATIC_COMPOUND, 30)
            .fluidOutput(RagiumVirtualFluids.NAPHTHA, 60)
            .save(output)
        // Naphtha -> Polymer Resin + Fuel
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.NAPHTHA.commonTag)
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
                .compressor(lookup)
                .itemInput(RagiumItems.POLYMER_RESIN)
                .catalyst(catalyst)
                .itemOutput(result)
                .saveSuffixed(output, "_from_polymer")
        }

        // Biomass -> Alcohol
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.BIOMASS.commonTag)
            .fluidOutput(RagiumVirtualFluids.ETHANOL)
            .save(output)
        // Alcohol + Plant Oil -> Crude Biodiesel
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.ETHANOL.commonTag, 400)
            .fluidInput(RagiumVirtualFluids.PLANT_OIL.commonTag, 100)
            .fluidOutput(RagiumVirtualFluids.CRUDE_BIODIESEL, 500)
            .save(output)
        // Crude Biodiesel -> Biodiesel + Glycerin
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.CRUDE_BIODIESEL.commonTag, 500)
            .fluidOutput(RagiumVirtualFluids.BIODIESEL, 400)
            .fluidOutput(RagiumVirtualFluids.GLYCEROL, 100)
            .save(output)
        // Glycerin + Mixture Acid
        HTFluidOutputRecipeBuilder
            .mixer(lookup)
            .fluidInput(RagiumVirtualFluids.GLYCEROL.commonTag, 100)
            .fluidInput(RagiumVirtualFluids.MIXTURE_ACID.commonTag, 300)
            .fluidOutput(RagiumVirtualFluids.NITROGLYCERIN, 100)
            .save(output)

        // XX Log -> Pulp + Sap
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(ItemTags.LOGS_THAT_BURN)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.WOOD), 4)
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)
        // Sap -> Slimeball + Latex
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.SAP.commonTag)
            .itemOutput(Items.SLIME_BALL)
            .fluidOutput(RagiumVirtualFluids.LATEX, 250)
            .saveSuffixed(output, "_from_sap")
        // Latex -> Raw Rubber
        HTSolidifierRecipeBuilder(lookup)
            .fluidInput(RagiumVirtualFluids.LATEX.commonTag, 250)
            .catalyst(RagiumItems.BALL_PRESS_MOLD)
            .itemOutput(Items.SLIME_BALL)
            .saveSuffixed(output, "_from_latex")

        // Crimson Stem -> Crimson Sap
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(ItemTags.CRIMSON_STEMS)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.WOOD), 4)
            .fluidOutput(RagiumVirtualFluids.CRIMSON_SAP)
            .savePrefixed(output, "crimson_")
        // Crimson Sap -> Crimson Crystal + Sap
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.CRIMSON_SAP.commonTag)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.CRIMSON_CRYSTAL))
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)

        // Warped Stem -> Warped Sap
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(ItemTags.WARPED_STEMS)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.DUST, CommonMaterials.WOOD), 4)
            .fluidOutput(RagiumVirtualFluids.WARPED_SAP)
            .savePrefixed(output, "warped_")
        // Warped Sap -> Warped Crystal + Sap
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumVirtualFluids.WARPED_SAP.commonTag)
            .itemOutput(RagiumItems.getMaterialItem(HTTagPrefix.GEM, RagiumMaterials.WARPED_CRYSTAL))
            .fluidOutput(RagiumVirtualFluids.SAP)
            .save(output)

        // Creosote -> Slime Ball + Fuel
        HTFluidOutputRecipeBuilder
            .refinery(lookup)
            .fluidInput(RagiumFluidTags.CREOSOTE)
            .itemOutput(Items.SLIME_BALL)
            .fluidOutput(RagiumVirtualFluids.FUEL)
            .saveSuffixed(output, "_from_creosote")
    }

    //    Solidifier    //

    private fun solidifier(output: RecipeOutput) {
        // Laval + Cobblestone -> Magma Block
        HTSolidifierRecipeBuilder(lookup)
            .fluidInput(Tags.Fluids.LAVA)
            .catalyst(Tags.Items.COBBLESTONES)
            .itemOutput(Items.MAGMA_BLOCK)
            .save(output)
    }
}
