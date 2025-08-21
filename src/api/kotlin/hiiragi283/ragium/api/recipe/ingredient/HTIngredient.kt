package hiiragi283.ragium.api.recipe.ingredient

import java.util.function.Predicate

/**
 * @see [mekanism.api.recipes.ingredients.InputIngredient]
 */
interface HTIngredient<STACK : Any> : Predicate<STACK> {
    abstract override fun test(stack: STACK): Boolean

    fun testOnlyType(stack: STACK): Boolean

    fun getMatchingStack(stack: STACK): STACK

    fun getRequiredAmount(stack: STACK): Int

    fun hasNoMatchingStacks(): Boolean

    fun getMatchingStacks(): List<STACK>
}
