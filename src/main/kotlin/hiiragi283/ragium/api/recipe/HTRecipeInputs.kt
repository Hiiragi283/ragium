package hiiragi283.ragium.api.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

object HTRecipeInputs {
    @JvmStatic
    fun single(input: ItemStack): Single = Single(input)

    @JvmStatic
    fun double(first: ItemStack, second: ItemStack): Double = Double(first, second)

    @JvmStatic
    fun triple(first: ItemStack, second: ItemStack, third: ItemStack): Triple = Triple(first, second, third)

    //    Single    //

    data class Single(val stack: ItemStack) : RecipeInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> stack
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 1
    }

    //    Double    //

    data class Double(val first: ItemStack, val second: ItemStack) : RecipeInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> first
            1 -> second
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 2
    }

    //    Triple    //

    data class Triple(val first: ItemStack, val second: ItemStack, val third: ItemStack) : RecipeInput {
        override fun getStackInSlot(slot: Int): ItemStack = when (slot) {
            0 -> first
            1 -> second
            2 -> third
            else -> ItemStack.EMPTY
        }

        override fun getSize(): Int = 3
    }
}
