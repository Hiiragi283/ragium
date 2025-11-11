package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.recipe.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@JvmRecord
data class HTMultiRecipeInput(val items: List<ImmutableItemStack?>, val fluids: List<ImmutableFluidStack?>) : RecipeInput {
    companion object {
        @JvmStatic
        fun fromSlots(slots: List<HTItemSlot>): HTMultiRecipeInput = HTMultiRecipeInput(slots.map(HTItemSlot::getStack), listOf())

        @JvmStatic
        fun fromSlots(vararg slots: HTItemSlot): HTMultiRecipeInput = fromSlots(slots.toList())
    }
    
    override fun getItem(index: Int): ItemStack = items[index]?.unwrap() ?: ItemStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = HTMultiInputsToObjRecipe.isEmpty(items) && HTMultiInputsToObjRecipe.isEmpty(fluids)
}
