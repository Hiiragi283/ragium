package hiiragi283.ragium.api.recipe.manager

import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

fun interface HTRecipeGetter<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    fun getAllHolders(access: HTRecipeAccess): Sequence<HTRecipeHolder<RECIPE>>

    fun getAllRecipes(access: HTRecipeAccess): Sequence<RECIPE> = getAllHolders(access).map(HTRecipeHolder<RECIPE>::recipe)
}
