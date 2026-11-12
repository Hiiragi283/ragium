package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.recipe.base.HTItemCombineRecipe
import hiiragi283.lib.recipe.handler.cache.HTDoubleRecipeCache
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
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

    /**
     * 入力が[TypedInstance]を継承している場合に利用できる[HTDoubleRecipeLookupHandler]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface Typed<TYPE_A : Any, IN_A : TypedInstance<TYPE_A>, TYPE_B : Any, IN_B : TypedInstance<TYPE_B>, RECIPE : Any> :
        HTDoubleRecipeLookupHandler<IN_A, IN_B, RECIPE> {
        override val recipeCache: HTDoubleRecipeCache.Typed<TYPE_A, IN_A, TYPE_B, IN_B, RECIPE>

        /**
         * 指定した[first]に対して有効なレシピがあるか判定します。
         */
        fun hasRecipeForFirst(first: TypedInstance<TYPE_A>): Boolean = recipeCache.findAllHolderForFirst(first, createLookupContext()).any()

        fun hasRecipeForSecond(second: TypedInstance<TYPE_B>): Boolean = recipeCache.findAllHolderForSecond(second, createLookupContext()).any()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItemCombine : Typed<Item, ItemInstance, Item, ItemInstance, HTItemCombineRecipe>
}
