package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.toDataResult
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

/**
 * [ResourceKey]を利用したレシピのキャッシュクラス
 * @param I [RecipeInput]を継承したクラス
 * @param R [Recipe]を継承したクラス
 * @param recipeType レシピのタイプ
 * @see [net.minecraft.world.item.crafting.RecipeManager.CachedCheck]
 */
class HTRecipeCache<I : RecipeInput, R : Recipe<I>>(private val recipeType: RecipeType<R>) {
    private var id: ResourceKey<Recipe<*>>? = null

    /**
     * 指定した[input]に一致する最初のレシピを返します。
     * @return [DataResult]で包まれた値
     */
    fun getFirstMatch(input: I, level: ServerLevel): DataResult<R> = level
        .recipeAccess()
        .getRecipeFor(recipeType, input, level, id)
        .toDataResult { "Failed to find matching recipe!" }
        .ifSuccess { this.id = it.id }
        .ifError { this.id = null }
        .map(RecipeHolder<R>::value)
}
