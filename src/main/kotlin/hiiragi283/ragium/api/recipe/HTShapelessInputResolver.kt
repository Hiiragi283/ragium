package hiiragi283.ragium.api.recipe

import net.minecraft.item.ItemStack

object HTShapelessInputResolver {
    @JvmStatic
    fun canMatch(ingredients: Collection<HTItemIngredient>, stacks: Collection<ItemStack>): Boolean {
        val ingredients1: List<HTItemIngredient> = ingredients.filterNot(HTItemIngredient::isEmpty)
        if (ingredients1.isEmpty()) return true
        val stacks1: MutableList<ItemStack> = stacks.filterNot(ItemStack::isEmpty).toMutableList()
        if (stacks1.isEmpty()) return false
        // RagiumAPI.LOGGER.info("===")
        var successCount = 0
        ingredient@ for (ingredient: HTItemIngredient in ingredients1) {
            // RagiumAPI.LOGGER.info("Current ingredient: $ingredient")
            for (stack: ItemStack in stacks1) {
                // RagiumAPI.LOGGER.info("Current stack: $stack")
                if (ingredient.test(stack)) {
                    // RagiumAPI.LOGGER.info("$ingredient matches $stack!")
                    stacks1.remove(stack)
                    successCount++
                    continue@ingredient
                }
            }
        }
        return stacks1.isEmpty() && ingredients1.size == successCount
    }

    @JvmStatic
    fun resolve(ingredients: Collection<HTItemIngredient>, stacks: Collection<ItemStack>): Map<HTItemIngredient, ItemStack> {
        val builder: MutableMap<HTItemIngredient, ItemStack> = mutableMapOf()
        ingredient@ for (ingredient: HTItemIngredient in ingredients) {
            for (stack: ItemStack in stacks) {
                if (ingredient.test(stack)) {
                    builder.put(ingredient, stack)
                    continue@ingredient
                }
            }
        }
        return builder
    }
}
