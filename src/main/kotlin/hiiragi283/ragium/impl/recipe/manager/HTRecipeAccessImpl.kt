package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.recipe.manager.HTRecipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

internal class HTRecipeAccessImpl(private val recipeManager: RecipeManager) : HTRecipeAccess {
    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): HTRecipeHolder<RECIPE>? = recipeManager.getRecipeFor(type, input, level, lastRecipe).map(::HTRecipeHolder).getOrNull()

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        type: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: HTRecipeHolder<RECIPE>?,
    ): HTRecipeHolder<RECIPE>? = recipeManager.getRecipeFor(type, input, level, lastRecipe?.toVanilla()).map(::HTRecipeHolder).getOrNull()

    override fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getAllRecipesFor(
        type: RecipeType<RECIPE>,
    ): Sequence<HTRecipeHolder<RECIPE>> = recipeManager.getAllRecipesFor(type).asSequence().map(::HTRecipeHolder)
}
