package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.recipe.handler.cache.HTDoubleRecipeCache
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.material.Fluid
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
    interface Typed<TYPE_A : Any, INPUT_A : TypedInstance<TYPE_A>, TYPE_B : Any, INPUT_B : TypedInstance<TYPE_B>, RECIPE : Any> :
        HTDoubleRecipeLookupHandler<INPUT_A, INPUT_B, RECIPE> {
        override val recipeCache: HTDoubleRecipeCache.Typed<TYPE_A, INPUT_A, TYPE_B, INPUT_B, RECIPE>

        fun hasRecipeForFirst(input: TypedInstance<TYPE_A>): Boolean = recipeCache.findAllHolderForFirst(input, createLookupContext()).any()

        fun hasRecipeForSecond(input: TypedInstance<TYPE_B>): Boolean = recipeCache.findAllHolderForSecond(input, createLookupContext()).any()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItemFluid<RECIPE : Any> : Typed<Item, ItemInstance, Fluid, FluidInstance, RECIPE>
}
