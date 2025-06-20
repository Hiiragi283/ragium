package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data class HTUniversalRecipeInput(val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun fromSlots(slots: List<HTItemSlot>): HTUniversalRecipeInput = HTUniversalRecipeInput(slots.map(HTItemSlot::stack), listOf())

        @JvmStatic
        fun fromTanks(tanks: List<HTFluidTank>): HTUniversalRecipeInput = HTUniversalRecipeInput(listOf(), tanks.map(HTFluidTank::stack))
    }

    override fun getItem(index: Int): ItemStack = items[index]

    fun getFluid(index: Int): FluidStack = fluids[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean {
        val bool1: Boolean = items.isEmpty() || items.all(ItemStack::isEmpty)
        val bool2: Boolean = fluids.isEmpty() || fluids.all(FluidStack::isEmpty)
        return bool1 && bool2
    }
}
