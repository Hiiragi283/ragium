package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

object HTFoodRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerHoney(output)
        registerMeat(output)
    }

    private fun registerHoney(output: RecipeOutput) {
        // Honey
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.HONEY_BLOCK)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.HONEY_BOTTLE)
            .itemOutput(Items.GLASS_BOTTLE)
            .fluidOutput(RagiumFluids.HONEY, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_bottle")
        // Bee wax
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.HONEYCOMB_BLOCK)
            .itemOutput(RagiumItems.BEE_WAX, 4)
            .fluidOutput(RagiumFluids.HONEY)
            .saveSuffixed(output, "_from_block")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .itemInput(Items.HONEYCOMB)
            .itemOutput(RagiumItems.BEE_WAX)
            .fluidOutput(RagiumFluids.HONEY, FluidType.BUCKET_VOLUME / 4)
            .saveSuffixed(output, "_from_comb")
    }

    private fun registerMeat(output: RecipeOutput) {
        // Raw Food -> Minced Meat
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.TALLOW)
            .saveSuffixed(output, "_from_meat")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.TALLOW)
            .saveSuffixed(output, "_from_fish")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Items.ROTTEN_FLESH)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_rotten")
        // Minced Meat -> Meat Ingot
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.COMPRESSOR)
            .itemInput(RagiumItems.MINCED_MEAT)
            .itemOutput(RagiumItems.MEAT_INGOT)
            .save(output)
        // Meat Ingot -> Cooked Meat Ingot
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.MEAT_INGOT),
                RagiumItems.COOKED_MEAT_INGOT,
                RecipeCategory.FOOD,
                types = setOf(HTCookingRecipeBuilder.Type.SMELTING, HTCookingRecipeBuilder.Type.SMOKING),
            ).unlockedBy("has_meat_ingot", has(RagiumItems.MEAT_INGOT))
            .save(output)
    }
}
