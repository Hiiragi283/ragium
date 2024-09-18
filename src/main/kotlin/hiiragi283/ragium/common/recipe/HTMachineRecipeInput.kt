package hiiragi283.ragium.common.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.input.RecipeInput

class HTMachineRecipeInput(
    private val first: ItemStack,
    private val second: ItemStack,
    private val third: ItemStack,
    private val catalyst: ItemStack,
) : RecipeInput {
    fun matches(
        first: WeightedIngredient?,
        second: WeightedIngredient?,
        third: WeightedIngredient?,
        catalyst: Ingredient?,
    ): Boolean = matchesInternal(
        first ?: WeightedIngredient.EMPTY,
        second ?: WeightedIngredient.EMPTY,
        third ?: WeightedIngredient.EMPTY,
        catalyst ?: Ingredient.EMPTY,
    )

    private fun matchesInternal(
        first: WeightedIngredient,
        second: WeightedIngredient,
        third: WeightedIngredient,
        catalyst: Ingredient,
    ): Boolean = when {
        !first.test(this.first) -> false
        !second.test(this.second) -> false
        !third.test(this.third) -> false
        !catalyst.test(this.catalyst) -> false
        else -> true
    }

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
        0 -> first
        1 -> second
        2 -> third
        3 -> catalyst
        else -> throw IndexOutOfBoundsException()
    }

    override fun getSize(): Int = 4
}
