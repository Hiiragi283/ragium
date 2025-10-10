package hiiragi283.ragium.api.recipe.manager

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

/**
 * @see [net.minecraft.world.item.crafting.RecipeManager]
 */
@JvmInline
value class HTRecipeAccess(val recipeManager: RecipeManager) {
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): HTRecipeHolder<RECIPE>? = recipeManager.getRecipeFor(type, input, level, lastRecipe).map(::HTRecipeHolder).getOrNull()

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: HTRecipeHolder<RECIPE>?,
    ): HTRecipeHolder<RECIPE>? = recipeManager.getRecipeFor(type, input, level, lastRecipe?.holder).map(::HTRecipeHolder).getOrNull()

    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipesFor(type: RecipeType<RECIPE>): Sequence<HTRecipeHolder<RECIPE>> =
        recipeManager.getAllRecipesFor(type).asSequence().map(::HTRecipeHolder)
}
