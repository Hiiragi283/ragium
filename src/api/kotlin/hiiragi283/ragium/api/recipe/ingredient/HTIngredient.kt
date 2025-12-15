package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.tag.getName
import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.tags.TagKey
import java.util.function.Predicate

/**
 * [STACK]を判定するインターフェース
 * @param STACK 判定の対象となるクラス
 * @see HTItemIngredient
 * @see HTFluidIngredient
 * @see mekanism.api.recipes.ingredients.InputIngredient
 */
interface HTIngredient<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> :
    Predicate<STACK>,
    HTHasText {
    /**
     * 指定された[stack]が条件を満たしているか判定します。
     */
    override fun test(stack: STACK): Boolean = testOnlyType(stack) && stack.amount() >= getRequiredAmount()

    /**
     * 指定された[stack]が数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(stack: STACK): Boolean

    /**
     * この[HTIngredient]に合致する数量を返します。
     */
    fun getRequiredAmount(): Int

    fun unwrap(): Either<Pair<TagKey<TYPE>, Int>, List<STACK>>

    override fun getText(): Component = unwrap().map(
        { (tagKey: TagKey<TYPE>, _) -> tagKey.getName() },
        { stacks: List<STACK> -> ComponentUtils.formatList(stacks, HTHasText::getText) },
    )
}
