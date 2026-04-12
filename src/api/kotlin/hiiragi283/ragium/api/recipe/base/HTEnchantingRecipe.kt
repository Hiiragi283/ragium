package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTEnchantingRecipe : HTSerializableRecipe<HTEnchantingRecipe.Input> {
    override fun test(input: Input): Boolean {
        val (resource: HTFluidResourceType, amount: Int) = input.fluid.toResourcePair() ?: return false
        val bool1: Boolean = HCFluids.EXPERIENCE.isOf(resource) && amount >= getRequiredExpAmount(input)
        val bool2: Boolean = testBook(input.book)
        val bool3: Boolean = testItem(input.item)
        return bool1 && bool2 && bool3
    }

    fun testBook(stack: ItemStack): Boolean

    fun testItem(stack: ItemStack): Boolean

    fun getRequiredExpAmount(input: Input): Int

    fun getRequiredItemAmount(input: Input): Int

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
}
