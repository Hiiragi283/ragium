package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.*
import hiiragi283.ragium.api.extension.requiresFor
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags

object HTFoodRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Melon Pie
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(Tags.Items.CROPS_MELON, 4)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .itemOutput(RagiumItems.MELON_PIE)
            .save(output)

        registerChocolate(output)
        registerHoney(output)
        registerMeat(output)
        registerMilk(output)
        registerMushroom(output)
        registerWart(output)
        registerWheat(output)
    }

    private fun registerChocolate(output: RecipeOutput) {
        // Chocolate
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Tags.Items.CROPS_COCOA_BEAN)
            .milkInput()
            .itemOutput(RagiumItems.CHOCOLATE)
            .save(output)
        // Chocolate Apple
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.FOOD, RagiumItems.CHOCOLATE_APPLE)
            .requires(RagiumItems.CHOCOLATE)
            .requires(Items.APPLE)
            .unlockedBy("has_chocolate", has(RagiumItems.CHOCOLATE))
            .savePrefixed(output)
        // Chocolate Bread
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.FOOD, RagiumItems.CHOCOLATE_BREAD)
            .requires(RagiumItems.CHOCOLATE)
            .requires(Items.BREAD)
            .unlockedBy("has_chocolate", has(RagiumItems.CHOCOLATE))
            .savePrefixed(output)
        // Chocolate Cookie
        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.FOOD, RagiumItems.CHOCOLATE_COOKIE, 8)
            .requires(RagiumItems.CHOCOLATE)
            .requires(RagiumItems.FLOUR)
            .requires(RagiumItems.BUTTER)
            .unlockedBy("has_chocolate", has(RagiumItems.CHOCOLATE))
            .savePrefixed(output)

        // Sweet Berries Cake
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .itemInput(RagiumItems.CHOCOLATE, 3)
            .itemInput(Tags.Items.FOODS_BERRY, 2)
            .itemOutput(RagiumBlocks.SWEET_BERRIES_CAKE)
            .save(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.FOOD, RagiumItems.SWEET_BERRIES_CAKE_PIECE, 8)
            .requires(RagiumBlocks.SWEET_BERRIES_CAKE)
            .unlockedBy("has_cake", has(RagiumBlocks.SWEET_BERRIES_CAKE))
            .savePrefixed(output)

        ShapelessRecipeBuilder
            .shapeless(RecipeCategory.FOOD, RagiumBlocks.SWEET_BERRIES_CAKE)
            .requiresFor(8, Ingredient.of(RagiumItems.SWEET_BERRIES_CAKE_PIECE))
            .unlockedBy("has_cake", has(RagiumItems.SWEET_BERRIES_CAKE_PIECE))
            .savePrefixed(output)

        // Ambrosia
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(RagiumItems.CHOCOLATE, 64)
            .fluidInput(Tags.Fluids.HONEY, 1000 * 64)
            .itemOutput(RagiumItems.AMBROSIA)
            .save(output)
    }

    private fun registerHoney(output: RecipeOutput) {
        // Honey
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.HONEY_BLOCK)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.HONEY_BOTTLE)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluids.HONEY, 250)
            .saveSuffixed(output, "_from_bottle")

        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Items.GLASS_BOTTLE)
            .fluidInput(Tags.Fluids.HONEY, 250)
            .itemOutput(Items.HONEY_BOTTLE)
            .save(output)

        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Tags.Items.GLASS_BLOCKS)
            .fluidInput(Tags.Fluids.HONEY)
            .itemOutput(Items.HONEY_BLOCK)
            .save(output)
        // Bee wax
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.HONEYCOMB_BLOCK)
            .itemOutput(RagiumItems.BEE_WAX, 4)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.HONEYCOMB)
            .itemOutput(RagiumItems.BEE_WAX)
            .fluidOutput(RagiumFluids.HONEY, 250)
            .saveSuffixed(output, "_from_comb")
    }

    private fun registerMeat(output: RecipeOutput) {
        // Raw Food -> Minced Meat
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_meat")

        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_fish")

        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.ROTTEN_FLESH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_rotten")
        // Minced Meat -> Meat Ingot
        HTSingleItemRecipeBuilder
            .compressor(lookup)
            .itemInput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.MEAT_INGOT)
            .save(output)
        // Meat Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.MEAT_INGOT),
                RagiumItems.COOKED_MEAT_INGOT,
                RecipeCategory.FOOD,
                types = HTCookingRecipeBuilder.SMOKING_TYPES,
            ).save(output)
        // Cooked Meat Ingot -> Canned Cooked Meat
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(RagiumItems.COOKED_MEAT_INGOT, 8)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemOutput(RagiumItems.CANNED_COOKED_MEAT, 8)
            .save(output)
    }

    private fun registerMilk(output: RecipeOutput) {
        // Milk + Bucket -> Milk Bucket
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Items.BUCKET)
            .milkInput()
            .itemOutput(Items.MILK_BUCKET)
            .save(output)
        // Milk Bucket -> Milk + Bucket
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.MILK_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(NeoForgeMod.MILK)
            .save(output, NeoForgeMod.MILK.id)

        // Butter
        HTSolidifierRecipeBuilder(lookup)
            .milkInput()
            .catalyst(Tags.Items.INGOTS)
            .itemOutput(RagiumItems.BUTTER)
            .save(output)
        // Sponge Cake
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(RagiumItems.FLOUR, 3)
            .itemInput(Items.SUGAR, 2)
            .itemInput(RagiumItems.BUTTER)
            .itemOutput(RagiumBlocks.SPONGE_CAKE, 4)
            .save(output)

        // Cake
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .milkInput()
            .itemOutput(Items.CAKE)
            .saveSuffixed(output, "_with_sponge_cake")
    }

    private fun registerMushroom(output: RecipeOutput) {
        // Mushroom Stew
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(Tags.Items.MUSHROOMS)
            .milkInput()
            .fluidOutput(RagiumVirtualFluids.MUSHROOM_STEW)
            .saveSuffixed(output, "_from_milk")

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Items.MUSHROOM_STEW)
            .itemOutput(Items.BOWL)
            .fluidOutput(RagiumVirtualFluids.MUSHROOM_STEW)
            .saveSuffixed(output, "_from_bowl")
    }

    private fun registerWart(output: RecipeOutput) {
        // Wart Block -> Wart
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.NETHER_WART_BLOCK)
            .itemOutput(Items.NETHER_WART, 9)
            .save(output)

        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Items.WARPED_WART_BLOCK)
            .itemOutput(RagiumItems.WARPED_WART, 9)
            .save(output)

        // Wart -> XX Sap
        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(Tags.Items.CROPS_NETHER_WART)
            .fluidOutput(RagiumVirtualFluids.CRIMSON_SAP, 10)
            .save(output)

        HTFluidOutputRecipeBuilder
            .extractor(lookup)
            .itemInput(RagiumItemTags.CROPS_WARPED_WART)
            .fluidOutput(RagiumVirtualFluids.WARPED_SAP, 10)
            .save(output)
    }

    private fun registerWheat(output: RecipeOutput) {
        // Flour
        HTSingleItemRecipeBuilder
            .grinder(lookup)
            .itemInput(Tags.Items.CROPS_WHEAT)
            .itemOutput(RagiumItems.FLOUR)
            .save(output)
        // Dough
        HTFluidOutputRecipeBuilder
            .infuser(lookup)
            .itemInput(RagiumItems.FLOUR)
            .waterInput(250)
            .itemOutput(RagiumItems.DOUGH)
            .save(output)
        // Bread
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.DOUGH),
                Items.BREAD,
                RecipeCategory.FOOD,
                types = HTCookingRecipeBuilder.SMOKING_TYPES,
            ).save(output)
    }
}
