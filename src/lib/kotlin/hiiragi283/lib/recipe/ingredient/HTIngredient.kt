package hiiragi283.lib.recipe.ingredient

import java.util.function.Predicate
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter

/**
 * Hiiragi Seriesで使用される，レシピの材料を判定するインターフェースです。
 *
 * 参照 : [Mekanism - InputIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/InputIngredient.java)
 * @param INSTANCE 材料の種類のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTIngredient<INSTANCE> : Predicate<INSTANCE> where INSTANCE : TypedInstance<*>, INSTANCE : DataComponentGetter {
    /**
     * 条件を満たしているか判定します。
     */
    override fun test(instance: INSTANCE): Boolean

    /**
     * 数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(instance: INSTANCE): Boolean

    /**
     * 消費される数量を取得します。
     */
    fun getRequiredAmount(instance: INSTANCE): Int
}
