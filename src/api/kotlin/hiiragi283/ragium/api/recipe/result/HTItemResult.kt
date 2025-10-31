package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.stack.ImmutableItemStack

/**
 * [ImmutableItemStack]向けの[HTRecipeResult]の実装
 */
interface HTItemResult : HTRecipeResult<ImmutableItemStack> {
    fun copyWithCount(count: Int): HTItemResult
}
