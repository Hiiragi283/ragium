package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import java.util.Optional

class HTMachineRecipeCache<T : HTMachineRecipe>(val recipeType: HTRecipeType<T>) {
    private var lastRecipe: ResourceLocation? = null

    /**
     * 指定した[context]と[level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は[Result.failure]
     */
    fun getFirstRecipe(context: HTMachineRecipeContext, level: Level): Result<T> = Optional
        .ofNullable(recipeType.getFirstRecipe(context, level, lastRecipe))
        .map { holder: RecipeHolder<T> ->
            lastRecipe = holder.id
            Result.success(holder.value)
        }.orElseGet {
            lastRecipe = null
            Result.failure(HTMachineException.NoMatchingRecipe())
        }

    /**
     * 指定した[context]と[level]から最初に一致するレシピを処理します。
     * @throws HTMachineException 処理に失敗した場合
     */
    fun processFirstRecipe(context: HTMachineRecipeContext, level: Level) {
        val recipe: T = getFirstRecipe(context, level).getOrThrow()
        recipe.canProcess(context).getOrThrow()
        recipe.process(context)
    }
}
