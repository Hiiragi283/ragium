package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableStack

/**
 * 複数のインプットから完成品を生成するレシピ
 */
interface HTMultiInputsToObjRecipe : HTRecipe<HTMultiRecipeInput> {
    companion object {
        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> getMatchingSlots(ingredients: List<HTIngredient<*, STACK>>, stacks: List<STACK?>): IntArray {
            if (ingredients.isEmpty() || (stacks.isEmpty() || stacks.filterNotNull().isEmpty())) return intArrayOf()
            if (ingredients.size > stacks.size) return intArrayOf()

            val stacks1: MutableList<STACK?> = stacks.toMutableList()
            val result: MutableList<Int> = MutableList(ingredients.size) { -1 }

            ingredients.forEachIndexed { index: Int, ingredient: HTIngredient<*, STACK> ->
                stacks1.forEachIndexed stack@{ index1: Int, stack: STACK? ->
                    if (stack != null) {
                        if (ingredient.test(stack)) {
                            result[index] = index1
                            val count: Int = ingredient.getRequiredAmount(stack)
                            stacks1[index1] = stack.copyWithAmount(stack.amount() - count)
                            return@stack
                        }
                    }
                }
            }
            result.removeIf { it < 0 }
            return when {
                result.size != ingredients.size -> intArrayOf()
                else -> result.toIntArray()
            }
        }

        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> hasMatchingSlots(ingredients: List<HTIngredient<*, STACK>>, stacks: List<STACK?>): Boolean {
            val slots: IntArray = getMatchingSlots(ingredients, stacks)
            return slots.isNotEmpty() && slots.size == ingredients.size
        }

        fun <STACK : ImmutableStack<*, STACK>> isEmpty(stacks: List<STACK?>): Boolean = stacks.isEmpty() || stacks.filterNotNull().isEmpty()
    }
}
