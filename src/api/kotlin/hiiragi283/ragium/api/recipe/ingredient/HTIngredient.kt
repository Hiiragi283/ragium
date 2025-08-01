package hiiragi283.ragium.api.recipe.ingredient

import java.util.function.Predicate

/**
 * @see [mekanism.api.recipes.ingredients.InputIngredient]
 */
interface HTIngredient<T : Any> : Predicate<T> {
    abstract override fun test(stack: T): Boolean

    fun testOnlyType(stack: T): Boolean

    fun getMatchingStack(stack: T): T

    fun getRequiredAmount(stack: T): Int

    fun hasNoMatchingStacks(): Boolean

    fun getMatchingStacks(): List<T>
}
