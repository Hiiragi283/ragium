package hiiragi283.ragium.api.recipe.manager

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * @see [net.minecraft.world.item.crafting.RecipeManager]
 */
interface HTRecipeAccess {
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation? = null,
    ): HTRecipeHolder<RECIPE>?

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: HTRecipeHolder<RECIPE>? = null,
    ): HTRecipeHolder<RECIPE>?

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipesFor(type: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>>
}
