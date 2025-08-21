package hiiragi283.ragium.api.recipe

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

interface HTRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    val lastRecipe: ResourceLocation?

    /**
     * 指定した[input]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = getFirstHolder(input, level)?.value

    /**
     * 指定した[input]と[level]から最初に一致する[net.minecraft.world.item.crafting.RecipeHolder]を返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstHolder(input: INPUT, level: Level): RecipeHolder<RECIPE>?
}
