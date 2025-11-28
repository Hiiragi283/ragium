package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

@ConsistentCopyVisibility
@JvmRecord
data class HTDoubleRecipeInput private constructor(val first: ItemStack, val second: ItemStack) : RecipeInput {
    constructor(first: HTItemSlot, second: HTItemSlot) : this(first.getItemStack(), second.getItemStack())

    override fun getItem(index: Int): ItemStack = when (index) {
        0 -> first
        1 -> second
        else -> ItemStack.EMPTY
    }

    override fun size(): Int = 2
}
