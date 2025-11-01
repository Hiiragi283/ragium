package hiiragi283.ragium.api.recipe.manager

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

interface HTMaterialRecipeManager {
    fun <RECIPE : Recipe<*>> getAllRecipes(recipeType: RecipeType<RECIPE>): List<RecipeHolder<RECIPE>>

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        recipeType: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): RecipeHolder<RECIPE>?

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        recipeType: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: RecipeHolder<RECIPE>?,
    ): RecipeHolder<RECIPE>?
}
