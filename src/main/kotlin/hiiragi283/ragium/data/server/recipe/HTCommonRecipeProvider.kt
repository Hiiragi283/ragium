package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTInfuserRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.define
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
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

object HTCommonRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerRaginite(output)
        registerSteels(output)
        registerAluminum(output)
        registerRagium(output)

        registerCircuits(output)
        registerPressMolds(output)
        registerLens(output)

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

    private fun registerAluminum(output: RecipeOutput) {
        // Lapis + Water -> Lapis Solution
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .waterInput()
            .fluidOutput(RagiumVirtualFluids.LAPIS_SOLUTION)
            .save(output)

        // 8x Netherrack -> 6x Bauxite + 2x Sulfur
        HTSingleItemRecipeBuilder
            .grinder()
            .itemInput(Items.NETHERRACK, 8)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.BAUXITE, 4)
            .save(output)
        // Bauxite + Lapis solution -> Alumina + Water
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.BAUXITE)
            .fluidInput(RagiumVirtualFluids.LAPIS_SOLUTION)
            .itemOutput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .waterOutput()
            .save(output)
        // Alumina + 4x Coal -> Aluminum Ingot
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(ItemTags.COALS, 4)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .saveSuffixed(output, "_with_coal")

        // Al + HF -> Cryolite
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINUM)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID, FluidType.BUCKET_VOLUME * 6)
            .itemOutput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .save(output)
        // Alumina + Cryolite -> 3x Aluminum Ingot
        HTMultiItemRecipeBuilder
            .blastFurnace()
            .itemInput(HTTagPrefix.DUST, CommonMaterials.ALUMINA)
            .itemInput(HTTagPrefix.GEM, CommonMaterials.CRYOLITE)
            .itemOutput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 3)
            .saveSuffixed(output, "_with_cryolite")
    }

    private fun registerRagium(output: RecipeOutput) {
        // Ragium
        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 8)
            .fluidInput(RagiumVirtualFluids.LAPIS_SOLUTION, FluidType.BUCKET_VOLUME)
            .fluidOutput(RagiumVirtualFluids.RAGIUM_SOLUTION)
            .save(output)

        HTInfuserRecipeBuilder()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION, FluidType.BUCKET_VOLUME * 8)
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
        // Ragi Ticket
        HTInfuserRecipeBuilder()
            .itemInput(Items.PAPER)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION, FluidType.BUCKET_VOLUME / 8)
            .itemOutput(RagiumItems.RAGI_TICKET)
            .save(output)
    }

    private fun registerCircuits(output: RecipeOutput) {
        fun circuit(
            circuit: ItemLike,
            subMetal: HTMaterialKey,
            dopant: ItemLike,
            lens: ItemLike,
        ) {
            // Assembler
            HTMultiItemRecipeBuilder
                .assembler()
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .itemInput(HTTagPrefix.INGOT, subMetal)
                .itemInput(dopant)
                .itemOutput(circuit)
                .save(output)
            // Laser Assembly
            HTSingleItemRecipeBuilder
                .laser()
                .itemInput(RagiumItems.CIRCUIT_BOARD)
                .catalyst(lens)
                .itemOutput(circuit)
                .save(output)
        }

        circuit(
            RagiumItems.BASIC_CIRCUIT,
            VanillaMaterials.COPPER,
            Items.REDSTONE,
            RagiumItems.REDSTONE_LENS,
        )
        circuit(
            RagiumItems.ADVANCED_CIRCUIT,
            VanillaMaterials.GOLD,
            RagiumItems.GLOW_REAGENT,
            RagiumItems.GLOW_LENS,
        )
        circuit(
            RagiumItems.ELITE_CIRCUIT,
            CommonMaterials.ALUMINUM,
            RagiumItems.PRISMARINE_REAGENT,
            RagiumItems.PRISMARINE_LENS,
        )
        circuit(
            RagiumItems.ULTIMATE_CIRCUIT,
            RagiumMaterials.RAGIUM,
            RagiumItems.ENDER_REAGENT,
            RagiumItems.MAGICAL_LENS,
        )

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
        fun register(entry: Map.Entry<HTTagPrefix, ItemLike>) {
            val (prefix: HTTagPrefix, pressMold: ItemLike) = entry
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

        RagiumItems.PRESS_MOLDS.forEach(::register)
    }

    private fun registerLens(output: RecipeOutput) {
        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.DUSTS_REDSTONE, 64)
            .itemInput(Tags.Items.INGOTS_COPPER, 16)
            .itemInput(Tags.Items.GLASS_BLOCKS_COLORLESS, 8)
            .itemOutput(RagiumItems.REDSTONE_LENS)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItems.GLOW_REAGENT, 64)
            .itemInput(Tags.Items.INGOTS_GOLD, 16)
            .itemInput(RagiumBlocks.CHEMICAL_GLASS, 8)
            .itemOutput(RagiumItems.GLOW_LENS)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItems.PRISMARINE_REAGENT, 64)
            .itemInput(HTTagPrefix.INGOT, CommonMaterials.ALUMINUM, 16)
            .itemInput(RagiumBlocks.SOUL_GLASS, 8)
            .itemOutput(RagiumItems.PRISMARINE_LENS)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(RagiumItems.MAGICAL_REAGENT, 64)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.NETHERITE, 16)
            .itemInput(RagiumBlocks.OBSIDIAN_GLASS, 8)
            .itemOutput(RagiumItems.MAGICAL_LENS)
            .save(output)
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
            .shaped(RecipeCategory.TOOLS, RagiumItems.MAGNET)
            .pattern("A A")
            .pattern("B B")
            .pattern(" B ")
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .unlockedBy("has_steel", has(HTTagPrefix.INGOT, CommonMaterials.STEEL))
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

        ShapedRecipeBuilder
            .shaped(RecipeCategory.BUILDING_BLOCKS, RagiumBlocks.SHAFT, 6)
            .pattern("A")
            .pattern("A")
            .define('A', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON)
            .unlockedBy("has_iron_block", has(HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON))
            .savePrefixed(output)
    }
}
