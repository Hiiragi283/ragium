package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toDataResult
import hiiragi283.ragium.api.extension.validate
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.util.Identifier
import net.minecraft.world.World

/**
 * [Identifier]を利用したレシピのキャッシュクラス
 * @param I [RecipeInput]を継承したクラス
 * @param R [Recipe]を継承したクラス
 * @param recipeType レシピのタイプ
 * @see [net.minecraft.recipe.RecipeManager.createCachedMatchGetter]
 */
class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) {
    private var id: Identifier? = null

    /**
     * 指定した[input]に一致する最初のレシピを返します。
     * @return [DataResult]で包まれた値
     */
    fun getFirstMatch(input: I, world: World): DataResult<R> = world.recipeManager
        .getFirstMatch(recipeType, input, world, id)
        .toDataResult { "Failed to find matching recipe!" }
        .ifSuccess { this.id = it.id }
        .ifError { this.id = null }
        .map(RecipeEntry<R>::value)

    /**
     * 指定した[input]に一致する全てのレシピを返します。
     * @return [DataResult]で包まれた値
     */
    fun getAllMatches(input: I, world: World): DataResult<List<R>> = world.recipeManager
        .getAllMatches(recipeType, input, world)
        .map(RecipeEntry<R>::value)
        .toDataResult { "Failed to find matching recipes!" }
        .validate(List<R>::isNotEmpty) { "Failed to find matching recipes!" }
}
