package hiiragi283.lib.recipe.ingredient

import java.util.function.Predicate
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.util.context.ContextMap

/**
 * Hiiragi Seriesで使用される，レシピの材料を判定するインターフェースです。
 *
 * 参照 : [Mekanism - InputIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/InputIngredient.java)
 * @param TYPE 材料の種類のクラス
 * @param STACK 一致する材料のプレビューに使用されるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTIngredient<TYPE : Any, STACK> : Predicate<TypedInstance<TYPE>> where STACK : TypedInstance<TYPE>, STACK : DataComponentGetter {
    /**
     * 条件を満たしているか判定します。
     */
    override fun test(instance: TypedInstance<TYPE>): Boolean

    /**
     * 数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(instance: TypedInstance<TYPE>): Boolean

    /**
     * 消費される数量を取得します。
     */
    fun getRequiredAmount(instance: TypedInstance<TYPE>): Int

    /**
     * 一致する材料のプレビューを取得します。
     */
    fun getPreviewStacks(contextMap: ContextMap): List<STACK>
}
