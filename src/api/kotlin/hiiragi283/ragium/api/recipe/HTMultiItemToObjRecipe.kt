package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.world.item.ItemStack

/**
 * 複数の[ItemStack]から[ItemStack]を生成するレシピのインターフェース
 * @see [HTCombineItemToItemRecipe]
 * @see [HTItemWithCatalystToItemRecipe]
 */
interface HTMultiItemToObjRecipe : HTRecipe<HTMultiItemRecipeInput> {
    companion object {
        @JvmStatic
        fun getMatchingSlots(ingredients: List<HTItemIngredient>, stacks: List<ItemStack>): IntArray {
            if (ingredients.isEmpty() || (stacks.isEmpty() || stacks.all(ItemStack::isEmpty))) return intArrayOf()
            if (ingredients.size > stacks.size) return intArrayOf()

            val stacks1: MutableList<ItemStack> = stacks.map(ItemStack::copy).toMutableList()
            val result: MutableList<Int> = MutableList(ingredients.size) { -1 }

            ingredients.forEachIndexed { index: Int, ingredient: HTItemIngredient ->
                stacks1.forEachIndexed stack@{ index1: Int, stack: ItemStack ->
                    val immutable: ImmutableItemStack = stack.toImmutable() ?: return@stack
                    if (ingredient.test(immutable)) {
                        result[index] = index1
                        val count: Int = ingredient.getRequiredAmount(immutable)
                        stack.shrink(count)
                        return@stack
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
        fun hasMatchingSlots(ingredients: List<HTItemIngredient>, stacks: List<ItemStack>): Boolean {
            val slots: IntArray = getMatchingSlots(ingredients, stacks)
            return slots.isNotEmpty() && slots.size == ingredients.size
        }
    }
}
