package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.storage.HTFluidVariantStack
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.item.ItemStack

object HTShapelessInputResolver {
    //    Fluid    //

    @JvmStatic
    fun canMatch(storage: Storage<FluidVariant>, stacks: Collection<HTFluidVariantStack>): Boolean {
        if (!storage.supportsExtraction()) return false
        val stacks1: MutableList<HTFluidVariantStack> = stacks.filterNot(HTFluidVariantStack::isEmpty).toMutableList()
        if (stacks1.isEmpty()) return false
        var successCount = 0
        for (stack: HTFluidVariantStack in stacks1) {
            val (variant: FluidVariant, amount: Long) = stack
            if (StorageUtil.tryInsertStacking(storage, variant, amount, null) == amount) {
                stacks1.remove(stack)
                successCount++
            }
        }
        return stacks1.isEmpty() && stacks.size == successCount
    }

    //    Item    //

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
