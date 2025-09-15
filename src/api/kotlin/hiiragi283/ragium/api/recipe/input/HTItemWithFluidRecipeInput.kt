package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [ItemStack]と[FluidStack]を受け取る[RecipeInput]の実装
 */
@JvmRecord
data class HTItemWithFluidRecipeInput(val item: ItemStack, val fluid: FluidStack) : RecipeInput {
    constructor(slot: HTItemSlot, tank: HTFluidTank) : this(slot.getStack(), tank.getStack())

    override fun getItem(index: Int): ItemStack = item

    override fun size(): Int = 1

    override fun isEmpty(): Boolean = item.isEmpty && fluid.isEmpty
}
