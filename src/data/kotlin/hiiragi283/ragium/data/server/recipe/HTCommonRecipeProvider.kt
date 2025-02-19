package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.*
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
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.DeferredItem

object HTCommonRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerRaginite(output)
        registerSteels(output)
        registerRagium(output)

        registerCircuits(output)
        registerPressMolds(output)
        registerLens(output)

        registerArmor(output)
        registerTool(output)
        registerMisc(output)
    }

    private fun registerRaginite(output: RecipeOutput) {
        val ragiAlloy: DeferredItem<out Item> =
            RagiumItems.getMaterialItem(HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)

        // Ragi-Alloy
        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow8()
            .define('A', HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.RAGI_ALLOY_COMPOUND)
            .hollow4()
            .define('A', HTTagPrefix.DUST, RagiumMaterials.RAGINITE)
            .define('B', HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .save(output, RagiumAPI.id("ragi_alloy_compound_alt"))

        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.RAGI_ALLOY_COMPOUND),
                ragiAlloy,
                exp = 0.5f,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).save(output)

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
            .itemInput(HTTagPrefix.GEM, CommonMaterials.COAL_COKE)
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

    private fun registerRagium(output: RecipeOutput) {
        // Ragium
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.DUST, RagiumMaterials.RAGI_CRYSTAL, 8)
            .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 1000)
            .fluidOutput(RagiumVirtualFluids.RAGIUM_SOLUTION)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION.commonTag, 8000)
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
            null,
        )
        // Ragi Ticket
        HTFluidOutputRecipeBuilder
            .infuser()
            .itemInput(Items.PAPER)
            .fluidInput(RagiumVirtualFluids.RAGIUM_SOLUTION.commonTag, 125)
            .itemOutput(RagiumItems.RAGI_TICKET)
            .save(output)
    }

    private fun registerCircuits(output: RecipeOutput) {
        fun circuit(
            circuit: ItemLike,
            subMetal: HTMaterialKey,
            dopant: TagKey<Item>,
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
            Tags.Items.DUSTS_REDSTONE,
            RagiumItems.REDSTONE_LENS,
        )
        circuit(
            RagiumItems.ADVANCED_CIRCUIT,
            VanillaMaterials.GOLD,
            Tags.Items.DUSTS_GLOWSTONE,
            RagiumItems.GLOW_LENS,
        )
        circuit(
            RagiumItems.ELITE_CIRCUIT,
            CommonMaterials.ALUMINUM,
            Tags.Items.GEMS_PRISMARINE,
            RagiumItems.PRISMARINE_LENS,
        )
        circuit(
            RagiumItems.ULTIMATE_CIRCUIT,
            RagiumMaterials.RAGIUM,
            HTTagPrefix.DUST.createTag(RagiumMaterials.RAGI_CRYSTAL),
            RagiumItems.MAGICAL_LENS,
        )

        HTShapedRecipeBuilder(RagiumItems.BASIC_CIRCUIT)
            .pattern(" A ")
            .pattern("BCB")
            .pattern(" A ")
            .define('A', Tags.Items.INGOTS_COPPER)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', ItemTags.PLANKS)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.ADVANCED_CIRCUIT)
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', Tags.Items.GEMS_LAPIS)
            .define('B', Tags.Items.DUSTS_REDSTONE)
            .define('C', Tags.Items.DUSTS_GLOWSTONE)
            .define('D', RagiumItemTags.BASIC_CIRCUIT)
            .save(output)
    }

    private fun registerPressMolds(output: RecipeOutput) {
        fun register(mold: ItemLike) {
            SingleItemRecipeBuilder
                .stonecutting(
                    Ingredient.of(RagiumItems.BLANK_PRESS_MOLD),
                    RecipeCategory.MISC,
                    mold,
                ).unlockedBy("has_steel", has(RagiumItems.BLANK_PRESS_MOLD))
                .savePrefixed(output)
        }

        HTShapedRecipeBuilder(RagiumItems.BLANK_PRESS_MOLD)
            .pattern(
                "AA",
                "AA",
                "B ",
            ).define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItems.FORGE_HAMMER)
            .save(output)

        register(RagiumItems.BALL_PRESS_MOLD)
        RagiumItems.PRESS_MOLDS.values.forEach(::register)
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
            .itemInput(Tags.Items.DUSTS_GLOWSTONE, 64)
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

    private fun registerArmor(output: RecipeOutput) {
        // Diving Goggles
        HTShapedRecipeBuilder(RagiumItems.DIVING_GOGGLE, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "AAA",
                "ABA",
                "C C",
            ).define('A', RagiumItemTags.PLASTICS)
            .define('B', Tags.Items.GLASS_PANES)
            .define('C', RagiumItemTags.ADVANCED_CIRCUIT)
            .save(output)
        // Jetpack
        HTShapedRecipeBuilder(RagiumItems.JETPACK, category = CraftingBookCategory.EQUIPMENT)
            .pattern(
                "ABA",
                "ACA",
                "D D",
            ).define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', RagiumItemTags.ELITE_CIRCUIT)
            .define('C', ItemTags.CHEST_ARMOR)
            .define('D', HTTagPrefix.GEM, RagiumMaterials.RAGI_CRYSTAL)
            .save(output)
    }

    private fun registerTool(output: RecipeOutput) {
        HTShapedRecipeBuilder(RagiumItems.FORGE_HAMMER, category = CraftingBookCategory.EQUIPMENT)
            .pattern(" AA")
            .pattern("BBA")
            .pattern(" AA")
            .define('A', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.SILKY_CRYSTAL)
            .cross8()
            .define('A', ItemTags.WOOL)
            .define('B', Items.PAPER)
            .define('C', HTTagPrefix.GEM, VanillaMaterials.EMERALD)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.SILKY_PICKAXE, category = CraftingBookCategory.EQUIPMENT)
            .pattern("AAA")
            .pattern(" B ")
            .pattern(" B ")
            .define('A', RagiumItems.SILKY_CRYSTAL)
            .define('B', Tags.Items.RODS_WOODEN)
            .save(output)

        HTShapedRecipeBuilder(RagiumItems.MAGNET, category = CraftingBookCategory.EQUIPMENT)
            .pattern("A A")
            .pattern("B B")
            .pattern(" B ")
            .define('A', HTTagPrefix.INGOT, CommonMaterials.STEEL)
            .define('B', HTTagPrefix.INGOT, RagiumMaterials.RAGI_ALLOY)
            .save(output)
    }

    private fun registerMisc(output: RecipeOutput) {
        HTShapedRecipeBuilder(RagiumItems.SOLAR_PANEL)
            .pattern("AAA")
            .pattern("BBB")
            .pattern("CCC")
            .define('A', Tags.Items.GLASS_PANES)
            .define('B', HTTagPrefix.DUST, VanillaMaterials.LAPIS)
            .define('C', HTTagPrefix.INGOT, CommonMaterials.ALUMINUM)
            .save(output)

        HTMultiItemRecipeBuilder
            .assembler()
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
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

        HTShapedRecipeBuilder(RagiumBlocks.SLAG_BLOCK)
            .hollow8()
            .define('A', RagiumItemTags.SLAG)
            .define('B', RagiumItems.SLAG)
            .save(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.MISC, RagiumItems.SLAG, 9)
            .requires(RagiumBlocks.SLAG_BLOCK)
            .unlockedBy("has_slag", has(RagiumBlocks.SLAG_BLOCK))
            .savePrefixed(output)

        HTShapedRecipeBuilder(RagiumBlocks.SHAFT, 6, CraftingBookCategory.BUILDING)
            .pattern("A")
            .pattern("A")
            .define('A', HTTagPrefix.STORAGE_BLOCK, VanillaMaterials.IRON)
            .save(output)
    }
}
