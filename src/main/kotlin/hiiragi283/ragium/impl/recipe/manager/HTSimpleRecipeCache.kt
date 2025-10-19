package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.recipe.manager.HTRecipeCache
import hiiragi283.ragium.api.recipe.manager.HTRecipeFinder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

internal class HTSimpleRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val finder: HTRecipeFinder<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: ResourceLocation? = null

    override fun getFirstHolder(input: INPUT, level: Level): RecipeHolder<RECIPE>? =
        finder.getRecipeFor(level.recipeManager, input, level, lastRecipe).also { holder ->
            lastRecipe = holder?.id
        }
}
