package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.recipe.multi.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTMultiRecipeInput(val items: List<ImmutableItemStack?>, val fluids: List<ImmutableFluidStack?>) : HTFluidRecipeInput {
    companion object {
        @JvmStatic
        fun fromSlots(slots: List<HTItemSlot>): HTMultiRecipeInput = HTMultiRecipeInput(slots.map(HTItemSlot::getStack), listOf())

        @JvmStatic
        fun fromSlots(vararg slots: HTItemSlot): HTMultiRecipeInput = fromSlots(slots.toList())
    }

    constructor(item: ImmutableItemStack?, fluid: ImmutableFluidStack?) : this(listOfNotNull(item), listOfNotNull(fluid))

    private fun validateItem(index: Int): ItemStack = items[index]
        ?.takeUnless { stack: ImmutableItemStack -> stack.isOf(RagiumModTags.Items.IGNORED_IN_RECIPES) }
        ?.unwrap()
        ?: ItemStack.EMPTY

    override fun getItem(index: Int): ItemStack = validateItem(index)

    override fun getFluid(index: Int): FluidStack = fluids[index]?.unwrap() ?: FluidStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = HTMultiInputsToObjRecipe.isEmpty(items) && HTMultiInputsToObjRecipe.isEmpty(fluids)
}
