package hiiragi283.lib.recipe.ingredient

import net.minecraft.core.TypedInstance
import java.util.function.Predicate

/**
 * Hiiragi Seriesで使用される，レシピの材料を判定するインターフェースです。
 *
 * 参照 : [Mekanism - InputIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/InputIngredient.java)
 * @param TYPE 材料の種類のクラス
 * @param INSTANCE 材料の種類を保持するインスタンスのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTIngredient<TYPE : Any, INSTANCE : TypedInstance<TYPE>> : Predicate<INSTANCE> {
    /**
     * 条件を満たしているか判定します。
     */
    override fun test(instance: INSTANCE): Boolean

    /**
     * 数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(instance: INSTANCE): Boolean

    /**
     * 消費される入力を取得します。
     */
    fun getMatchingStack(instance: INSTANCE): INSTANCE
}
