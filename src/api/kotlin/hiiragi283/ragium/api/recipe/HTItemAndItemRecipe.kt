package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.world.item.ItemStack

interface HTItemAndItemRecipe : HTProcessingRecipe<HTDoubleRecipeInput> {
    fun testFirstItem(stack: ItemStack): Boolean

    fun testSecondItem(stack: ItemStack): Boolean

    fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int>

    override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return testFirstItem(first) && testSecondItem(second)
    }

    //    Serializable    //

    interface Serializable :
        HTItemAndItemRecipe,
        HTSerializableRecipe<HTDoubleRecipeInput>
}
