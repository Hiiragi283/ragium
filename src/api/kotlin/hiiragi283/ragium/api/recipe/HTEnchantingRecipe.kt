package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.resource.isOf
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTEnchantingRecipe : HTRecipe<HTEnchantingRecipe.Input> {
    fun getRequiredExpAmount(input: Input): Int

    fun getRequiredItemAmount(input: Input): Int

    fun testExperience(input: Input): Boolean {
        val stack: FluidStack = input.fluid
        return HCFluids.EXPERIENCE.isOf(stack) && stack.amount >= getRequiredExpAmount(input)
    }

    @JvmRecord
    data class Input(val book: ItemStack, val item: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = fluid

        override fun getFluidSize(): Int = 0

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> book
            1 -> item
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 2
    }

    //    Serializable    //

    interface Serializable :
        HTEnchantingRecipe,
        HTSerializableRecipe<Input>
}
