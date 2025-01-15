package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.toResult
import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

/**
 * [ResourceKey]を利用したレシピのキャッシュクラス
 * @param I [RecipeInput]を継承したクラス
 * @param R [Recipe]を継承したクラス
 * @param recipeType レシピのタイプ
 * @see [net.minecraft.world.item.crafting.RecipeManager.CachedCheck]
 */
class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) {
    constructor(recipeType: Supplier<RecipeType<R>>) : this(recipeType.get())

    private var id: ResourceLocation? = null

    /**
     * 指定した[input]に一致する最初のレシピを返します。
     * @return [Result]で包まれた値
     */
    fun getFirstMatch(input: I, level: ServerLevel): Result<R> = level
        .recipeManager
        .getRecipeFor(recipeType, input, level, id)
        .toResult { HTMachineException.NoMatchingRecipe(false) }
        .onSuccess { this.id = it.id }
        .onFailure { this.id = null }
        .map(RecipeHolder<R>::value)
}
