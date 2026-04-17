package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

interface HTEnchantingRecipe : HTRecipe<HTEnchantingRecipe.Input> {
    fun getRequiredExpAmount(input: Input): Int

    fun getRequiredAdditionAmount(input: Input): Int

    interface Serializable :
        HTEnchantingRecipe,
        HTSerializableRecipe<Input>

    @JvmRecord
    data class Input(val base: ItemStack, val addition: ItemStack, val expAmount: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> base
            1 -> addition
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 2
    }
}
