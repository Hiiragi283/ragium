package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableStack
import net.minecraft.tags.TagKey
import java.util.function.Predicate

/**
 * [STACK]を判定するインターフェース
 * @param STACK 判定の対象となるクラス
 * @see [HTItemIngredient]
 * @see [HTFluidIngredient]
 * @see [mekanism.api.recipes.ingredients.InputIngredient]
 */
interface HTIngredient<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> : Predicate<STACK> {
    /**
     * 指定された[stack]が条件を満たしているか判定します。
     */
    abstract override fun test(stack: STACK): Boolean

    /**
     * 指定された[stack]が数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(stack: STACK): Boolean

    /**
     * 指定された[stack]から，この[HTIngredient]に合致する数量を返します。
     */
    fun getRequiredAmount(stack: STACK): Int

    /**
     * 条件に合致する[STACK]があるか判定します。
     * @return ない場合は`true`, ある場合は`false`
     */
    fun hasNoMatchingStacks(): Boolean

    fun unwrap(): Either<Pair<TagKey<TYPE>, Int>, List<STACK>>
}
