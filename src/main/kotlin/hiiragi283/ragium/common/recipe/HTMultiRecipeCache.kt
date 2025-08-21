package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTMultiRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeTypes: List<RecipeType<out R>>) : HTRecipeCache<I, R> {
    constructor(vararg recipeTypes: RecipeType<out R>) : this(recipeTypes.toList())

    override var lastRecipe: ResourceLocation? = null
        private set

    override fun getFirstHolder(input: I, level: Level): RecipeHolder<R>? {
        for (type: RecipeType<out R> in recipeTypes) {
            val foundRecipe: RecipeHolder<R>? = level.recipeManager
                .getRecipeFor(type, input, level, lastRecipe)
                .map { holder: RecipeHolder<out R> ->
                    lastRecipe = holder.id
                    RecipeHolder(holder.id, holder.value)
                }.orElseGet {
                    lastRecipe = null
                    null
                }
            if (foundRecipe != null) return foundRecipe
        }
        return null
    }
}
