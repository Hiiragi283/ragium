package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.recipe.handler.cache.HTDoubleRecipeCache
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * ２つの入力に対する[HTRecipeLookupHandler]の拡張インターフェースです。
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTDoubleRecipeLookupHandler<INPUT_A : Any, INPUT_B : Any, RECIPE : Any> : HTRecipeLookupHandler {
    val recipeCache: HTDoubleRecipeCache<INPUT_A, INPUT_B, RECIPE>

    fun findFirstRecipe(first: INPUT_A, second: INPUT_B): RECIPE? = recipeCache.findFirstRecipe(first, second, createLookupContext())

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItemFluid<RECIPE : Any> : HTDoubleRecipeLookupHandler<ItemInstance, FluidInstance, RECIPE>
}
