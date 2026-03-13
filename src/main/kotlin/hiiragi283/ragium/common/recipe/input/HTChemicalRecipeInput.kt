package hiiragi283.ragium.common.recipe.input

import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTChemicalRecipeInput(val items: Map<HTItemResourceType, Int>, val fluids: Map<HTFluidResourceType, Int>) : HTFluidRecipeInput {
    override fun getFluid(index: Int): FluidStack {
        val (resource: HTFluidResourceType, amount: Int) = fluids.entries.elementAt(index)
        return resource.toStack(amount)
    }

    override fun getFluidSize(): Int = fluids.size

    override fun getItem(index: Int): ItemStack {
        val (resource: HTItemResourceType, count: Int) = items.entries.elementAt(index)
        return resource.toStack(count)
    }

    override fun size(): Int = items.size
}
