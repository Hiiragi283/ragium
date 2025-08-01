package hiiragi283.ragium.api.util

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(val recipeType: RecipeType<R>) {
    var lastRecipe: ResourceLocation? = null
        private set

    /**
     * 指定した[input]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: I, level: Level): R? = getFirstHolder(input, level)?.value

    /**
     * 指定した[input]と[level]から最初に一致する[net.minecraft.world.item.crafting.RecipeHolder]を返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstHolder(input: I, level: Level): RecipeHolder<R>? = level.recipeManager
        .getRecipeFor(recipeType, input, level, lastRecipe)
        .map { holder: RecipeHolder<R> ->
            lastRecipe = holder.id
            holder
        }.orElseGet {
            lastRecipe = null
            null
        }
}
