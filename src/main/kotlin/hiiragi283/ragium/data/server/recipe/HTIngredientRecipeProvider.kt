package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
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
import net.neoforged.neoforge.registries.DeferredItem

object HTIngredientRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerRaginite(output)
        registerSteels(output)
        registerEndContents(output)

        registerCircuits(output)
        registerPressMolds(output)

        registerTool(output)
        registerMisc(output)
    }

    private fun registerRaginite(output: RecipeOutput) {
        val ragiAlloy: DeferredItem<out Item> =
            RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)

        // Ragi-Alloy
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterials.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.CRUDE_RAGINITE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', HTTagPrefix.DUST, RagiumMaterials.CRUDE_RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .unlockedBy("has_crude_raginite", has(HTTagPrefix.DUST, RagiumMaterials.CRUDE_RAGINITE))
            .save(output, RagiumAPI.id("shaped/ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                ragiAlloy,
                exp = 0.5f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_compound", has(RagiumItems.RAGI_ALLOY_COMPOUND))
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .itemOutput(ragiAlloy)
            .save(output)
        // Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.CRUDE_RAGINITE, 8)
            .itemInput(RagiumItems.SOAP)
            .waterInput()
            .itemOutput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE, 6)
            .itemOutput(RagiumItems.SLAG, 2)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE, 4)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL)
            .save(output)
        // Refined Ragi-Steel
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE, 5)
            .catalyst(HTMachineTier.ADVANCED)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 4)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.REFINED_RAGI_STEEL)
            .save(output)
    }

    private fun registerSteels(output: RecipeOutput) {
        // Steel
        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .save(output)
        // Deep Steel
        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .itemInput(BlockTagIngredient(BlockTags.DEEPSLATE_ORE_REPLACEABLES), 8)
            .fluidInput(RagiumFluids.AQUA_REGIA, FluidType.BUCKET_VOLUME / 5)
            .itemOutput(RagiumItems.DEEPANT)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, FluidType.BUCKET_VOLUME / 5)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .itemInput(RagiumItems.DEEPANT, 4)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .save(output)
    }

    private fun registerEndContents(output: RecipeOutput) {
        // Ragium
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 4)
            .fluidInput(RagiumFluids.AQUA_REGIA)
            .fluidOutput(RagiumFluids.RAGIUM_SOLUTION)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.DISTILLATION_TOWER)
            .fluidInput(RagiumFluids.RAGIUM_SOLUTION)
            .catalyst(HTMachineTier.ELITE)
            .fluidOutput(RagiumFluids.DISTILLED_RAGIUM_SOLUTION, 750)
            .fluidOutput(RagiumFluids.CHEMICAL_SLUDGE, 250)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.CHEMICAL_REACTOR)
            .fluidInput(RagiumFluids.DISTILLED_RAGIUM_SOLUTION, 750)
            .catalyst(Items.NETHER_STAR)
            .fluidOutput(RagiumFluids.REFINED_RAGIUM_SOLUTION, 500)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.LASER_TRANSFORMER)
            .fluidInput(RagiumFluids.REFINED_RAGIUM_SOLUTION, 500)
            .fluidOutput(RagiumFluids.DESTABILIZED_RAGIUM_SOLUTION, 250)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.BLAST_FURNACE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .fluidInput(RagiumFluids.DESTABILIZED_RAGIUM_SOLUTION)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)
            .save(output)
        // Dragonium
        HTMachineRecipeBuilder
            .create(RagiumRecipes.EXTRACTOR)
            .itemInput(Items.DRAGON_BREATH)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluids.DRAGON_BREATH, FluidType.BUCKET_VOLUME / 250)
            .save(output)

        // Unbreakable Elytra
        val elytraId: ResourceLocation = RagiumAPI.id("smithing/dragonium_elytra")
        val ingotDragonium: TagKey<Item> = HTTagPrefix.INGOT.createTag(VanillaMaterials.NETHERITE)
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
                .addCriterion("has_dragonium", has(ingotDragonium))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(elytraId))
                .requirements(AdvancementRequirements.Strategy.OR)
                .rewards(AdvancementRewards.Builder.recipe(elytraId))
                .build(elytraId.withPrefix("recipes/combat/")),
        )
        // Echorium

        // Fierium
    }

    private fun registerCircuits(output: RecipeOutput) {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            // Assembler
            val dust: ItemLike = when (tier) {
                HTMachineTier.BASIC -> Items.REDSTONE
                HTMachineTier.ADVANCED -> Items.GLOWSTONE_DUST
                HTMachineTier.ELITE -> RagiumItems.LUMINESCENCE_DUST
                HTMachineTier.ULTIMATE -> RagiumItems.getMaterialItem(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL)
            }

            HTMachineRecipeBuilder
                .create(RagiumRecipes.ASSEMBLER)
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, tier.getSubMetal())
                .itemInput(dust)
                .catalyst(tier)
                .itemOutput(tier.getCircuit())
                .save(output)
        }

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.BASIC_CIRCUIT)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.ADVANCED_CIRCUIT)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', HTMachineTier.BASIC.getCircuitTag())
            .unlockedBy("has_circuit", has(HTMachineTier.BASIC.getCircuitTag()))
            .savePrefixed(output)
    }

    private fun registerPressMolds(output: RecipeOutput) {
        fun register(pressMold: ItemLike, prefix: HTTagPrefix) {
            ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, pressMold)
                .pattern("AA")
                .pattern("AA")
                .pattern("BC")
                .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
                .define('B', RagiumItems.FORGE_HAMMER)
                .define('C', prefix.commonTagKey)
                .unlockedBy("has_steel", has(HTTagPrefix.INGOT, CommonMaterials.STEEL))
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
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.TOOLS, RagiumItems.SILKY_CRYSTAL)
            .pattern("ABA")
            .pattern("BCB")
            .pattern("ABA")
            .define('A', ItemTags.WOOL)
            .define('B', Items.PAPER)
            .define('C', HTTagPrefix.GEM, VanillaMaterials.EMERALD)
            .unlockedBy("has_emerald", has(HTTagPrefix.GEM, VanillaMaterials.EMERALD))
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
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.DYES_WHITE)
            .unlockedBy("has_ragi_alloy", has(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY))
            .savePrefixed(output)
    }

    private fun registerMisc(output: RecipeOutput) {
        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.SOLAR_PANEL)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.GLASS_PANES)
            .define('B', HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .define('C', HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .unlockedBy("has_aluminum", has(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM))
            .savePrefixed(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(RagiumItems.LUMINESCENCE_DUST)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .itemOutput(RagiumItems.LED, 4)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 4)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.RAGI_STEEL, 4)
            .itemInput(Items.PISTON, 2)
            .catalyst(HTMachineTier.ADVANCED)
            .itemOutput(RagiumItems.ENGINE)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.EXTRACTOR)
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
            .define('A', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON)
            .unlockedBy("has_iron_block", has(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
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
}
