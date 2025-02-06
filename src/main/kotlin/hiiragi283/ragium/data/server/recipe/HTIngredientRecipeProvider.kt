package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTInfuserRecipeBuilder
import hiiragi283.ragium.api.data.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.ResourceLocation
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
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .unlockedBy("has_raginite", has(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumItems.RAGI_ALLOY_COMPOUND)
            .pattern(" A ")
            .pattern("ABA")
            .pattern(" A ")
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .unlockedBy("has_raginite", has(HTTagPrefix.DUST, RagiumMaterials.RAGINITE))
            .save(output, RagiumAPI.id("shaped/ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                ragiAlloy,
                exp = 0.5f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_compound", has(RagiumItems.RAGI_ALLOY_COMPOUND))
            .save(output)

        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .itemOutput(ragiAlloy)
            .save(output)
        // Refined Ragi-Steel
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGINITE, 4)
            .itemInput(Tags.Items.DUSTS_REDSTONE, 5)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)
    }

    private fun registerSteels(output: RecipeOutput) {
        // Steel
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(ItemTags.COALS, 2)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .save(output)

        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(RagiumItemTags.COAL_COKE)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .saveSuffixed(output, "_with_coke")
        // Deep Steel
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .itemInput(RagiumItems.DEEPANT_REAGENT, 4)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL)
            .save(output)
    }

    private fun registerEndContents(output: RecipeOutput) {
        // Ragium
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 8)
            .fluidInput(RagiumVirtualFluids.LAPIS_SOLUTION, FluidType.BUCKET_VOLUME * 8)
            .itemOutput(RagiumItems.RAGIUM_REAGENT)
            .save(output)

        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(RagiumItems.RAGIUM_REAGENT, 64)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.RAGIUM)
            .save(output)

        // Unbreakable Elytra
        val elytraId: ResourceLocation = RagiumAPI.id("smithing/ragi_elytra")
        val ragiumIngot: TagKey<Item> = HTTagPrefix.INGOT.createTag(RagiumMaterials.RAGIUM)
        output.accept(
            elytraId,
            SmithingTransformRecipe(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(Items.ELYTRA),
                Ingredient.of(ragiumIngot),
                ItemStack(Items.ELYTRA).apply {
                    set(DataComponents.UNBREAKABLE, Unbreakable(true))
                },
            ),
            output
                .advancement()
                .addCriterion("has_ragium", has(ragiumIngot))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(elytraId))
                .requirements(AdvancementRequirements.Strategy.OR)
                .rewards(AdvancementRewards.Builder.recipe(elytraId))
                .build(elytraId.withPrefix("recipes/combat/")),
        )
        // Echorium
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .itemInput(RagiumItems.SCULK_REAGENT, 16)
            .itemOutput(HTTagPrefix.INGOT, RagiumMaterials.ECHORIUM)
            .save(output)

        // Fiery Coal
        HTInfuserRecipeBuilder()
            .itemInput(RagiumItems.BLAZE_REAGENT, 8)
            .fluidInput(RagiumFluids.CRUDE_OIL)
            .itemOutput(HTTagPrefix.GEM, RagiumMaterials.FIERY_COAL)
            .save(output)
    }

    private fun registerCircuits(output: RecipeOutput) {
        fun circuit(circuit: ItemLike, subMetal: HTMaterialKey, dopant: Ingredient) {
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, subMetal)
                .itemInput(dopant)
                .itemOutput(circuit)
                .save(output)
        }

        circuit(RagiumItems.BASIC_CIRCUIT, VanillaMaterials.COPPER, Ingredient.of(Tags.Items.DUSTS_REDSTONE))
        circuit(RagiumItems.ADVANCED_CIRCUIT, VanillaMaterials.GOLD, Ingredient.of(RagiumItems.GLOW_REAGENT))
        circuit(RagiumItems.ELITE_CIRCUIT, CommonMaterials.ALUMINUM, Ingredient.of(RagiumItems.PRISMARINE_REAGENT))
        circuit(RagiumItems.ULTIMATE_CIRCUIT, RagiumMaterials.RAGIUM, Ingredient.of(RagiumItems.RAGIUM_REAGENT))

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
            .define('D', RagiumItemTags.BASIC_CIRCUIT)
            .unlockedBy("has_circuit", has(RagiumItemTags.BASIC_CIRCUIT))
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
        register(RagiumItems.PLATE_PRESS_MOLD, HTTagPrefix.PLATE)
        register(RagiumItems.ROD_PRESS_MOLD, HTTagPrefix.ROD)
        register(RagiumItems.WIRE_PRESS_MOLD, HTTagPrefix.WIRE)
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
            .shaped(RecipeCategory.MISC, RagiumItems.SLOT_LOCK)
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

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItems.GLOW_REAGENT)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS)
            .itemOutput(RagiumItems.LED, 4)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.STEEL, 4)
            .itemInput(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY, 4)
            .itemInput(Items.PISTON, 2)
            .itemOutput(RagiumItems.ENGINE)
            .save(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.MISC, RagiumBlocks.SLAG_BLOCK)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', RagiumItemTags.SLAG)
            .define('B', RagiumItems.SLAG)
            .unlockedBy("has_slag", has(RagiumItemTags.SLAG))
            .savePrefixed(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.SLAG, 9)
            .requires(RagiumBlocks.SLAG_BLOCK)
            .unlockedBy("has_slag", has(RagiumBlocks.SLAG_BLOCK))
            .savePrefixed(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumBlocks.SOUL_MAGMA_BLOCK)
            .requires(Items.MAGMA_BLOCK)
            .requires(RagiumItems.SOUL_REAGENT)
            .unlockedBy("has_soul", has(RagiumItems.SOUL_REAGENT))
            .savePrefixed(output)

        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.SHAFT, 6)
            .pattern("A")
            .pattern("A")
            .define('A', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON)
            .unlockedBy("has_iron_block", has(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
            .savePrefixed(output)

        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(Tags.Items.GLASS_BLOCKS, 4)
            .itemInput(Tags.Items.OBSIDIANS_NORMAL)
            .itemOutput(RagiumBlocks.OBSIDIAN_GLASS)
            .save(output)
    }
}
