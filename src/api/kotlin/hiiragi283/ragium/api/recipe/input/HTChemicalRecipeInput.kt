package hiiragi283.ragium.api.recipe.input

import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTChemicalRecipeInput(val items: Pair<ItemStack, ItemStack>, val fluids: Pair<FluidStack, FluidStack>) : HTFluidRecipeInput {
    override fun getFluid(index: Int): FluidStack = when (index) {
        0 -> fluids.first
        1 -> fluids.second
        else -> FluidStack.EMPTY
    }

    override fun getFluidSize(): Int = 2

    private fun validateItem(stack: ItemStack): ItemStack = when {
        stack.`is`(HiiragiCoreTags.Items.IGNORED_IN_RECIPE_INPUTS) -> ItemStack.EMPTY
        else -> stack
    }

    override fun getItem(index: Int): ItemStack = when (index) {
        0 -> items.first
        1 -> items.second
        else -> ItemStack.EMPTY
    }.let(::validateItem)

    override fun size(): Int = 2
}
