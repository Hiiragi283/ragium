package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [ItemStack]と[FluidStack]を受け取る[RecipeInput]の実装
 */
@JvmRecord
data class HTItemWithFluidRecipeInput(val item: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
    constructor(slot: HTItemSlot, tank: HTFluidTank) : this(slot.getItemStack(), tank.getFluidStack())

    private fun validateItem(): ItemStack =
        item.takeUnless { stack: ItemStack -> stack.`is`(RagiumModTags.Items.IGNORED_IN_RECIPES) } ?: ItemStack.EMPTY

    override fun getItem(index: Int): ItemStack = validateItem()

    override fun getFluid(index: Int): FluidStack = fluid

    override fun size(): Int = 1

    override fun isEmpty(): Boolean = validateItem().isEmpty && fluid.isEmpty
}
