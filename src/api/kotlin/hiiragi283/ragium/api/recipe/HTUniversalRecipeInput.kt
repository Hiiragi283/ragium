package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

data class HTUniversalRecipeInput(val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun fromItems(vararg stacks: ItemStack): HTUniversalRecipeInput = HTUniversalRecipeInput(listOf(*stacks), listOf())

        @JvmStatic
        fun fromFluids(vararg stacks: FluidStack): HTUniversalRecipeInput = HTUniversalRecipeInput(listOf(), listOf(*stacks))
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
