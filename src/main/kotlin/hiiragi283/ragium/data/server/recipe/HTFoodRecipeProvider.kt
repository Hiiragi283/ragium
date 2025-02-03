package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTExtractorRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.requiresFor
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.extension.sources
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTFoodRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        registerWheat(output)

        registerMilk(output)
        registerHoney(output)
        registerChocolate(output)
        registerCinnamon(output)

        registerMeat(output)
    }

    private fun registerWheat(output: RecipeOutput) {
        // Flour
        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Tags.Items.CROPS_WHEAT)
            .itemOutput(RagiumItems.FLOUR)
            .save(output)
        // Dough
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(RagiumItems.FLOUR, 3)
            .waterInput()
            .itemOutput(RagiumItems.DOUGH, 3)
            .save(output)
        // Bread
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.DOUGH),
                Items.BREAD,
                RecipeCategory.FOOD,
                types = HTCookingRecipeBuilder.SMOKING_TYPES,
            ).unlockedBy("has_dough", has(RagiumItems.DOUGH))
            .save(output)
    }

    private fun registerMilk(output: RecipeOutput) {
        // Milk
        HTExtractorRecipeBuilder()
            .itemInput(Items.MILK_BUCKET)
            .itemOutput(Items.BUCKET)
            .fluidOutput(NeoForgeMod.MILK)
            .save(output, RagiumAPI.id("milk"))

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Items.BUCKET)
            .milkInput()
            .itemOutput(Items.MILK_BUCKET)
            .save(output)

        // Butter
        HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
            .milkInput()
            .itemOutput(RagiumItems.BUTTER)
            .save(output)
        // Sponge Cake
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(RagiumItems.FLOUR, 3)
            .itemInput(Items.SUGAR, 2)
            .itemInput(RagiumItems.BUTTER)
            .milkInput()
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .itemOutput(RagiumBlocks.SPONGE_CAKE, 4)
            .save(output)

        // Cake
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Tags.Items.CROPS_WHEAT, 3)
            .itemInput(Items.SUGAR, 2)
            .itemInput(Tags.Items.EGGS)
            .milkInput(FluidType.BUCKET_VOLUME * 3)
            .itemOutput(Items.CAKE)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .milkInput(FluidType.BUCKET_VOLUME * 3)
            .itemOutput(Items.CAKE)
            .saveSuffixed(output, "_with_sponge_cake")
    }

    private fun registerHoney(output: RecipeOutput) {
        // Honey
        HTExtractorRecipeBuilder()
            .itemInput(Items.HONEY_BLOCK)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTExtractorRecipeBuilder()
            .itemInput(Items.HONEY_BOTTLE)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluids.HONEY, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_bottle")

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Items.GLASS_BOTTLE)
            .fluidInput(Tags.Fluids.HONEY, FluidType.BUCKET_VOLUME / 4)
            .itemOutput(Items.HONEY_BOTTLE)
            .save(output)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .fluidInput(Tags.Fluids.HONEY)
            .catalyst(Tags.Items.GLASS_BLOCKS)
            .itemOutput(Items.HONEY_BLOCK)
            .save(output)
        // Bee wax
        HTExtractorRecipeBuilder()
            .itemInput(Items.HONEYCOMB_BLOCK)
            .itemOutput(RagiumItems.BEE_WAX, 4)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTExtractorRecipeBuilder()
            .itemInput(Items.HONEYCOMB)
            .itemOutput(RagiumItems.BEE_WAX)
            .fluidOutput(RagiumFluids.HONEY, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_comb")
    }

    private fun registerChocolate(output: RecipeOutput) {
        // Chocolate
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(Items.SUGAR)
            .itemInput(Tags.Items.CROPS_COCOA_BEAN)
            .milkInput()
            .itemOutput(RagiumItems.CHOCOLATE)
            .save(output)

        // Sweet Berries Cake
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
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
    }

    private fun registerCinnamon(output: RecipeOutput) {
        // Cinnamon Powder
        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(RagiumItems.CINNAMON_STICK)
            .itemOutput(RagiumItems.CINNAMON_POWDER, 4)
            .save(output)
        // Cinnamon Roll
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Items.BREAD)
            .itemInput(RagiumItems.CINNAMON_POWDER)
            .milkInput()
            .itemOutput(RagiumItems.CINNAMON_ROLL)
            .save(output)

        // Ambrosia
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(RagiumItems.CHOCOLATE, 64)
            .itemInput(RagiumItems.CINNAMON_POWDER, 64)
            .fluidInput(Tags.Fluids.HONEY, FluidType.BUCKET_VOLUME * 64)
            .catalyst(HTMachineTier.ULTIMATE)
            .itemOutput(RagiumItems.AMBROSIA)
            .save(output)
    }

    private fun registerMeat(output: RecipeOutput) {
        // Raw Food -> Minced Meat
        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.TALLOW)
            .saveSuffixed(output, "_from_meat")

        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.TALLOW)
            .saveSuffixed(output, "_from_fish")

        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Items.ROTTEN_FLESH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_rotten")
        // Minced Meat -> Meat Ingot
        HTMachineRecipeBuilder
            .create(RagiumRecipes.COMPRESSOR)
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
            ).unlockedBy("has_meat_ingot", has(RagiumItems.MEAT_INGOT))
            .save(output)
        // Cooked Meat Ingot -> Canned Cooked Meat
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(RagiumItems.COOKED_MEAT_INGOT, 8)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemOutput(RagiumItems.CANNED_COOKED_MEAT, 8)
            .save(output)
    }
}
