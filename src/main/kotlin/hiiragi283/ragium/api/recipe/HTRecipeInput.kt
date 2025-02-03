package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HTRecipeInput private constructor(val items: List<ItemStack>, val fluids: List<FluidStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun of(items: List<ItemStack>, fluids: List<FluidStack>): HTRecipeInput = HTRecipeInput(items, fluids)

        @JvmStatic
        fun of(item: ItemStack): HTRecipeInput = HTRecipeInput(listOf(item), listOf())

        @JvmStatic
        fun of(fluid: FluidStack): HTRecipeInput = HTRecipeInput(listOf(), listOf(fluid))

        @JvmStatic
        fun of(item: ItemStack, fluid: FluidStack): HTRecipeInput = HTRecipeInput(listOf(item), listOf(fluid))
    }

    override fun getItem(index: Int): ItemStack = items.getOrNull(index) ?: ItemStack.EMPTY

    fun getFluid(index: Int): FluidStack = fluids.getOrNull(index) ?: FluidStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() && fluids.isEmpty()
}
