package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.condition.HTTemperatureCondition
import hiiragi283.ragium.data.define
import hiiragi283.ragium.data.savePrefixed
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.BlockTagIngredient
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier

object HTIngredientRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerRaginite(output)
        registerSteels(output)
        registerEndContents(output)

        registerPlastics(output)
        registerCircuits(output)

        registerCatalysts(output)
        registerPressMolds(output)

        registerTool(output)
        registerMisc(output)

        registerSnow(output)
        registerStone(output)
    }

    private fun registerRaginite(output: RecipeOutput) {
        // Ragi-Alloy
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.RAW_MATERIAL, RagiumMaterialKeys.CRUDE_RAGINITE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', HTTagPrefix.DUST, RagiumMaterialKeys.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.DUST, RagiumMaterialKeys.CRUDE_RAGINITE))
            .save(output, RagiumAPI.id("shaped/ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                RagiumItems.Ingots.RAGI_ALLOY,
                exp = 0.5f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_compound", has(RagiumItems.RAGI_ALLOY_COMPOUND))
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGINITE)
            .itemOutput(RagiumItems.Ingots.RAGI_ALLOY)
            .save(output)
        // Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .itemInput(RagiumItems.Dusts.CRUDE_RAGINITE, 8)
            .itemInput(RagiumItems.SOAP)
            .waterInput()
            .itemOutput(RagiumItems.Dusts.RAGINITE, 6)
            .itemOutput(RagiumItems.SLAG, 2)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGINITE, 4)
            .itemOutput(RagiumItems.Ingots.RAGI_STEEL)
            .save(output)
        // Refined Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.Dusts.RAGINITE, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE, 5)
            .itemOutput(RagiumItems.Dusts.RAGI_CRYSTAL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL)
            .itemInput(HTTagPrefix.DUST, RagiumMaterialKeys.RAGI_CRYSTAL, 4)
            .itemOutput(RagiumItems.Ingots.REFINED_RAGI_STEEL)
            .save(output)
    }

    private fun registerSteels(output: RecipeOutput) {
        // Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(RagiumItems.Ingots.STEEL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .itemInput(ItemTags.COALS, 2)
            .catalyst(RagiumItems.REDUCTION_CATALYST)
            .itemOutput(RagiumItems.Ingots.STEEL)
            .saveSuffixed(output, "_alt")
        // Deep Steel
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR, HTMachineTier.ADVANCED)
            .itemInput(BlockTagIngredient(BlockTags.DEEPSLATE_ORE_REPLACEABLES), 8)
            .fluidInput(RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME / 5)
            .itemOutput(RagiumItems.DEEPANT)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 5)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL)
            .itemInput(RagiumItems.DEEPANT, 4)
            .itemOutput(RagiumItems.Ingots.DEEP_STEEL)
            .save(output)
    }

    private fun registerEndContents(output: RecipeOutput) {
        // Ragium
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER, HTMachineTier.ELITE)
            .itemInput(RagiumItems.Dusts.RAGI_CRYSTAL, 4)
            .fluidInput(RagiumFluids.AQUA_REGIA)
            .fluidOutput(RagiumFluids.RAGIUM_SOLUTION)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RAGIUM_SOLUTION)
            .catalyst(RagiumItems.Circuits.ELITE)
            .fluidOutput(RagiumFluids.DISTILLED_RAGIUM_SOLUTION, 750)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, 250)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.DISTILLED_RAGIUM_SOLUTION, 750)
            .catalyst(RagiumItems.REDUCTION_CATALYST)
            .fluidOutput(RagiumFluids.REFINED_RAGIUM_SOLUTION, 500)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .fluidInput(RagiumFluids.REFINED_RAGIUM_SOLUTION, 500)
            .fluidOutput(RagiumFluids.DESTABILIZED_RAGIUM_SOLUTION, 250)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.IRON)
            .fluidInput(RagiumFluids.DESTABILIZED_RAGIUM_SOLUTION)
            .itemOutput(RagiumItems.Ingots.RAGIUM)
            .save(output)
        // Dragonium
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.DRAGON_BREATH)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluids.DRAGON_BREATH, FluidType.BUCKET_VOLUME / 250)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.BLAST_FURNACE)
            .itemInput(RagiumItems.Ingots.DEEP_STEEL)
            .fluidInput(RagiumFluids.DRAGON_BREATH)
            .catalyst(Items.DRAGON_EGG)
            .itemOutput(RagiumItems.Ingots.DRAGONIUM)
            .save(output)

        // Unbreakable Elytra
        val elytraId: ResourceLocation = RagiumAPI.id("smithing/dragonium_elytra")
        val ingotDragonium: TagKey<Item> = RagiumItems.Ingots.DRAGONIUM.prefixedTagKey
        output.accept(
            elytraId,
            SmithingTransformRecipe(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Items.ELYTRA),
                Ingredient.of(ingotDragonium),
                ItemStack(Items.ELYTRA).apply {
                    set(DataComponents.UNBREAKABLE, Unbreakable(true))
                },
            ),
            output
                .advancement()
                .addCriterion("has_dragonium", HTMaterialRecipeProvider.has(ingotDragonium))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(elytraId))
                .requirements(AdvancementRequirements.Strategy.OR)
                .rewards(AdvancementRewards.Builder.recipe(elytraId))
                .build(elytraId.withPrefix("recipes/combat/")),
        )
        // Echorium

        // Fierium
    }

    private fun registerPlastics(output: RecipeOutput) {
        fun register(result: ItemLike, builder: Supplier<HTMachineRecipeBuilder>) {
            builder
                .get()
                .itemOutput(result)
                .save(output)
            builder
                .get()
                .catalyst(RagiumItems.OXIDIZATION_CATALYST)
                .itemOutput(result, 2)
                .saveSuffixed(output, "_alt")
        }

        register(RagiumItems.Plastics.BASIC) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ETHENE)
        }
        register(RagiumItems.Plastics.ADVANCED) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.ACETYLENE)
                .fluidInput(RagiumFluids.CHLORINE)
        }
        register(RagiumItems.Plastics.ELITE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.PROPENE)
        }
        register(RagiumItems.Plastics.ULTIMATE) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.CHEMICAL_REACTOR)
                .fluidInput(RagiumFluids.AROMATIC_COMPOUNDS)
                .fluidInput(RagiumFluids.NITRIC_ACID)
        }

        RagiumItems.Plastics.entries.forEach { plastic: RagiumItems.Plastics ->
            // Circuit Board
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.COMPRESSOR)
                .itemInput(plastic)
                .itemInput(RagiumItems.Dusts.QUARTZ)
                .catalyst(RagiumItems.PLATE_PRESS_MOLD)
                .itemOutput(RagiumItems.CIRCUIT_BOARD, plastic.ordinal + 1)
                .savePrefixed(output, "${plastic.machineTier.serializedName}_")
        }
    }

    private fun registerCircuits(output: RecipeOutput) {
        RagiumItems.Circuits.entries.forEach { circuit: RagiumItems.Circuits ->
            // Assembler
            val dust: ItemLike = when (circuit) {
                RagiumItems.Circuits.BASIC -> Items.REDSTONE
                RagiumItems.Circuits.ADVANCED -> Items.GLOWSTONE_DUST
                RagiumItems.Circuits.ELITE -> RagiumItems.LUMINESCENCE_DUST
                RagiumItems.Circuits.ULTIMATE -> RagiumItems.Dusts.RAGI_CRYSTAL
            }

            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.ASSEMBLER, circuit.machineTier)
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, circuit.machineTier.getSubMetal())
                .itemInput(dust)
                .itemOutput(circuit)
                .save(output)
        }

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.BASIC)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.Circuits.ADVANCED)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', RagiumItems.Circuits.BASIC)
            .unlockedBy("has_circuit", has(RagiumItems.Circuits.BASIC))
            .savePrefixed(output)
    }

    private fun registerCatalysts(output: RecipeOutput) {
        fun register(catalyst: ItemLike, corner: HTMaterialKey, edge: ItemLike) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, catalyst)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', HTTagPrefix.STORAGE_BLOCK, corner)
                .define('B', edge)
                .define('C', Items.IRON_BARS)
                .unlockedBy("has_iron_bars", has(Items.IRON_BARS))
                .savePrefixed(output)
        }

        // register(RagiumItems.HEATING_CATALYST, RagiumMaterialKeys.COPPER, Items.MAGMA_BLOCK)
        // register(RagiumItems.COOLING_CATALYST, RagiumMaterialKeys.ALUMINUM, Items.PACKED_ICE)
        register(RagiumItems.OXIDIZATION_CATALYST, RagiumMaterialKeys.IRON, Items.COAL_BLOCK)
        register(RagiumItems.REDUCTION_CATALYST, RagiumMaterialKeys.GOLD, Items.WATER_BUCKET)
        register(RagiumItems.DEHYDRATION_CATALYST, RagiumMaterialKeys.STEEL, Items.SOUL_SAND)
    }

    private fun registerPressMolds(output: RecipeOutput) {
        fun register(pressMold: ItemLike, prefix: HTTagPrefix) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, pressMold)
                .pattern("AA")
                .pattern("AA")
                .pattern("BC")
                .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL)
                .define('B', RagiumItems.FORGE_HAMMER)
                .define('C', prefix.commonTagKey)
                .unlockedBy("has_steel", has(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL))
                .savePrefixed(output)
        }

        register(RagiumItems.GEAR_PRESS_MOLD, HTTagPrefix.GEAR)
        register(RagiumItems.ROD_PRESS_MOLD, HTTagPrefix.ROD)
        register(RagiumItems.PLATE_PRESS_MOLD, HTTagPrefix.PLATE)
    }

    private fun registerTool(output: RecipeOutput) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.TOOLS, RagiumItems.FORGE_HAMMER)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.TOOLS, RagiumItems.SILKY_CRYSTAL)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', ItemTags.WOOL)
            .define('B', Items.PAPER)
            .define('C', HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD)
            .unlockedBy("has_emerald", has(HTTagPrefix.GEM, RagiumMaterialKeys.EMERALD))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.TOOLS, RagiumItems.SILKY_PICKAXE)
            .pattern("AAA")
            .pattern(" B ")
            .pattern(" B ")
            .define('A', RagiumItems.SILKY_CRYSTAL)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_crystal", has(RagiumItems.SILKY_CRYSTAL))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.SLOT_LOCK, 3)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("AAA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .define('B', Tags.Items.DYES_WHITE)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_ALLOY))
            .savePrefixed(output)
    }

    private fun registerMisc(output: RecipeOutput) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.SOLAR_PANEL)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.GLASS_PANES)
            .define('B', HTTagPrefix.DUST, RagiumMaterialKeys.LAPIS)
            .define('C', HTTagPrefix.INGOT, RagiumMaterialKeys.ALUMINUM)
            .unlockedBy("has_aluminum", has(HTTagPrefix.INGOT, RagiumMaterialKeys.ALUMINUM))
            .savePrefixed(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(RagiumItems.LUMINESCENCE_DUST)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.COPPER)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .itemOutput(RagiumItems.LED, 4)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(
                Ingredient.of(ItemTags.COALS),
                RecipeCategory.MISC,
                RagiumItems.COAL_CHIP,
                8,
            ).unlockedBy("has_coal", has(ItemTags.COALS))
            .savePrefixed(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.STEEL, 4)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterialKeys.RAGI_STEEL, 4)
            .itemInput(Items.PISTON, 2)
            .itemOutput(RagiumItems.ENGINE)
            .save(output)

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.CRIMSON_CRYSTAL),
                Items.BLAZE_POWDER,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.CRIMSON_CRYSTAL))
            .save(output)

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.WARPED_CRYSTAL),
                Items.ENDER_PEARL,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.WARPED_CRYSTAL))
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.GLOW_INK_SAC)
            .itemOutput(RagiumItems.LUMINESCENCE_DUST)
            .itemOutput(Items.INK_SAC)
            .save(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.SOUL_MAGMA_BLOCK)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .define('B', RagiumItems.CALCIUM_CARBIDE)
            .define('C', Items.MAGMA_BLOCK)
            .unlockedBy("has_carbide", has(RagiumItems.CALCIUM_CARBIDE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.SHAFT, 6)
            .pattern("A")
            .pattern("A")
            .define('A', HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON)
            .unlockedBy("has_iron_block", has(HTTagPrefix.STORAGE_BLOCK, RagiumMaterialKeys.IRON))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.CHEMICAL_GLASS)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', Tags.Items.DYES_YELLOW)
            .define('B', Tags.Items.DYES_BLACK)
            .define('C', Tags.Items.GLASS_BLOCKS)
            .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS))
            .savePrefixed(output)
    }

    private fun registerSnow(output: RecipeOutput) {
        // Water -> Ice
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .waterInput()
            .machineConditions(HTTemperatureCondition.cooling(HTMachineTier.BASIC))
            .itemOutput(Items.SNOW_BLOCK)
            .save(output)

        // Snow Block -> 4x Snow Ball
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.SNOW_BLOCK)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_block")
        // Ice -> 4x Snow Ball
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.ICE)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_ice")

        // Powder Snow
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .waterInput()
            .machineConditions(HTTemperatureCondition.cooling(HTMachineTier.ADVANCED))
            .fluidOutput(RagiumFluids.SNOW)
            .saveSuffixed(output, "_from_water")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Tags.Items.BUCKETS_POWDER_SNOW)
            .itemOutput(Items.BUCKET)
            .fluidOutput(RagiumFluids.SNOW)
            .save(output, RagiumAPI.id("powder_snow_from_bucket"))

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.ASSEMBLER)
            .itemInput(Items.BUCKET)
            .fluidInput(RagiumFluids.SNOW)
            .itemOutput(Items.POWDER_SNOW_BUCKET)
            .save(output)
    }

    private fun registerStone(output: RecipeOutput) {
        fun registerRock(rock: ItemLike) {
            HTMachineRecipeBuilder
                .create(RagiumMachineKeys.MIXER)
                .waterInput(1)
                .fluidInput(Tags.Fluids.LAVA, 1)
                .catalyst(rock)
                .itemOutput(rock, 8)
                .save(output)
        }

        registerRock(Items.STONE)
        registerRock(Items.COBBLESTONE)
        registerRock(Items.GRANITE)
        registerRock(Items.DIORITE)
        registerRock(Items.ANDESITE)

        registerRock(Items.DEEPSLATE)
        registerRock(Items.COBBLED_DEEPSLATE)
        registerRock(Items.CALCITE)
        registerRock(Items.TUFF)
        registerRock(Items.DRIPSTONE_BLOCK)
        registerRock(Items.NETHERRACK)
        registerRock(Items.BASALT)
        registerRock(Items.BLACKSTONE)

        registerRock(Items.END_STONE)

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MIXER)
            .waterInput()
            .fluidInput(Tags.Fluids.LAVA)
            .catalyst(Items.OBSIDIAN)
            .itemOutput(Items.OBSIDIAN)
            .save(output)
    }
}
