package hiiragi283.ragium.impl.recipe.ingredient

import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import net.minecraft.core.HolderSet

abstract class HTIngredientBase<TYPE : Any, STACK : Any>(protected val holderSet: HolderSet<TYPE>, protected val amount: Int) :
    HTIngredient<STACK> {
    init {
        require(amount >= 1)
    }

    final override fun getRequiredAmount(stack: STACK): Int = if (test(stack)) this.amount else 0

    final override fun hasNoMatchingStacks(): Boolean = holderSet.toList().isEmpty()
}
