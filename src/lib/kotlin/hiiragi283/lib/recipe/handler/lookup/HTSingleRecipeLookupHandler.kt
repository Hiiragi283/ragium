package hiiragi283.lib.recipe.handler.lookup

import hiiragi283.lib.recipe.handler.cache.HTSingleRecipeCache
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * 単一の入力に対する[HTRecipeLookupHandler]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
interface HTSingleRecipeLookupHandler<INPUT : Any, RECIPE : Any> : HTRecipeLookupHandler {
    val recipeCache: HTSingleRecipeCache<INPUT, RECIPE>

    fun findFirstRecipe(input: INPUT): RECIPE? = recipeCache.findFirstRecipe(input, createLookupContext())

    /**
     * 入力が[TypedInstance]を継承している場合に利用できる[HTSingleRecipeLookupHandler]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface Typed<TYPE : Any, INPUT : TypedInstance<TYPE>, RECIPE : Any> : HTSingleRecipeLookupHandler<INPUT, RECIPE> {
        override val recipeCache: HTSingleRecipeCache.Typed<TYPE, INPUT, RECIPE>

        /**
         * 指定した[input]に対して有効なレシピがあるか判定します。
         */
        fun hasRecipe(input: TypedInstance<TYPE>): Boolean = recipeCache.findAllHolder(input, createLookupContext()).any()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForFluid<RECIPE : Any> : Typed<Fluid, FluidInstance, RECIPE>

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    interface ForItem<RECIPE : Any> : Typed<Item, ItemInstance, RECIPE>
}
