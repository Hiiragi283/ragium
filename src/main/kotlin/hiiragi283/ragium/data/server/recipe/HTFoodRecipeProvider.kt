package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object HTFoodRecipeProvider : RecipeProviderChild {
    override fun buildRecipes(output: RecipeOutput) {
        registerMeat(output)
    }

    private fun registerMeat(output: RecipeOutput) {
        // Raw Food -> Minced Meat
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_MEAT)
            .itemOutput(RagiumItems.MINCED_MEAT)
            .saveSuffixed(output, "_from_meat")

        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.GRINDER)
            .itemInput(Tags.Items.FOODS_RAW_FISH)
            .itemOutput(RagiumItems.MINCED_MEAT)
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
