package hiiragi283.ragium.api.recipe

import net.minecraft.item.ItemStack

object HTShapelessInputResolver {
    @JvmStatic
    fun canMatch(ingredients: Collection<HTItemIngredient>, stacks: Collection<ItemStack>): Boolean {
        if (ingredients.isEmpty() && stacks.isEmpty()) return true
        if (ingredients.isEmpty() || stacks.isEmpty()) return false
        if (ingredients.size > stacks.size) return false
        val stacks1: MutableList<ItemStack> = stacks.filterNot(ItemStack::isEmpty).toMutableList()
        // RagiumAPI.LOGGER.info("===")
        var successCount = 0
        ingredient@ for (ingredient: HTItemIngredient in ingredients) {
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
        return stacks1.isEmpty() && ingredients.size == successCount
    }

    @JvmStatic
    fun resolve(ingredients: Collection<HTItemIngredient>, stacks: Collection<ItemStack>): Map<HTItemIngredient, ItemStack> {
        if (ingredients.size > stacks.size) return mapOf()
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
